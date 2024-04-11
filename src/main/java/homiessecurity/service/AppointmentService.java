package homiessecurity.service;

import homiessecurity.dtos.Appointments.AppointmentDto;
import homiessecurity.dtos.Appointments.AppointmentRequestDto;
import homiessecurity.entities.Appointment;
import homiessecurity.entities.Status;
import homiessecurity.payload.ApiResponse;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    Appointment addAppointment(AppointmentRequestDto appointment, Integer userId,
                               Integer providerId, Integer serviceId);

    Appointment getAppointmentById(Integer appointmentId);

    ApiResponse deleteAppointment(Integer appointmentId);

    Appointment updateAppointment(Integer appointmentId, Appointment appointment);

    List<Appointment> getAppointmentsByProviderId(Integer providerId);

    List<Appointment> getAppointmentsByUserId(Integer userId);

    List<Appointment> getIncomingAppointmentsByProviderId(Integer providerId);

    List<Appointment> getAllAppointments();

    public Appointment respondAppointment(Integer appointmentId, String action);

    ApiResponse cancelAppointment(Integer appointmentId);

    List<Appointment> getAppointmentsByStatus(String status);

    public void sendAppointmentReminders();

}
