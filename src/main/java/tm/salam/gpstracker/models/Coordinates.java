package tm.salam.gpstracker.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "coordinates")
public class Coordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "lat")
    private Double lat;
    @Column(name = "lon")
    private Double lon;
    @Column(name = "alt")
    private Double alt;
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

    @Override
    public String toString() {
        return "Coordinates{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", alt=" + alt +
                ", creationDate=" + creationDate +
                ", creationTime=" + creationTime +
                ", creationDateTime=" + creationDateTime +
                '}';
    }
}