package tm.salam.gpstracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.salam.gpstracker.dto.GpsTrackerDTO;
import tm.salam.gpstracker.helper.ResponseTransfer;
import tm.salam.gpstracker.models.GpsTracker;
import tm.salam.gpstracker.service.GpsTrackerService;

import javax.websocket.server.PathParam;
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
    public ResponseEntity getAllGpsTrackers(){

        Map<Object,Object> response=new HashMap<>();

        response.put("status",true);
        response.put("gpstrackers",gpsTrackerService.getAllGpsTrackerDTO());

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/{id}",produces = "application/json")
    public ResponseEntity getGpsTrackerById(@PathVariable("id") int id){

        System.out.println(id);
        GpsTrackerDTO gpsTrackerDTO=gpsTrackerService.getGpsTrackerDTOById(id);
        Map<Object,Object>response=new HashMap<>();

        if(gpsTrackerDTO!=null){

            response.put("gpstracker",gpsTrackerDTO);
            response.put("status",true);
        }else{
            response.put("message","gpstracker not found");
            response.put("status",false);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/add/gpstracker",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer addGpsTracker(@ModelAttribute GpsTrackerDTO gpsTrackerDTO){

        if(!Objects.equals(gpsTrackerDTO.getSimcardNumber().substring(0,5),"+9936")){

            return new ResponseTransfer("simcard number is invalid",false);
        }

        return gpsTrackerService.addGpsTracker(gpsTrackerDTO);
    }

    @PutMapping(path="/edit/{id}",produces = "application/json")
    public ResponseTransfer editGpsTrackerById(@PathVariable("id") int id,
                                           @ModelAttribute GpsTrackerDTO gpsTrackerDTO){

        if(!Objects.equals(gpsTrackerDTO.getSimcardNumber().substring(0,4),"+993")){

            return new ResponseTransfer("simcard number is invalid",false);
        }

        return gpsTrackerService.editGpsTrackerById(id,gpsTrackerDTO);
    }

    @DeleteMapping(path = "/remove",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = "application/json")
    public ResponseTransfer deleteGpsTrackerById(int id){

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
