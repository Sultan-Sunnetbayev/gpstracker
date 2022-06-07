package tm.salam.gpstracker.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import tm.salam.gpstracker.dto.CoordinateDTO;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CoordinatesService {

    @Scheduled(cron = "0 0/5 * * * *")
    @Async
    void SendAndReadSms() throws InterruptedException;

    CoordinateDTO getCoordinateByOrderCard(String orderCard);

    CoordinateDTO getCoordinateByOrderCardAndBetweenDates(String deviceId, Date begin, Date end);

    CoordinateDTO getCoordinatesDeviceByOrderCardAndDate(String orderCard, Date date);


}
