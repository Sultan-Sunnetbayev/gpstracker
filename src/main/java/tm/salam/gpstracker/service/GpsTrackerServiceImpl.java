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
    public ResponseTransfer addGpsTracker(GpsTracker gpsTracker){

        GpsTracker temporal=gpsTrackerRepository.findGpsTrackerByDeviceId(gpsTracker.getDeviceId());

        if(temporal!=null){

            return new ResponseTransfer("this gps tracker's deviceId already added", false);
        }
        temporal=gpsTrackerRepository.findGpsTrackerBySimcardNumber(gpsTracker.getSimcardNumber());
        if(temporal!=null){

            return new ResponseTransfer("this gps tracker's simcard number already added",false);
        }

        gpsTrackerRepository.save(gpsTracker);

        return new ResponseTransfer("gps tracker successfull added",true);
    }

    @Override
    @Transactional
    public ResponseTransfer editGpsTrackerByParameter(String parameter, GpsTracker gpsTracker){

        GpsTracker temporal=gpsTrackerRepository.findGpsTrackerBySimcardNumber(parameter);

        if(temporal!=null){

            if(gpsTracker.getDeviceId()!=temporal.getDeviceId()){
                if(gpsTrackerRepository.findGpsTrackerByDeviceId(gpsTracker.getDeviceId())!=null){

                    return new ResponseTransfer("gps tracker's device id already added",false);
                }
            }
            if(gpsTracker.getSimcardNumber()!=temporal.getSimcardNumber()){
                if(gpsTrackerRepository.findGpsTrackerBySimcardNumber(gpsTracker.getSimcardNumber())!=null){

                    return new ResponseTransfer("gps tracker's simcard number already added",false);
                }
            }
            temporal.setName(gpsTracker.getName());
            temporal.setDeviceId(gpsTracker.getDeviceId());
            temporal.setSimcardNumber(gpsTracker.getSimcardNumber());
            temporal.setLogin(gpsTracker.getLogin());
            temporal.setPassword(gpsTracker.getPassword());
            gpsTrackerRepository.save(temporal);

            return new ResponseTransfer("gps tracker successfull edited",true);
        }
        temporal=gpsTrackerRepository.findGpsTrackerByDeviceId(parameter);
        if(temporal!=null){

            temporal.setName(gpsTracker.getName());
            temporal.setDeviceId(gpsTracker.getDeviceId());
            temporal.setSimcardNumber(gpsTracker.getSimcardNumber());
            temporal.setLogin(gpsTracker.getLogin());
            temporal.setPassword(gpsTracker.getPassword());
//            gpsTrackerRepository.save(gpsTracker);

            return new ResponseTransfer("gps tracker successfull edited",true);
        }

        return new ResponseTransfer("gps tracker don't found with by parameter",false);
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
