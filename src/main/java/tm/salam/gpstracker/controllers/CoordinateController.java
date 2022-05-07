package tm.salam.gpstracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tm.salam.gpstracker.dto.CoordinatesDTO;
import tm.salam.gpstracker.models.GpsTracker;
import tm.salam.gpstracker.service.CoordinatesService;
import tm.salam.gpstracker.service.GpsTrackerService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/coordinate")
public class CoordinateController {

    private final CoordinatesService coordinatesService;
    private final GpsTrackerService gpsTrackerService;

    @Autowired
    public CoordinateController(CoordinatesService coordinatesService,
                                GpsTrackerService gpsTrackerService) {
        this.coordinatesService = coordinatesService;
        this.gpsTrackerService = gpsTrackerService;
    }

    @GetMapping(path = "/getCoordinateByDeviceId",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = "application/json")
    public ResponseEntity getCoordinateByDeviceId(@RequestParam("deviceId")String deviceId){

        Map<Object,Object>response=new HashMap<>();
        GpsTracker gpsTracker=gpsTrackerService.getGpsTrackerByDeviceId(deviceId);

        if(gpsTracker==null){

            response.put("gps tracker don't found with by device id",false);

        }else{

            response.put("status",true);
            response.put("coordinate",coordinatesService.getCoordinateByDeviceId(deviceId));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/getCoordinateDeviceByDate",
                consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
                produces = "application/json")
    public ResponseEntity getCoordinateDeviceByDate(@RequestParam("deviceId")String deviceId,
                                                          @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd") Date date){

//        System.out.println(date);
        Map<Object,Object>response=new HashMap<>();
        GpsTracker gpsTracker=gpsTrackerService.getGpsTrackerByDeviceId(deviceId);

        if(gpsTracker==null){

            response.put("gps tracker don't found with by device id",false);
        }else{

            List<CoordinatesDTO>coordinatesDTOS=coordinatesService.getCoordinatesDeviceByDate(date,deviceId);
            if(!coordinatesDTOS.isEmpty()){

                response.put("status",true);
                response.put("coordinates",coordinatesDTOS);

            }else{

                coordinatesDTOS=coordinatesService.getCoordinateByNearestDate(date,deviceId);

                if(!coordinatesDTOS.isEmpty()){

                    response.put("status",true);
                    response.put("message","coordinate by nearest date");
                    response.put("coordinates",coordinatesDTOS);
                }else{

                    response.put("status",false);
                    response.put("message","gps tracker don't send coordinates");
                }
            }
        }

        return ResponseEntity.ok(response);
    }
}
