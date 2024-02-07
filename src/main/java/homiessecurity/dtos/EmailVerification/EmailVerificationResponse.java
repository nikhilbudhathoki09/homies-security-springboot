package homiessecurity.dtos.EmailVerification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EmailVerificationResponse {
    private String otp;
    private String verificationToken;
}
