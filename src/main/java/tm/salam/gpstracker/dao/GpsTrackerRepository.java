package tm.salam.gpstracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tm.salam.gpstracker.models.GpsTracker;

import java.util.List;

@Repository
public interface GpsTrackerRepository extends JpaRepository<GpsTracker,Integer> {

    GpsTracker findGpsTrackerByDeviceId(String deviceId);
    GpsTracker findGpsTrackerBySimcardNumber(String simcardNumber);
    GpsTracker findGpsTrackerById(int id);
    void deleteGpsTrackerByDeviceId(String deviceId);
    void deleteGpsTrackerBySimcardNumber(String simcardNumber);
    void deleteGpsTrackerById(int id);
    List<GpsTracker> getGpsTrackersByOrderCardNotNull();
    List<GpsTracker> getGpsTrackersByOrderCardNull();
    GpsTracker findGpsTrackerByOrderCard(String orderCard);

}
