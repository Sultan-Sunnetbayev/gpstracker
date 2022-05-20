package tm.salam.gpstracker.service;

import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tm.salam.gpstracker.dao.CoordinateRepository;
import tm.salam.gpstracker.dto.CoordinateDTO;
import tm.salam.gpstracker.models.Coordinate;
import tm.salam.gpstracker.models.GpsTracker;

import java.util.*;

@Service
public class CoordinatesServiceImpl implements CoordinatesService{

    private final GpsTrackerService gpsTrackerService;
    private final CoordinateRepository coordinatesRepository;
    private final GsmService gsmService;
    private final double earthRadius=6378137;

    @Autowired
    public CoordinatesServiceImpl(GpsTrackerService gpsTrackerService,
                                  CoordinateRepository coordinatesRepository,
                                  GsmService gsmService) {
        this.gpsTrackerService = gpsTrackerService;
        this.coordinatesRepository = coordinatesRepository;
        this.gsmService = gsmService;
    }

    private String ReFormatText(int ind,String sms){

        String result="";

        while(ind<sms.length() && sms.charAt(ind)!=' '){
            result+=sms.charAt(ind++);
        }

        return result;
    }
    @Scheduled(cron = "0 0/5 * * * *")
    @Override
    public void SendAndReadSms() throws InterruptedException {

        gsmService.initialize(gsmService.getSystemPorts()[1]);

        ArrayList<String[]> sms = gsmService.readSms();
        boolean chk = false;
        int ind=0;
        String[] hlp = new String[3];

        System.out.println("starting");
        for (int i = 0; i < sms.size(); i++) {

            String[] temporal = sms.get(i);
            for (int j = 0; j < temporal.length; j++) {

                temporal[j] = temporal[j].toLowerCase();
                System.out.println(temporal[j]);

                if (!chk && temporal[j].contains("+993")) {
                    chk = true;
                    ind=j;
                    continue;
                }else {
                    if (chk) {

                        GpsTracker gpsTracker=gpsTrackerService.getGpsTrackerBySimcardNumber(temporal[ind]);
                        if (gpsTracker!=null && temporal[j].contains("lat") && temporal[j].contains("long")) {

                            chk = false;
                            hlp[0] = this.ReFormatText(temporal[j].indexOf("lat") + 4, temporal[j]);
                            hlp[1] = this.ReFormatText(temporal[j].indexOf("long") + 5, temporal[j]);
                            hlp[2] = this.ReFormatText(temporal[j].indexOf("alt") + 4, temporal[j]);

                            if(gpsTracker.getOrderCard()!=null) {
                                Coordinate coordinates = Coordinate.builder()
                                        .lat(Double.parseDouble(hlp[0]))
                                        .lon(Double.parseDouble(hlp[1]))
                                        .alt(Double.parseDouble(hlp[2]))
                                        .orderCard(gpsTracker.getOrderCard())
                                        .gpsTracker(gpsTracker)
                                        .build();

                                coordinatesRepository.save(coordinates);
                            }

                        }
                    }
                }
            }
        }

        List<GpsTracker>gpsTrackers=gpsTrackerService.getOccupiedGpsTrackers();

        for (GpsTracker gpsTracker:gpsTrackers){

            System.out.println(gpsTracker.getSimcardNumber());
            gsmService.sendSms(gpsTracker.getSimcardNumber(),gpsTracker.getLogin()+" "+gpsTracker.getPassword()+" getgps");
            Thread.sleep(2000);

        }

        gsmService.deleteAllSms(gsmService.getSmsStorage()[0]);
        gsmService.deleteAllSms(gsmService.getSmsStorage()[1]);

        gsmService.closePort();
        System.out.println("ending");

    }

    @Override
    public CoordinateDTO getCoordinateByOrderCard(String orderCard){

        List<Coordinate>coordinates=coordinatesRepository.findCoordinatesByOrderCard(orderCard);

        if(coordinates.isEmpty()){

            return null;
        }else{

            return toDTO(coordinates,gpsTrackerService.getGpsTrackerDTOByOrderCard(orderCard).getDeviceId());
        }
    }

    @Override
    public CoordinateDTO getCoordinateByOrderCardAndBetweenDates(String orderCard, Date begin, Date end){

        List<Coordinate>coordinateList=coordinatesRepository.findCoordinatesByOrderCardAndCreationDateBetween(orderCard,begin,end);

        if(coordinateList.isEmpty()){

            return null;
        }else{

            return toDTO(coordinateList,gpsTrackerService.getGpsTrackerDTOByOrderCard(orderCard).getDeviceId());
        }
    }

    private CoordinateDTO toDTO(List<Coordinate>coordinatesList,String deviceId){

        List<List<Double>> temporal = new ArrayList<>();
        double passedWay = 0.0;

        for (Coordinate coordinates : coordinatesList) {

            temporal.add(new ArrayList<Double>(
                    Arrays.asList(coordinates.getLat(),coordinates.getLon())

            ));
        }

        passedWay = CalculatePassedWayByGpsCoordinates(coordinatesList.get(coordinatesList.size() - 1).getLon(),
                coordinatesList.get(coordinatesList.size() - 1).getLat(),
                coordinatesList.get(0).getLon(),
                coordinatesList.get(0).getLat()
        );
        List<Pair<String, Long>> duration = new ArrayList<>();

        long differenceInTime = Math.abs(coordinatesList.get(0).getCreationDateTime().getTime() - coordinatesList.get(coordinatesList.size() - 1).getCreationDateTime().getTime());
        long differenceInSeconds = (differenceInTime / 1000) % 60;
        long differenceInMinutes = (differenceInTime / (1000 * 60)) % 60;
        long differenceInHours = (differenceInTime / (1000 * 60 * 60)) % 24;
        long differenceInDays = (differenceInTime / (1000 * 60 * 60 * 24)) % 365;
        long differenceInYears = (differenceInTime / (10001 * 60 * 60 * 24 * 365));

        duration.add(new Pair<>("years", differenceInYears));
        duration.add(new Pair<>("days", differenceInDays));
        duration.add(new Pair<>("hours", differenceInHours));
        duration.add(new Pair<>("minutes", differenceInMinutes));
        duration.add(new Pair<>("seconds", differenceInSeconds));


        return CoordinateDTO.builder()
                .coordinates(temporal)
                .duration(duration)
                .passedWay(passedWay)
                .id(gpsTrackerService.getGpsTrackerByDeviceId(deviceId).getId())
                .build();

    }

    @Override
    public CoordinateDTO getCoordinatesDeviceByOrderCardAndDate(String orderCard, Date date){

        List<Coordinate>coordinatesList=coordinatesRepository.findCoordinatesByOrderCardAndCreationDate(orderCard,date);
        List<List<Double>>temporal = new ArrayList<>();

        for(Coordinate coordinates:coordinatesList){

            temporal.add(new ArrayList<>(
                    Arrays.asList(coordinates.getLon(),coordinates.getLat(),coordinates.getAlt())
            ));
        }
        CoordinateDTO coordinatesDTO= CoordinateDTO.builder()
                .coordinates(temporal)
                .id(gpsTrackerService.getGpsTrackerDTOByOrderCard(orderCard).getId())
                .build();

        return coordinatesDTO;
    }

    private double DoubleToRadian(double number){

        return number*Math.PI/180.0;
    }

    private double CalculatePassedWayByGpsCoordinates(double lon1, double lat1, double lon2, double lat2){

        double radLat1 = DoubleToRadian(lat1);
        double radLat2 = DoubleToRadian(lat2);
        double a = radLat1 - radLat2;
        double b = DoubleToRadian(lon1) - DoubleToRadian(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * earthRadius;

        return s;
    }

}
