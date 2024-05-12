package homiessecurity.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import homiessecurity.dtos.Appointments.AppointmentDto;
import homiessecurity.dtos.Appointments.AppointmentRequestDto;
import homiessecurity.dtos.Users.UserDto;
import homiessecurity.dtos.khalti.CustomerInfo;
import homiessecurity.dtos.khalti.KhaltiInitiationRequest;
import homiessecurity.dtos.khalti.KhaltiResponseDTO;
import homiessecurity.entities.*;
import homiessecurity.exceptions.CustomCommonException;
import homiessecurity.exceptions.ResourceNotFoundException;
import homiessecurity.payload.ApiResponse;
import homiessecurity.repository.AppointmentRepository;
import homiessecurity.service.*;
import homiessecurity.utils.KhaltiPayment;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
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

    private final KhaltiPayment khaltiPayment;

    @Autowired
    public AppointmentServiceImpl(ProviderService providerService, UserService userService,
                                  AppointmentRepository appointmentRepository, ModelMapper modelMapper,
                                  CloudinaryService cloudinaryService,ServicesService servicesService,
                                  EmailSenderService emailSenderService, KhaltiPayment khaltiPayment){
        this.providerService=providerService;
        this.userService=userService;
        this.appointmentRepository=appointmentRepository;
        this.modelMapper=modelMapper;
        this.cloudinaryService=cloudinaryService;
        this.emailSenderService=emailSenderService;
        this.servicesService = servicesService;
        this.khaltiPayment = khaltiPayment;
    }
//    @Override
//    public Appointment addAppointment(AppointmentRequestDto appointment, Integer userId, Integer providerId
//                                        ,Integer serviceId) {
//
//        String imageUrl = null;
//        ServiceProvider provider = providerService.getProviderById(providerId);
//        User user = userService.getRawUserById(userId);
//        Services service = servicesService.getServiceById(serviceId);
//
//        if (appointment.getAppointmentImageUrl() != null) {
//            imageUrl = appointment.getAppointmentImageUrl();
//        }
//
//        Appointment new_appointment = Appointment.builder()
//                .description(appointment.getDescription())
//                .arrivalTime(appointment.getArrivalTime())
//                .appointmentDate(appointment.getArrivalDate())
//                .detailedLocation(appointment.getDetailedLocation())
//                .provider(provider)
//                .appointmentImage(imageUrl)
//                .user(user)
//                .service(service)
//                .status(Status.PENDING)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//
//        emailSenderService.sendNewRequestEmail(new_appointment);
//        return appointmentRepository.save(new_appointment);
//    }

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


    public Appointment respondAppointment(Integer appointmentId, String action) {
        Appointment appointment = getAppointmentById(appointmentId);

        Status status;
        switch (action.toUpperCase()) {
            case "ACCEPTED":
                status = Status.ACCEPTED;
                appointment.setUpdatedAt(LocalDateTime.now());
                break;
            case "CANCELLED":
                status = Status.CANCELLED;
                appointment.setUpdatedAt(LocalDateTime.now());
                break;
            case "COMPLETED":
                status = Status.COMPLETED;
                appointment.setUpdatedAt(LocalDateTime.now());
                break;
            default:
                throw new CustomCommonException("Unsupported action for the appointment");
        }
        appointment.setStatus(status);
        Appointment updatedAppointment = appointmentRepository.save(appointment);

        ServiceProvider provider = appointment.getProvider();

        if (provider != null) {
            emailSenderService.sendAppointmentStatusEmail(appointment.getUser().getEmail(),
                    appointment.getUser().getName(),
                    provider.getProviderName(),
                    status,
                    appointment.getAppointmentDate(),
                    appointment.getArrivalTime());
        }

        return updatedAppointment;
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
    public List<Appointment> getIncomingAppointmentsByProviderId(Integer providerId) {
        return appointmentRepository.findByProviderIdAndStatus(providerId, Status.PENDING).orElseThrow(() ->
                new ResourceNotFoundException("Appointment", "status", "ACCEPTED"));
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }


    @Override
    public ApiResponse cancelAppointment(Integer appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);

        // Check if the appointment can be cancelled based on the time difference
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cancellationDeadline = appointment.getCreatedAt().plusMinutes(30);
        if (now.isAfter(cancellationDeadline)) {
            // If the current time is after the cancellation deadline, return an error response
            return new ApiResponse("Cannot cancel appointment after 30 minutes ", false);
        }

        // If the appointment can be cancelled, proceed with the cancellation
        appointment.setStatus(Status.CANCELLED);
        appointment.setUpdatedAt(now);
        appointmentRepository.save(appointment);
        return new ApiResponse("Appointment cancelled successfully", true);
    }


    @Override
    public List<Appointment> getAppointmentsByStatus(String status) {
        return appointmentRepository.findByStatus(Status.valueOf(status.toUpperCase())).orElseThrow(() ->
                new ResourceNotFoundException("Appointment", "status", status));
    }

    @Transactional
    @Scheduled(cron = "00 00 03 * * *")
    public void sendAppointmentReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Appointment> appointments = appointmentRepository.findByAppointmentDateAndStatus(tomorrow,
                Status.ACCEPTED);

        for (Appointment appointment : appointments) {
            try {
                emailSenderService.sendReminderEmail( appointment);
            } catch (Exception e) {
                throw new CustomCommonException(e.getMessage());
            }
        }
    }

    @Override
    public KhaltiResponseDTO addAppointment(AppointmentRequestDto appointment, Integer userId, Integer providerId
            , Integer serviceId) {

        try{
            String imageUrl = null;
            ServiceProvider provider = providerService.getProviderById(providerId);
            User user = userService.getRawUserById(userId);
            Services service = servicesService.getServiceById(serviceId);

            if (appointment.getAppointmentImageUrl() != null) {
                imageUrl = appointment.getAppointmentImageUrl();
            }

            Appointment new_appointment = Appointment.builder()
                    .description(appointment.getDescription())
                    .arrivalTime(appointment.getArrivalTime())
                    .appointmentDate(appointment.getArrivalDate())
                    .detailedLocation(appointment.getDetailedLocation())
                    .provider(provider)
                    .appointmentImage(imageUrl)
                    .user(user)
                    .service(service)
                    .status(Status.PENDING)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            emailSenderService.sendNewRequestEmail(new_appointment);
            Appointment appointment1 = appointmentRepository.save(new_appointment);

            KhaltiInitiationRequest khaltiInitiationRequest = new KhaltiInitiationRequest();

            String return_url = "http://localhost:5173/appointments";
            String website_url = "http://localhost:5173/";

            khaltiInitiationRequest.setReturn_url(return_url);
            khaltiInitiationRequest.setWebsite_url(website_url);
            double khaltiTotalAmount = appointment1.getService().getPerHourRate()* 100;
            khaltiInitiationRequest.setAmount(khaltiTotalAmount);
            khaltiInitiationRequest.setPurchase_order_id(appointment1.getAppointmentId().toString());
            khaltiInitiationRequest.setPurchase_order_name(appointment1.getService().getServiceName());

            System.out.println("khalti set amount " + khaltiInitiationRequest.getAmount());




            CustomerInfo customerInfo = new CustomerInfo(user.getUsername(), user.getUsername(), user.getUsername(), user.getEmail(), user.getPhoneNumber(), user.getAddress());

            khaltiInitiationRequest.setCustomerInfo(customerInfo);

            KhaltiResponseDTO res =  this.khaltiPayment.callKhalti(khaltiInitiationRequest);
            System.out.println(res.getPidx());
            System.out.println(res.getPayment_url());
            System.out.println(res.getExpires_at());

            return res;

        } catch (JsonProcessingException e) {
            System.out.println("Error processing JSON");
        throw new RuntimeException("Error processing JSON", e);
    } catch (HttpClientErrorException e) {
            System.out.println("Error communicating with Khalti API");
        throw new RuntimeException("Error communicating with Khalti API", e);
    } catch (Exception e) {
            System.out.println("Unexpected error");
        throw new RuntimeException("Unexpected error", e);
    }

    }


   public String updatePaymentTable(String pidx, Integer apointmentId){
        Appointment appointment = getAppointmentById(apointmentId);
        Payment payment = Payment.builder()
                .amount(appointment.getService().getPerHourRate())
                .transactionId(pidx)
                .paymentMethod("Khalti")
                .createdAt(LocalDateTime.now())
                .user(appointment.getUser())
                .appointment(appointment)
                .token("SCkjdflG7oidfn")
                .mobile("mobile")
                .productIdentity("productIdentity")
                .productName(appointment.getService().getServiceName())
                .build();
        return "Payment table updated";

    }








}
