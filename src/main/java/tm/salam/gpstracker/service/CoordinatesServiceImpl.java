package tm.salam.gpstracker.service;

import org.apache.tomcat.jni.Local;
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

    @Scheduled(cron = "0 0/3 * * * *")
    @Override
    public void SendAndReadSms() throws InterruptedException {

        gsmService.initialize(gsmService.getSystemPorts()[1]);

        ArrayList<String[]>sms=gsmService.readSms();

        System.out.println(sms.size());
        System.out.println();
        for(int i=0;i<sms.size();i++){

            for(int j=0;j<sms.get(i).length;j++){

                System.out.println(sms.get(i)[j]);
            }
        }
        System.out.println("starting");
        for (int i=0;i<sms.size();i++){

//            System.out.println(sms.get(i));
            String[] temporal=sms.get(i);
            if(temporal.length>7) {
//                System.out.println(temporal[7]);
                GpsTracker gpsTracker=gpsTrackerService.getGpsTrackerBySimcardNumber(temporal[3]);
                if(gpsTracker!=null){

                    String[] hlp = new String[3];

                    temporal[7]=temporal[7].toLowerCase();
                    int ind=temporal[7].indexOf("lat");
                    if(ind==-1){

                        continue;
                    }else{
                        ind+=4;
                        hlp[0]="";
                        while (temporal[7].charAt(ind)!=' ') {
                            hlp[0] += temporal[7].charAt(ind++);
                        }
                    }
                    ind=temporal[7].indexOf("long");
                    if(ind==-1){

                        continue;
                    }else{
                        ind+=5;
                        hlp[1]="";
                        while (temporal[7].charAt(ind)!=' ') {
                            hlp[1] += temporal[7].charAt(ind++);
                        }
                    }
                    ind=temporal[7].indexOf("alt");
                    if(ind==-1){

                        continue;
                    }else{
                        ind+=4;
                        hlp[2]="";
                        while (temporal[7].charAt(ind)!=' ') {
                            hlp[2] += temporal[7].charAt(ind++);
                        }
                    }
//                    for(String s:hlp){
//                        System.out.println(s);
//                    }
//                    System.out.println(gpsTracker);
                    Coordinates coordinates=Coordinates.builder()
                            .lat(Double.parseDouble(hlp[0]))
                            .lon(Double.parseDouble(hlp[1]))
                            .alt(Double.parseDouble(hlp[2]))
                            .gpsTracker(gpsTracker)
                            .build();

                    coordinatesRepository.save(coordinates);
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

                if(hlp>0){
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

}
