package tm.salam.gpstracker.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "gpstrackers")
public class GpsTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    @NotBlank(message = "device name is mandatory")
    @NotEmpty(message = "device name is empty")
    private String name;
    @Column(name = "simcard_number")
    @Pattern(regexp = "\\+\\d{11}",message = "simcard number is invalid")
    private String simcardNumber;
    @Column(name = "device_id")
    @NotBlank(message = "deviceId is mandatory")
    @NotEmpty(message = "deviceId is empty")
    private String deviceId;
    @Column(name = "login")
    @NotBlank(message = "login is mandatory")
    @NotEmpty(message = "login is empty")
    @Size(min = 3, max= 10, message = "length login should be greater than 2 and less than 11")
    private String login;
    @Column(name = "password")
    @NotBlank(message = "password is mandatory")
    @NotEmpty(message = "password is empty")
    @Size(min = 3, max= 15, message = "length password should be greater than 2 and less than 16")
    private String password;
    @Column(name = "created")
    @CreationTimestamp
    private LocalDateTime created;
    @Column(name = "updated")
    @UpdateTimestamp
    private LocalDateTime updated;
    @Column(name = "order_card")
    private String orderCard;
    @OneToMany(mappedBy = "gpsTracker",cascade = CascadeType.ALL)
    private List<Coordinate> coordinates;

}
