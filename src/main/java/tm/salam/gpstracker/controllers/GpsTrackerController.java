package tm.salam.gpstracker.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.bind.annotation.*;
import tm.salam.gpstracker.dto.GpsTrackerDTO;
import tm.salam.gpstracker.helper.CheckToken;
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
    public ResponseEntity getAllGpsTrackers(@RequestHeader("Authorization")String token) throws Exception {

        JsonParser jsonParser= JsonParserFactory.getJsonParser();
        Map<String,?>tokenData=jsonParser.parseMap(JwtHelper.decode(token).getClaims());

        if(tokenData.containsKey("message") && tokenData.containsKey("date")){
            if(!CheckToken.Check(String.valueOf(tokenData.get("message")),String.valueOf(tokenData.get("date")))){

                return ResponseEntity.badRequest().body("wrong token");
            }
        }else{

            return ResponseEntity.badRequest().body("wrong token");
        }

        Map<Object,Object> response=new HashMap<>();

        response.put("status",true);
        response.put("gpstrackers",gpsTrackerService.getAllGpsTrackerDTO());

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/id",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity getGpsTrackerById(@RequestParam("id") int id,
                                            @RequestHeader("Authorization")String token) throws Exception {

        JsonParser jsonParser= JsonParserFactory.getJsonParser();
        Map<String,?>tokenData=jsonParser.parseMap(JwtHelper.decode(token).getClaims());

        if(tokenData.containsKey("message") && tokenData.containsKey("date")){
            if(!CheckToken.Check(String.valueOf(tokenData.get("message")),String.valueOf(tokenData.get("date")))){

                return ResponseEntity.badRequest().body("wrong token");
            }
        }else{

            return ResponseEntity.badRequest().body("wrong token");
        }


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
    public ResponseEntity addGpsTracker(@ModelAttribute GpsTrackerDTO gpsTrackerDTO,
                                          @RequestHeader("Authorization")String token) throws Exception {

        JsonParser jsonParser= JsonParserFactory.getJsonParser();
        Map<String,?>tokenData=jsonParser.parseMap(JwtHelper.decode(token).getClaims());

        if(tokenData.containsKey("message") && tokenData.containsKey("date")){
            if(!CheckToken.Check(String.valueOf(tokenData.get("message")),String.valueOf(tokenData.get("date")))){

                return ResponseEntity.badRequest().body("wrong token");
            }
        }else{
            return ResponseEntity.badRequest().body("wrong token");
        }

        Map<String,Object>response=new HashMap<>();
        if(!Objects.equals(gpsTrackerDTO.getSimcardNumber().substring(0,4),"+993")){

            response.put("message","simcard number is invalid");
            response.put("status",false);

            return ResponseEntity.ok(response);
        }

        ResponseTransfer responseTransfer=gpsTrackerService.addGpsTracker(gpsTrackerDTO);
        response.put("message",responseTransfer.getMessage());
        response.put("status",responseTransfer.getStatus());

        return ResponseEntity.ok(response);
    }

    @PutMapping(path="/id/edit",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity editGpsTrackerById(@RequestParam("id") int id,
                                           @ModelAttribute GpsTrackerDTO gpsTrackerDTO,
                                               @RequestHeader("Authorization")String token) throws Exception {

        JsonParser jsonParser= JsonParserFactory.getJsonParser();
        Map<String,?>tokenData=jsonParser.parseMap(JwtHelper.decode(token).getClaims());

        if(tokenData.containsKey("message") && tokenData.containsKey("date")){
            if(!CheckToken.Check(String.valueOf(tokenData.get("message")),String.valueOf(tokenData.get("date")))){

                return ResponseEntity.badRequest().body("wrong token");
            }
        }else{

            return ResponseEntity.badRequest().body("wrong token");
        }
        Map<String,Object>response=new HashMap<>();

        if(!Objects.equals(gpsTrackerDTO.getSimcardNumber().substring(0,4),"+993")){

            response.put("message","simcard number is invalid");
            response.put("status",false);

            return ResponseEntity.ok(response);
        }
        ResponseTransfer responseTransfer=gpsTrackerService.editGpsTrackerById(id,gpsTrackerDTO);
        response.put("message",responseTransfer.getMessage());
        response.put("status",responseTransfer.getStatus());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/remove",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = "application/json")
    public ResponseEntity deleteGpsTrackerById(@RequestParam("id") int id,
                                                 @RequestHeader("Authorization")String token) throws Exception {

        JsonParser jsonParser= JsonParserFactory.getJsonParser();
        Map<String,?>tokenData=jsonParser.parseMap(JwtHelper.decode(token).getClaims());

        if(tokenData.containsKey("message") && tokenData.containsKey("date")){
            if(!CheckToken.Check(String.valueOf(tokenData.get("message")),String.valueOf(tokenData.get("date")))){

                return ResponseEntity.badRequest().body("wrong token");
            }
        }else{

            return ResponseEntity.badRequest().body("wrong token");
        }
        ResponseTransfer responseTransfer=gpsTrackerService.deleteGpsTrackerById(id);
        Map<Object,Object>response=new HashMap<>();
        response.put("message",responseTransfer.getMessage());
        response.put("status",responseTransfer.getStatus());

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/occupied",produces = "application/json")
    public ResponseEntity getOccupiedGpsTrackers(@RequestHeader("Authorization")String token) throws Exception {

        JsonParser jsonParser= JsonParserFactory.getJsonParser();
        Map<String,?>tokenData=jsonParser.parseMap(JwtHelper.decode(token).getClaims());

        if(tokenData.containsKey("message") && tokenData.containsKey("date")){
            if(!CheckToken.Check(String.valueOf(tokenData.get("message")),String.valueOf(tokenData.get("date")))){

                return ResponseEntity.badRequest().body("wrong token");
            }
        }else{

            return ResponseEntity.badRequest().body("wrong token");
        }

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
    public ResponseEntity getLooseGpsTrackers(@RequestHeader("Authorization")String token) throws Exception {

        JsonParser jsonParser= JsonParserFactory.getJsonParser();
        Map<String,?>tokenData=jsonParser.parseMap(JwtHelper.decode(token).getClaims());

        if(tokenData.containsKey("message") && tokenData.containsKey("date")){
            if(!CheckToken.Check(String.valueOf(tokenData.get("message")),String.valueOf(tokenData.get("date")))){

                return ResponseEntity.badRequest().body("wrong token");
            }
        }else{
            return ResponseEntity.badRequest().body("wrong token");
        }

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
    public ResponseEntity getGpsTrackerByOrderCard(@RequestParam("orderCard")String orderCard,
                                                   @RequestHeader("Authorization")String token) throws Exception {

        JsonParser jsonParser= JsonParserFactory.getJsonParser();
        Map<String,?>tokenData=jsonParser.parseMap(JwtHelper.decode(token).getClaims());

        if(tokenData.containsKey("message") && tokenData.containsKey("date")){
            if(!CheckToken.Check(String.valueOf(tokenData.get("message")),String.valueOf(tokenData.get("date")))){

                return ResponseEntity.badRequest().body("wrong token");
            }
        }else{
            return ResponseEntity.badRequest().body("wrong token");
        }

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
    public ResponseEntity setOrderToGpsTracker(@RequestParam("id")int id,@RequestParam("orderCard")String orderCard,
                                                 @RequestHeader("Authorization")String token) throws Exception {

        JsonParser jsonParser= JsonParserFactory.getJsonParser();
        Map<String,?>tokenData=jsonParser.parseMap(JwtHelper.decode(token).getClaims());

        if(tokenData.containsKey("message") && tokenData.containsKey("date")){
            if(!CheckToken.Check(String.valueOf(tokenData.get("message")),String.valueOf(tokenData.get("date")))){

                return ResponseEntity.badRequest().body("wrong token");
            }
        }else{
            return ResponseEntity.badRequest().body("wrong token");
        }

        Map<String,Object>response=new HashMap<>();
        ResponseTransfer responseTransfer=gpsTrackerService.setOrderGpsTracker(id,orderCard);
        response.put("message",responseTransfer.getMessage());
        response.put("status",responseTransfer.getStatus());

        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/trush/order",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},produces = "application/json")
    public ResponseEntity trushOrderFromGpsTracker(@RequestParam("id")int id,
                                                     @RequestHeader("Authorization")String token) throws Exception {

        JsonParser jsonParser= JsonParserFactory.getJsonParser();
        Map<String,?>tokenData=jsonParser.parseMap(JwtHelper.decode(token).getClaims());

        if(tokenData.containsKey("message") && tokenData.containsKey("date")){
            if(!CheckToken.Check(String.valueOf(tokenData.get("message")),String.valueOf(tokenData.get("date")))){

                return ResponseEntity.badRequest().body("wrong token");
            }
        }else{

            return ResponseEntity.badRequest().body("wrong token");
        }
        ResponseTransfer responseTransfer=gpsTrackerService.trushOrderFromGpsTracker(id);
        Map<String,Object>response=new HashMap<>();
        response.put("message",responseTransfer.getMessage());
        response.put("status",responseTransfer.getStatus());

        return ResponseEntity.ok(response);
    }
}
