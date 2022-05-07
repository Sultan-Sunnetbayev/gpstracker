package tm.salam.gpstracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.salam.gpstracker.dto.GpsTrackerDTO;
import tm.salam.gpstracker.helper.ResponseTransfer;
import tm.salam.gpstracker.models.GpsTracker;
import tm.salam.gpstracker.service.GpsTrackerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/gpstracker")
public class GpsTrackerController {

    private final GpsTrackerService gpsTrackerService;

    @Autowired
    public GpsTrackerController(GpsTrackerService gpsTrackerService) {
        this.gpsTrackerService = gpsTrackerService;
    }

    @GetMapping(produces = "application/json")
    public List<GpsTrackerDTO> ShowAllGpsTrackers(){

        return gpsTrackerService.getAllGpsTrackerDTO();
    }

    @GetMapping(path = "/{id}",produces = "application/json")
    @ResponseBody
    public ResponseEntity getGpsTrackerById(@PathVariable("id") String id){

        Map<Object,Object> response=new HashMap<>();
        GpsTrackerDTO gpsTrackerDTO=gpsTrackerService.getGpsTrackerDTOById(Integer.parseInt(id));
        System.out.println(id);

        if(gpsTrackerDTO==null){

            response.put("status",false);
            response.put("gps tracker not found",null);
        }else{

            response.put("status",true);
            response.put("gps tracker",gpsTrackerDTO);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/add",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer addGpsTracker(@ModelAttribute GpsTrackerDTO gpsTrackerDTO){

        if(!Objects.equals(gpsTrackerDTO.getSimcardNumber().substring(0,5),"+9936")){

            return new ResponseTransfer("simcard number is invalid",false);
        }

        return gpsTrackerService.addGpsTracker(gpsTrackerDTO);
    }

    @PutMapping(path="/edit",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer editGpsTracker(@RequestParam("id") int id,
                                           @ModelAttribute GpsTrackerDTO gpsTrackerDTO){

        ResponseTransfer responseTransfer;

        if(!Objects.equals(gpsTrackerDTO.getSimcardNumber().substring(0,4),"+993")){

            responseTransfer=new ResponseTransfer("simcard number is invalid",false);
        }
        GpsTrackerDTO temporal=gpsTrackerService.getGpsTrackerDTOById(id);

        if(temporal==null){

            responseTransfer=new ResponseTransfer("gps tracker not found",false);
        }else{

            responseTransfer=gpsTrackerService.editGpsTrackerByDeviceId(temporal.getDeviceId(),gpsTrackerDTO);
        }

        return responseTransfer;
    }

    @DeleteMapping(path = "/remove",
            produces = "application/json")
    public ResponseTransfer deleteGpsTrackerById(@RequestParam("id") int id){

        return gpsTrackerService.deleteGpsTrackerById(id);
    }

//    @DeleteMapping(path = "/removeGpsTrackerByDeviceId",
//            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
//            produces = "application/json")
//    public ResponseTransfer deleteGpsTrackerByDeviceId(String deviceId){
//
//        return gpsTrackerService.deleteGpsTrackerByDeviceId(deviceId);
//    }
//
//    @DeleteMapping(path = "/removeGpsTrackerBySimcardNumber",
//            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
//            produces = "application/json")
//    public ResponseTransfer deleteGpsTrackerBySimcardNumber(String simcardNumber){
//
//        return gpsTrackerService.deleteGpsTrackerBySimcardNumber(simcardNumber);
//    }
}
