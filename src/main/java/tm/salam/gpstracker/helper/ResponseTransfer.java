package tm.salam.gpstracker.helper;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTransfer {

    private String message;
    private Boolean status;
}
