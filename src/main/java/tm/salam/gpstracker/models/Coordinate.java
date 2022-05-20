package tm.salam.gpstracker.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "coordinates")
public class Coordinate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "lat")
    private Double lat;
    @Column(name = "lon")
    private Double lon;
    @Column(name = "alt")
    private Double alt;
    @Column(name = "order_card")
    @NotEmpty(message = "order card is empty")
    @NotBlank(message = "order card is blank")
    private String orderCard;
    @Column(name = "creation_date")
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    @Column(name = "creation_time")
    @CreationTimestamp
    @Temporal(TemporalType.TIME)
    private Date creationTime;
    @Column(name = "creation_date_time")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDateTime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id")
    private GpsTracker gpsTracker;

}