package homiessecurity.service;

import homiessecurity.dtos.Appointments.AppointmentDto;
import homiessecurity.dtos.Appointments.AppointmentRequestDto;
import homiessecurity.entities.Appointment;
import homiessecurity.payload.ApiResponse;

import java.util.List;

public interface AppointmentService {

    Appointment addAppointment(AppointmentRequestDto appointment, Integer userId,
                               Integer providerId, Integer serviceId);

    Appointment getAppointmentById(Integer appointmentId);

    ApiResponse deleteAppointment(Integer appointmentId);

    Appointment updateAppointment(Integer appointmentId, Appointment appointment);

    Appointment respondAppointment(Integer appointmentId, String action );

    List<Appointment> getAppointmentsByProviderId(Integer providerId);

    List<Appointment> getAppointmentsByUserId(Integer userId);

    List<Appointment> getAllAppointments();

    ApiResponse cancelAppointment(Integer appointmentId);

    List<Appointment> getAppointmentsByStatus(String status);



}
