package homiessecurity.controllers;


import homiessecurity.dtos.Appointments.AppointmentRequestDto;
import homiessecurity.entities.Appointment;
import homiessecurity.exceptions.CustomCommonException;
import homiessecurity.payload.ApiResponse;
import homiessecurity.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin("*")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/create-appointment")
    public ResponseEntity<Appointment> createAppointment(
            @RequestParam("userId") Integer userId,
            @RequestParam("providerId") Integer providerId,
            @RequestParam("serviceId") Integer serviceId,
            @RequestBody AppointmentRequestDto appointmentRequestDto) {
        Appointment newAppointment = appointmentService.addAppointment(appointmentRequestDto, userId, providerId, serviceId);
        return new ResponseEntity<>(newAppointment, HttpStatus.CREATED);
    }

    @PutMapping("/appointments/respond")
    public ResponseEntity<Appointment> respondAppointment(@RequestParam Integer appointmentId, @RequestParam String action) {
        try {
            Appointment updatedAppointment = appointmentService.respondAppointment(appointmentId, action);
            return ResponseEntity.ok(updatedAppointment);
        } catch (CustomCommonException e) {
            return ResponseEntity.badRequest().build();
        }
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



}