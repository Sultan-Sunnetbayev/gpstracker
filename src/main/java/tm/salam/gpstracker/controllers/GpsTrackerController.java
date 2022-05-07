package tm.salam.gpstracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tm.salam.gpstracker.dto.GpsTrackerDTO;
import tm.salam.gpstracker.helper.ResponseTransfer;
import tm.salam.gpstracker.models.GpsTracker;
import tm.salam.gpstracker.service.GpsTrackerService;

import java.util.List;
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

    @GetMapping(path = "/getGpsTrackerByParamater",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = "application/json")
    public GpsTrackerDTO ShowGpsTrackerByParameter(@RequestParam("parameter") String paramater){

        GpsTrackerDTO gpsTrackerDTO=gpsTrackerService.getGpsTrackerDTOByDeviceId(paramater);

        if(gpsTrackerDTO!=null){

            return gpsTrackerDTO;
        }
        gpsTrackerDTO=gpsTrackerService.getGpsTrackerDTOBySimcardNumber(paramater);
        if(gpsTrackerDTO!=null){

            return gpsTrackerDTO;
        }
        for(int i=0;i<paramater.length();i++){

            if(!Character.isDigit(paramater.charAt(i))){

                return null;
            }
        }

        return gpsTrackerService.getGpsTrackerDTOById(Integer.parseInt(paramater));
    }

    @PostMapping(path = "/addGpsTracker",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer addGpsTracker(@ModelAttribute GpsTracker gpsTracker){

        if(!Objects.equals(gpsTracker.getSimcardNumber().substring(0,5),"+9936")){

            return new ResponseTransfer("simcard number is invalid",false);
        }

        return gpsTrackerService.addGpsTracker(gpsTracker);
    }

    @PostMapping(path="/editGpsTracker",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseTransfer editGpsTracker(@RequestParam("parameter") String parameter,
                                           @ModelAttribute GpsTracker gpsTracker){

        if(!Objects.equals(gpsTracker.getSimcardNumber().substring(0,4),"+993")){

            return new ResponseTransfer("simcard number is invalid",false);
        }

        return gpsTrackerService.editGpsTrackerByParameter(parameter,gpsTracker);
    }

    @DeleteMapping(path = "/removeGpsTrackerById",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = "application/json")
    public ResponseTransfer deleteGpsTrackerById(int id){

        return gpsTrackerService.deleteGpsTrackerById(id);
    }

    @DeleteMapping(path = "/removeGpsTrackerByDeviceId",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = "application/json")
    public ResponseTransfer deleteGpsTrackerByDeviceId(String deviceId){

        return gpsTrackerService.deleteGpsTrackerByDeviceId(deviceId);
    }

    @DeleteMapping(path = "/removeGpsTrackerBySimcardNumber",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = "application/json")
    public ResponseTransfer deleteGpsTrackerBySimcardNumber(String simcardNumber){

        return gpsTrackerService.deleteGpsTrackerBySimcardNumber(simcardNumber);
    }
}
