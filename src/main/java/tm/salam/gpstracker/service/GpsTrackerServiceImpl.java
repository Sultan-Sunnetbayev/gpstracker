package tm.salam.gpstracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tm.salam.gpstracker.dao.GpsTrackerRepository;
import tm.salam.gpstracker.dto.GpsTrackerDTO;
import tm.salam.gpstracker.helper.ResponseTransfer;
import tm.salam.gpstracker.models.GpsTracker;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GpsTrackerServiceImpl implements GpsTrackerService{

    private final GpsTrackerRepository gpsTrackerRepository;

    @Autowired
    public GpsTrackerServiceImpl(GpsTrackerRepository gpsTrackerRepository) {
        this.gpsTrackerRepository = gpsTrackerRepository;
    }

    @Override
    public List<GpsTracker>getAllGpsTrackers(){

        return gpsTrackerRepository.findAll();
    }

    @Override
    public List<GpsTrackerDTO>getAllGpsTrackerDTO(){

        return gpsTrackerRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private  GpsTrackerDTO toDTO(GpsTracker gpsTracker) {
        
        return GpsTrackerDTO.builder()
                .id(gpsTracker.getId())
                .name(gpsTracker.getName())
                .simcardNumber(gpsTracker.getSimcardNumber())
                .deviceId(gpsTracker.getDeviceId())
                .build();
    }

    @Override
    public GpsTrackerDTO getGpsTrackerDTOByDeviceId(String deviceId){

        GpsTracker gpsTracker=gpsTrackerRepository.findGpsTrackerByDeviceId(deviceId);

        if(gpsTracker==null){

            return null;
        }else{

            return toDTO(gpsTracker);
        }

    }

    @Override
    public GpsTracker getGpsTrackerByDeviceId(String deviceId){

        GpsTracker gpsTracker=gpsTrackerRepository.findGpsTrackerByDeviceId(deviceId);

        if(gpsTracker==null){

            return null;
        }else{

            return gpsTracker;
        }

    }

    @Override
    public GpsTrackerDTO getGpsTrackerDTOBySimcardNumber(String simcardNumber){

        GpsTracker gpsTracker=gpsTrackerRepository.findGpsTrackerBySimcardNumber(simcardNumber);

        if(gpsTracker==null){

            return null;
        }else{

            return toDTO(gpsTracker);
        }
    }

    @Override
    public GpsTracker getGpsTrackerBySimcardNumber(String simcardNumber){

        GpsTracker gpsTracker=gpsTrackerRepository.findGpsTrackerBySimcardNumber(simcardNumber);

        if(gpsTracker==null){

            return null;
        }else{

            return gpsTracker;
        }
    }

    @Override
    public GpsTrackerDTO getGpsTrackerDTOById(int id){

        GpsTracker gpsTracker=gpsTrackerRepository.findGpsTrackerById(id);

        if(gpsTracker==null){

            return null;
        }else{

            return toDTO(gpsTracker);
        }
    }

    @Override
    public GpsTracker getGpsTrackerById(int id){

        GpsTracker gpsTracker=gpsTrackerRepository.findGpsTrackerById(id);

        if(gpsTracker==null){

            return null;
        }else{

            return gpsTracker;
        }
    }

    @Override
    @Transactional
    public ResponseTransfer addGpsTracker(GpsTrackerDTO gpsTrackerDTO){

        GpsTracker temporal=gpsTrackerRepository.findGpsTrackerByDeviceId(gpsTrackerDTO.getDeviceId());

        if(temporal!=null){

            return new ResponseTransfer("this gps tracker's deviceId already added", false);
        }
        temporal=gpsTrackerRepository.findGpsTrackerBySimcardNumber(gpsTrackerDTO.getSimcardNumber());
        if(temporal!=null){

            return new ResponseTransfer("this gps tracker's simcard number already added",false);
        }

        GpsTracker gpsTracker= GpsTracker.builder()
                .name(gpsTrackerDTO.getName())
                .simcardNumber(gpsTrackerDTO.getSimcardNumber())
                .deviceId(gpsTrackerDTO.getDeviceId())
                .login(gpsTrackerDTO.getLogin())
                .password(gpsTrackerDTO.getPassword())
                .build();

        gpsTrackerRepository.save(gpsTracker);

        return new ResponseTransfer("gps tracker successfull added",true);
    }

    @Override
    @Transactional
    public ResponseTransfer editGpsTrackerByDeviceId(String deviceId, GpsTrackerDTO gpsTrackerDTO){

        GpsTracker temporal=gpsTrackerRepository.findGpsTrackerByDeviceId(deviceId);
        ResponseTransfer responseTransfer;

        if(temporal!=null){

            GpsTracker gpsTracker=gpsTrackerRepository.findGpsTrackerByDeviceId(gpsTrackerDTO.getDeviceId());

            if(gpsTracker!=null){

                if(temporal.getId()!=gpsTracker.getId()){

                    responseTransfer=new ResponseTransfer("deviceId edited gps tracker already added",false);
                }else{

                    temporal.setName(gpsTrackerDTO.getName());
                    temporal.setSimcardNumber(gpsTrackerDTO.getSimcardNumber());
                    temporal.setDeviceId(gpsTrackerDTO.getDeviceId());
                    temporal.setLogin(gpsTrackerDTO.getLogin());
                    temporal.setPassword(gpsTrackerDTO.getPassword());
                    responseTransfer=new ResponseTransfer("gps tracker successfull edited",true);
                }

            }else{

                temporal.setName(gpsTrackerDTO.getName());
                temporal.setDeviceId(gpsTrackerDTO.getDeviceId());
                temporal.setSimcardNumber(gpsTrackerDTO.getSimcardNumber());
                temporal.setLogin(gpsTrackerDTO.getLogin());
                temporal.setPassword(gpsTrackerDTO.getPassword());
                responseTransfer=new ResponseTransfer("gps tracker successfull edited",true);

            }
        }else{

            responseTransfer=new ResponseTransfer("gps tracker not found",false);

        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer editGpsTrackerBySimcardNumber(String simcardNumber, GpsTrackerDTO gpsTrackerDTO){

        GpsTracker temporal=gpsTrackerRepository.findGpsTrackerBySimcardNumber(simcardNumber);
        ResponseTransfer responseTransfer;

        if(temporal!=null){

            GpsTracker gpsTracker=gpsTrackerRepository.findGpsTrackerBySimcardNumber(gpsTrackerDTO.getSimcardNumber());

            if(gpsTracker!=null){

                if(gpsTracker.getId()==temporal.getId()){

                    temporal.setName(gpsTrackerDTO.getName());
                    temporal.setDeviceId(gpsTrackerDTO.getDeviceId());
                    temporal.setSimcardNumber(gpsTrackerDTO.getSimcardNumber());
                    temporal.setLogin(gpsTrackerDTO.getLogin());
                    temporal.setPassword(gpsTrackerDTO.getPassword());
                    responseTransfer=new ResponseTransfer("gps tracker successfull edited",true);

                }else{

                    responseTransfer=new ResponseTransfer("simcard number edited gps tracker already added",false);
                }
            }else{

                temporal.setName(gpsTrackerDTO.getName());
                temporal.setDeviceId(gpsTrackerDTO.getDeviceId());
                temporal.setSimcardNumber(gpsTrackerDTO.getSimcardNumber());
                temporal.setLogin(gpsTracker.getLogin());
                temporal.setPassword(gpsTrackerDTO.getPassword());
                responseTransfer=new ResponseTransfer("gps tracker successfull edited",true);
            }

        }else{
            responseTransfer=new ResponseTransfer("gps tracker not found",false);

        }

        return responseTransfer;
    }

    @Override
    @Transactional
    public ResponseTransfer deleteGpsTrackerById(int id){

        GpsTracker gpsTracker=gpsTrackerRepository.findGpsTrackerById(id);

        if(gpsTracker!=null){

            gpsTrackerRepository.deleteGpsTrackerById(id);

            return new ResponseTransfer("Gps tracker successfull removed",true);
        }else{

            return new ResponseTransfer("Gps tracker don't found with id",false);
        }
    }

    @Override
    @Transactional
    public ResponseTransfer deleteGpsTrackerByDeviceId(String deviceId){

        GpsTracker gpsTracker=gpsTrackerRepository.findGpsTrackerByDeviceId(deviceId);

        if(gpsTracker!=null){

            gpsTrackerRepository.deleteGpsTrackerByDeviceId(deviceId);

            return new ResponseTransfer("Gps tracker successfull removed",true);
        }else{

            return new ResponseTransfer("Gps tracker don't found with deviceId",false);
        }
    }

    @Override
    @Transactional
    public ResponseTransfer deleteGpsTrackerBySimcardNumber(String simcardNumber){

        GpsTracker gpsTracker=gpsTrackerRepository.findGpsTrackerBySimcardNumber(simcardNumber);

        if(gpsTracker!=null){

            gpsTrackerRepository.deleteGpsTrackerBySimcardNumber(simcardNumber);

            return new ResponseTransfer("Gps tracker successfull removed",true);
        }else{

            return new ResponseTransfer("Gps tracker don't found with simcard number",false);
        }
    }

}
