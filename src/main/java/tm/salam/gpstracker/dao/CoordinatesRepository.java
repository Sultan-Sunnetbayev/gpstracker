package tm.salam.gpstracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tm.salam.gpstracker.models.Coordinates;

import java.util.Date;
import java.util.List;

@Repository
public interface CoordinatesRepository extends JpaRepository<Coordinates,Integer> {

    List<Coordinates>findCoordinatesByGpsTracker_DeviceId(String deviceId);
 //   @Query("SELECT c FROM Coordinates c WHERE c.gpsTracker=:deviceId AND c.creationDate=:date")
//    List<Coordinates> findCoordinatesByGpsTracker_DeviceIdAndCreationDate(@Param("deviceId") int deviceId,@Param("date")Date date);

    List<Coordinates>findCoordinatesByGpsTracker_DeviceIdAndCreationDate(String deviceId,Date date);

}
