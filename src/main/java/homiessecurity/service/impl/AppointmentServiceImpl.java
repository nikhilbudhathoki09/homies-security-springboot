package homiessecurity.service.impl;

import homiessecurity.dtos.Appointments.AppointmentDto;
import homiessecurity.dtos.Appointments.AppointmentRequestDto;
import homiessecurity.dtos.Users.UserDto;
import homiessecurity.entities.*;
import homiessecurity.exceptions.CustomCommonException;
import homiessecurity.exceptions.ResourceNotFoundException;
import homiessecurity.payload.ApiResponse;
import homiessecurity.repository.AppointmentRepository;
import homiessecurity.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final ProviderService providerService;
    private final UserService userService;
    private final AppointmentRepository appointmentRepository;
    private final ServicesService servicesService;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final EmailSenderService emailSenderService;

    @Autowired
    public AppointmentServiceImpl(ProviderService providerService, UserService userService,
                                  AppointmentRepository appointmentRepository, ModelMapper modelMapper,
                                  CloudinaryService cloudinaryService,ServicesService servicesService,
                                  EmailSenderService emailSenderService){
        this.providerService=providerService;
        this.userService=userService;
        this.appointmentRepository=appointmentRepository;
        this.modelMapper=modelMapper;
        this.cloudinaryService=cloudinaryService;
        this.emailSenderService=emailSenderService;
        this.servicesService = servicesService;
    }
    @Override
    public Appointment addAppointment(AppointmentRequestDto appointment, Integer userId, Integer providerId
                                        ,Integer serviceId) {
        ServiceProvider provider = providerService.getProviderById(providerId);
        User user = userService.getRawUserById(userId);
        Services service = servicesService.getServiceById(serviceId);


        Appointment new_appointment = Appointment.builder()
                .description(appointment.getDescription())
                .arrivalTime(appointment.getArrivalTime())
                .appointmentDate(appointment.getArrivalDate())
                .provider(provider)
                .user(user)
                .service(service)
                .status(Status.PENDING) // Assuming "PENDING" is a valid status
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();


        return appointmentRepository.save(new_appointment);
    }

    @Override
    public Appointment getAppointmentById(Integer appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow(() ->
                new ResourceNotFoundException("Appointment", "appointmentId", appointmentId));
    }

    @Override
    public ApiResponse deleteAppointment(Integer appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointmentRepository.delete(appointment);
        return new ApiResponse("Appointment deleted successfully",true);

    }

    @Override
    public Appointment updateAppointment(Integer appointmentId, Appointment appointment) {
        return null;
    }

    @Override
    public Appointment respondAppointment(Integer appointmentId, String action) {
        Appointment appointment = getAppointmentById(appointmentId);
        System.out.println(action);
        switch (action) {
            case "ACCEPTED":
                appointment.setStatus(Status.ACCEPTED);
                appointment.setUpdatedAt(LocalDateTime.now());
                break;
            case "REJECTED":
                appointment.setStatus(Status.REJECTED);
                appointment.setUpdatedAt(LocalDateTime.now());
                break;
            default:
                throw new CustomCommonException("Unsupported action for the appointment");
        }
        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsByProviderId(Integer providerId) {
        List<Appointment> providerAppointments = appointmentRepository.findAllByProviderId(providerId).orElseThrow(() ->
                new ResourceNotFoundException("Appointment", "providerId", providerId));
        return providerAppointments;
    }

    @Override
    public List<Appointment> getAppointmentsByUserId(Integer userId) {
        List<Appointment> userAppointments = appointmentRepository.findAllByUserId(userId).orElseThrow(() ->
                new ResourceNotFoundException("Appointment", "userId", userId));
        return userAppointments;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public ApiResponse cancelAppointment(Integer appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus(Status.CANCELLED);
        appointment.setUpdatedAt(LocalDateTime.now());
        appointmentRepository.save(appointment);
        return new ApiResponse("Appointment cancelled successfully",true);
    }

    @Override
    public List<Appointment> getAppointmentsByStatus(String status) {
        return appointmentRepository.findByStatus(Status.valueOf(status)).orElseThrow(() ->
                new ResourceNotFoundException("Appointment", "status", status));
    }
}
