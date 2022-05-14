package tm.salam.gpstracker.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import tm.salam.gpstracker.dto.CoordinatesDTO;

import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CoordinatesService {

    @Scheduled(cron = "0 0/5 * * * *")
    @Async
    void SendAndReadSms() throws InterruptedException;

    CoordinatesDTO getCoordinateByDeviceId(String id);

    List<CoordinatesDTO> getCoordinatesDeviceByDate(Date date,String deviceID);

    CoordinatesDTO getCoordinateByNearestDate(Date date, String deviceId);
}
