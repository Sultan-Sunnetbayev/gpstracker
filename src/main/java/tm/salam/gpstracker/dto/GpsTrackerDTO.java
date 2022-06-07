package tm.salam.gpstracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GpsTrackerDTO {

    private int id;
    private String name;
    private String simcardNumber;
    private String deviceId;
    private String login;
    private String password;
    private String orderCard;

}
