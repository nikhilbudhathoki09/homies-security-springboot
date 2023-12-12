package homiessecurity.dtos.EmailVerification;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OTPVerificationRequest {

    @NotNull(message = "OTP is required")
    @Size(min = 5, max = 5, message = "OTP must be 5 characters long")
    private String otp;

 //TODO: Uncomment this when we have the email verification token
//    private String verificationToken;
}
