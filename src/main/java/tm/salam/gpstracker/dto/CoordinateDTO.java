package tm.salam.gpstracker.dto;

import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoordinateDTO {

//    private Double lat;
//    private Double lon;
//    private Double alt;
//    private String name;
//    private String deviceId;
//    @Temporal(TemporalType.DATE)
//    private Date date;
//    @Temporal(TemporalType.TIME)
//    private Date time;

    List<List<Double>>coordinates;
    int id;
    List<Pair<String,Long>>duration;
    Double passedWay;
}
