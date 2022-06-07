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
import java.util.*;

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

    @GetMapping(path = "/id",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity getGpsTrackerById(@RequestParam("id") int id){

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

    @PostMapping(path = "/add",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer addGpsTracker(@ModelAttribute GpsTrackerDTO gpsTrackerDTO){

        if(!Objects.equals(gpsTrackerDTO.getSimcardNumber().substring(0,4),"+993")){

            return new ResponseTransfer("simcard number is invalid",false);
        }

        return gpsTrackerService.addGpsTracker(gpsTrackerDTO);
    }

    @PutMapping(path="/id/edit",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseTransfer editGpsTrackerById(@RequestParam("id") int id,
                                           @ModelAttribute GpsTrackerDTO gpsTrackerDTO){

        if(!Objects.equals(gpsTrackerDTO.getSimcardNumber().substring(0,4),"+993")){

            return new ResponseTransfer("simcard number is invalid",false);
        }

        return gpsTrackerService.editGpsTrackerById(id,gpsTrackerDTO);
    }

    @DeleteMapping(path = "/remove",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = "application/json")
    public ResponseTransfer deleteGpsTrackerById(@RequestParam("id") int id){

        return gpsTrackerService.deleteGpsTrackerById(id);
    }

    @GetMapping(path = "/occupied",produces = "application/json")
    public ResponseEntity getOccupiedGpsTrackers(){

        List<GpsTrackerDTO>gpsTrackerDTOS=gpsTrackerService.getOccupiedGpsTrackerDTOS();
        List<Object>response=new ArrayList<>();
        Map<Object,Object>temporal=new HashMap<>();

        if(gpsTrackerDTOS.isEmpty()){

            temporal.put("message","all gps trackers loose");
            temporal.put("status",false);
        }else{

            response.add(gpsTrackerDTOS);
            temporal.put("status",true);
        }
        response.add(temporal);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/loose",produces = "application/json")
    public ResponseEntity getLooseGpsTrackers(){

        List<GpsTrackerDTO>gpsTrackerDTOS=gpsTrackerService.getLooseGpsTrackerDTOS();
        List<Object>response=new ArrayList<>();
        Map<Object,Object>temporal=new HashMap<>();

        if(gpsTrackerDTOS.isEmpty()){

            temporal.put("message","all gps trackers occupied");
            temporal.put("status",false);

        }else{

            response.add(gpsTrackerDTOS);
            temporal.put("status",true);
        }
        response.add(temporal);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/orderCard",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseEntity getGpsTrackerByOrderCard(@RequestParam("orderCard")String orderCard){

        GpsTrackerDTO gpsTrackerDTO=gpsTrackerService.getGpsTrackerDTOByOrderCard(orderCard);
        Map<Object,Object>response=new HashMap<>();

        if(gpsTrackerDTO==null){

            response.put("message","this order card don't seted");
            response.put("status",false);
        }else{

            response.put("gpstracker",gpsTrackerDTO);
            response.put("status",true);
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/set/order",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer setOrderToGpsTracker(@RequestParam("id")int id,@RequestParam("orderCard")String orderCard){

        return gpsTrackerService.setOrderGpsTracker(id,orderCard);
    }

    @PutMapping(path = "/trush/order",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer trushOrderFromGpsTracker(@RequestParam("id")int id){

        return gpsTrackerService.trushOrderFromGpsTracker(id);
    }
}
