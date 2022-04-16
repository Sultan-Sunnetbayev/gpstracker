package tm.salam.gpstracker.service;

import org.apache.tomcat.jni.Local;
import org.checkerframework.checker.units.qual.min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tm.salam.gpstracker.dao.CoordinatesRepository;
import tm.salam.gpstracker.dto.CoordinatesDTO;
import tm.salam.gpstracker.models.Coordinates;
import tm.salam.gpstracker.models.GpsTracker;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CoordinatesServiceImpl implements CoordinatesService{

    private final GpsTrackerService gpsTrackerService;
    private final CoordinatesRepository coordinatesRepository;
    private final GsmService gsmService;

    @Autowired
    public CoordinatesServiceImpl(GpsTrackerService gpsTrackerService,
                                  CoordinatesRepository coordinatesRepository,
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

//            System.out.println(sms.get(i));
            String[] temporal = sms.get(i);
            for (int j = 0; j < temporal.length; j++) {

                temporal[j] = temporal[j].toLowerCase();
                System.out.println(temporal[j]);

                if (!chk && temporal[j].contains("+993")) {
                    chk = true;
  //                  System.out.println("yes +993");
                    ind=j;
//                    System.out.println(temporal[ind]);
                    continue;
                }else {
                    if (chk) {

                        GpsTracker gpsTracker=gpsTrackerService.getGpsTrackerBySimcardNumber(temporal[ind]);
                        if (gpsTracker!=null && temporal[j].contains("lat") && temporal[j].contains("long")) {

                            chk = false;
                            hlp[0] = this.ReFormatText(temporal[j].indexOf("lat") + 4, temporal[j]);
                            hlp[1] = this.ReFormatText(temporal[j].indexOf("long") + 5, temporal[j]);
                            hlp[2] = this.ReFormatText(temporal[j].indexOf("alt") + 4, temporal[j]);

                            Coordinates coordinates = Coordinates.builder()
                                    .lat(Double.parseDouble(hlp[0]))
                                    .lon(Double.parseDouble(hlp[1]))
                                    .alt(Double.parseDouble(hlp[2]))
                                    .gpsTracker(gpsTracker)
                                    .build();

//                            System.out.println(coordinates);

                            coordinatesRepository.save(coordinates);

                        }
                    }
                }
            }
        }

        List<GpsTracker>gpsTrackers=gpsTrackerService.getAllGpsTrackers();

        for (GpsTracker gpsTracker:gpsTrackers){

            gsmService.sendSms(gpsTracker.getSimcardNumber(),gpsTracker.getLogin()+" "+gpsTracker.getPassword()+" getgps");
            Thread.sleep(2000);

        }

        gsmService.deleteAllSms(gsmService.getSmsStorage()[0]);
        gsmService.deleteAllSms(gsmService.getSmsStorage()[1]);

        gsmService.closePort();
        System.out.println("ending");

    }

    @Override
    public CoordinatesDTO getCoordinateByDeviceId(String deviceId){

        List<Coordinates> coordinatesList=coordinatesRepository.findCoordinatesByGpsTracker_DeviceId(deviceId);

        if(coordinatesList.isEmpty()){

            return null;
        }else{

            Date date=coordinatesList.get(0).getCreationDateTime();
            int ind=0;

            for(int i=1;i<coordinatesList.size();i++){

                int hlp=date.compareTo(coordinatesList.get(i).getCreationDateTime());

                if(hlp<0){
                    date=(Date) coordinatesList.get(i).getCreationDateTime().clone();
                    ind=i;
                }
            }


            return CoordinatesDTO.builder()
                    .lat(coordinatesList.get(ind).getLat())
                    .lon(coordinatesList.get(ind).getLon())
                    .alt(coordinatesList.get(ind).getAlt())
                    .name(coordinatesList.get(ind).getGpsTracker().getName())
                    .build();
        }
    }

    @Override
    public List<CoordinatesDTO> getCoordinatesDeviceByDate(Date date, String deviceId){

        List<Coordinates>coordinatesList=coordinatesRepository.findCoordinatesByGpsTracker_DeviceIdAndCreationDate(deviceId,date);

        List<CoordinatesDTO>coordinatesDTOS=new ArrayList<>();

        for(Coordinates coordinates:coordinatesList){

            coordinatesDTOS.add(CoordinatesDTO.builder()
                    .lat(coordinates.getLat())
                    .lon(coordinates.getLon())
                    .alt(coordinates.getAlt())
                    .name(coordinates.getGpsTracker().getName())
                    .build());
        }

        return coordinatesDTOS;
    }

    @Override
    public List<CoordinatesDTO>getCoordinateByNearestDate(Date date, String deviceId){

        List<Coordinates>coordinatesList=coordinatesRepository.findCoordinatesByGpsTracker_DeviceId(deviceId);

        if(!coordinatesList.isEmpty()){

            long help,min=Math.abs(date.getTime()-coordinatesList.get(0).getCreationDateTime().getTime());
            int ind=0;

            for (int i=1;i<coordinatesList.size();i++){

                help=Math.abs(date.getTime()-coordinatesList.get(i).getCreationDateTime().getTime());
                if(help<min){

                    min=help;
                    ind=i;
                }
            }
            List<CoordinatesDTO>coordinatesDTOS=new ArrayList<>();

            coordinatesDTOS.add(CoordinatesDTO.builder()
                    .lat(coordinatesList.get(ind).getLat())
                    .lon(coordinatesList.get(ind).getLon())
                    .alt(coordinatesList.get(ind).getAlt())
                    .name(coordinatesList.get(ind).getGpsTracker().getName())
                    .build());

            return coordinatesDTOS;
        }else{

            return null;
        }

    }



}
