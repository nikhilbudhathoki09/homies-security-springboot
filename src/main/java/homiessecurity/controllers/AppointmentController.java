package homiessecurity.controllers;


import homiessecurity.dtos.Appointments.AppointmentRequestDto;
import homiessecurity.dtos.khalti.KhaltiResponseDTO;
import homiessecurity.entities.Appointment;
import homiessecurity.exceptions.CustomCommonException;
import homiessecurity.payload.ApiResponse;
import homiessecurity.service.AppointmentService;
import homiessecurity.service.CloudinaryService;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin("*")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService,CloudinaryService cloudinaryService) {
        this.appointmentService = appointmentService;
        this.cloudinaryService = cloudinaryService;
    }

//    @PostMapping("/create-appointment")
//    public ResponseEntity<Appointment> createAppointment(
//            @RequestParam("userId") Integer userId,
//            @RequestParam("providerId") Integer providerId,
//            @RequestParam("serviceId") Integer serviceId,
//            @ModelAttribute AppointmentRequestDto appointmentRequestDto,
//            @RequestParam(value = "appointmentImage", required = false) MultipartFile appointmentImage) {
//
//        if (appointmentImage != null) {
//            String imageUrl = cloudinaryService.uploadImage(appointmentImage, "appointmentImages");
//            appointmentRequestDto.setAppointmentImageUrl(imageUrl); // Set the image URL in the DTO
//        }
//        Appointment newAppointment = appointmentService.addAppointment(appointmentRequestDto, userId, providerId, serviceId);
//        return new ResponseEntity<>(newAppointment, HttpStatus.CREATED);
//    }
@PostMapping("/create-appointment")
public ResponseEntity<KhaltiResponseDTO> createAppointment(
        @RequestParam("userId") Integer userId,
        @RequestParam("providerId") Integer providerId,
        @RequestParam("serviceId") Integer serviceId,
        @ModelAttribute AppointmentRequestDto appointmentRequestDto,
        @RequestParam(value = "appointmentImage", required = false) MultipartFile appointmentImage) {

    if (appointmentImage != null) {
        String imageUrl = cloudinaryService.uploadImage(appointmentImage, "appointmentImages");
        appointmentRequestDto.setAppointmentImageUrl(imageUrl); // Set the image URL in the DTO
    }
    KhaltiResponseDTO res = appointmentService.addAppointment(appointmentRequestDto, userId, providerId, serviceId);
    return new ResponseEntity<KhaltiResponseDTO>(res, HttpStatus.CREATED);
}



    @PutMapping("/appointments/respond")
    public ResponseEntity<Appointment> respondAppointment(@RequestParam("appointmentId") Integer appointmentId,
                                                          @RequestParam("action") String action){
        Appointment updatedAppointment = appointmentService.respondAppointment(appointmentId, action);
        return ResponseEntity.ok(updatedAppointment);

    }

    @GetMapping("/appointments/{appointmentId}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Integer appointmentId) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);
            return ResponseEntity.ok(appointment);
        } catch (CustomCommonException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/appointments/{appointmentId}")
    public ResponseEntity<ApiResponse> deleteAppointment(@PathVariable Integer appointmentId) {
        try {
            ApiResponse res = appointmentService.deleteAppointment(appointmentId);
            return new ResponseEntity<ApiResponse>(res, HttpStatus.OK);
        } catch (CustomCommonException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/appointments/provider/{providerId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByProviderId(@PathVariable Integer providerId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByProviderId(providerId);
        return ResponseEntity.ok(appointments);
    }

    // Method to get appointments by userId
    @GetMapping("/appointments/user/{userId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByUserId(@PathVariable Integer userId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByUserId(userId);
        return ResponseEntity.ok(appointments);
    }

    // Method to get all appointments
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/appointments/cancel")
    public ResponseEntity<ApiResponse> cancelAppointment(@RequestParam Integer appointmentId) {
        try {
            ApiResponse res = appointmentService.cancelAppointment(appointmentId);
            return new ResponseEntity<ApiResponse>(res, HttpStatus.OK);
        } catch (CustomCommonException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/appointments/status")
    public ResponseEntity<List<Appointment>> getAppointmentsByStatus(@RequestParam String status) {
        List<Appointment> appointments = appointmentService.getAppointmentsByStatus(status);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/appointments/incoming-requests")
    public ResponseEntity<List<Appointment>> getIncomingAppointmentsByProviderId(@RequestParam("providerId") Integer providerId) {
        List<Appointment> appointments = appointmentService.getIncomingAppointmentsByProviderId(providerId);
        return ResponseEntity.ok(appointments);
    }

//    @PostMapping("/khalti/update")
//    public ResponseEntity<?> updateKhalti(
//            @RequestParam(name = "pidx") String pidx,
//            @RequestParam(name = "status") String status,
//            @RequestParam(name = "bookingId") String bookingId,
//            @RequestParam(name = "totalAmount") long totalAmount
//    ){
//        try {
//            String res = this.appointmentService.updatePaymentTable(pidx, appointmentId);
//            if (Objects.equals(res, "SuccessFull enter")) {
//                ApiResponse apiResponse = new ApiResponse<>(200, "success", res);
//                return ResponseEntity.ok().body(apiResponse);
//            } else {
//                // Handle the case where the update operation was not successful
//                ApiResponse<String> apiResponse = new ApiResponse<>(400, "failure", "Failed to update payment table");
//                return ResponseEntity.badRequest().body(apiResponse);
//            }
//        } catch (Exception e) {
//            // Handle exceptions
//            ApiResponse<String> apiResponse = new ApiResponse<>(500, "error", "An error occurred");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
//        }
//    }




}