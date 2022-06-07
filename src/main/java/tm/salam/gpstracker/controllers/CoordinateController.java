package tm.salam.gpstracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tm.salam.gpstracker.dto.CoordinateDTO;
import tm.salam.gpstracker.dto.GpsTrackerDTO;
import tm.salam.gpstracker.service.CoordinatesService;
import tm.salam.gpstracker.service.GpsTrackerService;

import java.util.*;

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

    @PostMapping(path = "/orderCard/gpstracker",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = "application/json")
    public @ResponseBody ResponseEntity getCoordinateByDeviceId(@RequestParam(value = "orderCard",required = true)String orderCard,
                                                  @RequestParam(value = "begin",required = false)
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin,
                                                  @RequestParam(value = "end",required = false)
                                                      @DateTimeFormat(pattern = "yyyy-MM-dd") Date end){
        List<Object>response=new ArrayList<>();
        Map<Object,Object>temporal=new HashMap<>();
        CoordinateDTO coordinateDTO=null;
        GpsTrackerDTO gpsTrackerDTO=gpsTrackerService.getGpsTrackerDTOByOrderCard(orderCard);

        if(gpsTrackerDTO==null){

            temporal.put("message","gps tracker not found with orderCard");
            temporal.put("status",false);
            response.add(temporal);
        }else {
            if (begin == null && end == null) {

                coordinateDTO = coordinatesService.getCoordinateByOrderCard(orderCard);

            } else if (begin != null && end != null) {

                coordinateDTO = coordinatesService.getCoordinateByOrderCardAndBetweenDates(orderCard, begin, end);
            }

            if (coordinateDTO == null) {

//                response.add(temporal);
//                temporal.put("message", "gps tracker don't send coordinate");
//                temporal.put("id", gpsTrackerService.getGpsTrackerDTOByOrderCard(orderCard).getId());
//                temporal.put("status", false);
                response.add(coordinateDTO);
            } else {

                response.add(coordinateDTO);
            }
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/all/gpstrackers",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public @ResponseBody ResponseEntity getCoordinateAllGpsTracker(@RequestParam(value = "begin",required = false)
                                                                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin,
                                                     @RequestParam(value = "end",required = false)
                                                                               @DateTimeFormat(pattern = "yyyy-MM-dd") Date end){

        List<Object>response=new ArrayList<>();
        List<GpsTrackerDTO>gpsTrackerDTOS=gpsTrackerService.getOccupiedGpsTrackerDTOS();
        for(GpsTrackerDTO gpsTrackerDTO:gpsTrackerDTOS){

            CoordinateDTO coordinateDTO=null;
            Map<Object,Object>temporal=new HashMap<>();

            if(begin==null && end==null){

                coordinateDTO=coordinatesService.getCoordinateByOrderCard(gpsTrackerDTO.getOrderCard());

                if(coordinateDTO!=null){
                    temporal.put("status",true);
                    response.add(coordinateDTO);
                }
            }else if(begin!=null && end!=null){

                coordinateDTO=coordinatesService.getCoordinateByOrderCardAndBetweenDates(gpsTrackerDTO.getOrderCard(),begin,end);
                if(coordinateDTO==null){
//                    temporal.put("message","gps tracker don't send coordinate between dates");
//                    temporal.put("id",gpsTrackerDTO.getId());
//                    temporal.put("status",false);
                    response.add(temporal);
                }else{
//                    temporal.put("status",true);
                    response.add(coordinateDTO);
                }
            }
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/orderCard/date",
                consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
                produces = "application/json")
    public ResponseEntity getCoordinateDeviceByDate(@RequestParam(value = "orderCard",required = true)String orderCard,
                                                    @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd") Date date){

        Map<Object,Object>response=new HashMap<>();
        GpsTrackerDTO gpsTrackerDTO=gpsTrackerService.getGpsTrackerDTOByOrderCard(orderCard);

        if(gpsTrackerDTO==null){

            response.put("gps tracker don't found with by orderCard",false);
        }else{

            CoordinateDTO coordinatesDTO=coordinatesService.getCoordinatesDeviceByOrderCardAndDate(orderCard,date);
            if(coordinatesDTO!=null){

                response.put("status",true);
                response.put("coordinates",coordinatesDTO);

            }else{

                response.put("status",false);
                response.put("message","Not found coordinate gps tracker this date");
                response.put("id",gpsTrackerService.getGpsTrackerDTOByOrderCard(orderCard).getId());
            }
        }

        return ResponseEntity.ok(response);
    }
}
