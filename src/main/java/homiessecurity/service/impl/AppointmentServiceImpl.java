package homiessecurity.service.impl;

import homiessecurity.entities.Appointment;
import homiessecurity.service.AppointmentService;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Override
    public Appointment addAppointment(Appointment appointment) {
        return null;
    }

    @Override
    public Appointment getAppointmentById(Integer appointmentId) {
        return null;
    }

    @Override
    public void deleteAppointment(Integer appointmentId) {

    }

    @Override
    public Appointment updateAppointment(Integer appointmentId, Appointment appointment) {
        return null;
    }

    @Override
    public Appointment respondAppointment(Integer appointmentId, String action) {
        return null;
    }
}
