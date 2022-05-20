package tm.salam.gpstracker.service;

import org.springframework.transaction.annotation.Transactional;
import tm.salam.gpstracker.dto.GpsTrackerDTO;
import tm.salam.gpstracker.helper.ResponseTransfer;
import tm.salam.gpstracker.models.GpsTracker;

import java.util.List;

public interface GpsTrackerService {


    List<GpsTracker> getAllGpsTrackers();

    List<GpsTrackerDTO>getAllGpsTrackerDTO();

    GpsTrackerDTO getGpsTrackerDTOByDeviceId(String deviceId);

    GpsTracker getGpsTrackerByDeviceId(String deviceId);

    GpsTrackerDTO getGpsTrackerDTOBySimcardNumber(String simcardNumber);

    GpsTracker getGpsTrackerBySimcardNumber(String simcardNumber);

    GpsTracker getGpsTrackerById(int id);

    @Transactional
    ResponseTransfer deleteGpsTrackerByDeviceId(String deviceId);

    GpsTrackerDTO getGpsTrackerDTOById(int id);

    @Transactional
    ResponseTransfer editGpsTrackerById(int id, GpsTrackerDTO gpsTrackerDTO);

    @Transactional
    ResponseTransfer addGpsTracker(GpsTrackerDTO gpsTrackerDTO);

    @Transactional
    ResponseTransfer deleteGpsTrackerById(int id);

    @Transactional
    ResponseTransfer deleteGpsTrackerBySimcardNumber(String simcardNumber);

    List<GpsTracker> getOccupiedGpsTrackers();

    List<GpsTrackerDTO> getOccupiedGpsTrackerDTOS();

    List<GpsTrackerDTO> getLooseGpsTrackerDTOS();

    GpsTrackerDTO getGpsTrackerDTOByOrderCard(String orderCard);

    @Transactional
    ResponseTransfer setOrderGpsTracker(int id, String orderCard);

    @Transactional
    ResponseTransfer trushOrderFromGpsTracker(int id);
}
