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
    public ResponseTransfer editGpsTrackerById(int id, GpsTrackerDTO gpsTrackerDTO){

        GpsTracker gpsTracker=gpsTrackerRepository.findGpsTrackerById(id);

        ResponseTransfer responseTransfer;

        if(gpsTracker!=null){

            GpsTracker temporal=gpsTrackerRepository.findGpsTrackerByDeviceId(gpsTracker.getDeviceId());

            if(temporal!=null) {
                if (temporal.getId() != gpsTracker.getId()) {

                    return new ResponseTransfer("deviceId new gps tracker already added", false);
                }
            }
            temporal=gpsTrackerRepository.findGpsTrackerBySimcardNumber(gpsTrackerDTO.getSimcardNumber());

            if(temporal!=null){
                if(temporal.getId()!=gpsTracker.getId()){

                    return new ResponseTransfer("simcard number new gps tracker already added",false);
                }
            }

            gpsTracker.setName(gpsTrackerDTO.getName());
            gpsTracker.setDeviceId(gpsTrackerDTO.getDeviceId());
            gpsTracker.setSimcardNumber(gpsTrackerDTO.getSimcardNumber());
            gpsTracker.setLogin(gpsTrackerDTO.getLogin());
            gpsTracker.setPassword(gpsTrackerDTO.getPassword());
            responseTransfer=new ResponseTransfer("gpstracker successfull edited",true);

        }else{

            responseTransfer=new ResponseTransfer("gps tracker don't found",false);
        }

        return responseTransfer;
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

        GpsTracker saveGpsTracker= GpsTracker.builder()
                .name(gpsTrackerDTO.getName())
                .deviceId(gpsTrackerDTO.getDeviceId())
                .simcardNumber(gpsTrackerDTO.getSimcardNumber())
                .login(gpsTrackerDTO.getLogin())
                .password(gpsTrackerDTO.getPassword())
                .build();

        gpsTrackerRepository.save(saveGpsTracker);

        return new ResponseTransfer("gps tracker successfull added",true);
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
