package homiessecurity.dtos.khalti;



import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class KhaltiResponseDTO {

    private String pidx;

    private String payment_url;

    private String expires_at;

    private String expires_in;

    private Integer appointmentId;

    public KhaltiResponseDTO() {
    }

    public KhaltiResponseDTO(String pidx, String payment_url, String expires_at,
                             String expires_in, Integer appointmentId) {
        this.pidx = pidx;
        this.payment_url = payment_url;
        this.expires_at = expires_at;
        this.expires_in = expires_in;
        this.appointmentId = appointmentId;
    }
}