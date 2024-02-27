package homiessecurity.service;

import homiessecurity.entities.Appointment;

public interface AppointmentService {

    Appointment addAppointment(Appointment appointment);

    Appointment getAppointmentById(Integer appointmentId);

    void deleteAppointment(Integer appointmentId);

    Appointment updateAppointment(Integer appointmentId, Appointment appointment);

    Appointment respondAppointment(Integer appointmentId, String action );



}
