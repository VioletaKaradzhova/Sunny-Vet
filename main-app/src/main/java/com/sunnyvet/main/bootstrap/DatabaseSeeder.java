package com.sunnyvet.main.bootstrap;

import com.sunnyvet.main.domain.entity.Appointment;
import com.sunnyvet.main.domain.entity.Doctor;
import com.sunnyvet.main.domain.entity.Owner;
import com.sunnyvet.main.domain.entity.Pet;
import com.sunnyvet.main.domain.entity.UserEntity;
import com.sunnyvet.main.domain.enums.Role;
import com.sunnyvet.main.repository.AppointmentRepository;
import com.sunnyvet.main.repository.DoctorRepository;
import com.sunnyvet.main.repository.OwnerRepository;
import com.sunnyvet.main.repository.PetRepository;
import com.sunnyvet.main.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final DoctorRepository doctorRepository;
    private final PetRepository petRepository;
    private final AppointmentRepository appointmentRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(UserRepository userRepository,
                          OwnerRepository ownerRepository,
                          DoctorRepository doctorRepository,
                          PetRepository petRepository,
                          AppointmentRepository appointmentRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.ownerRepository = ownerRepository;
        this.doctorRepository = doctorRepository;
        this.petRepository = petRepository;
        this.appointmentRepository = appointmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {

            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setEmail("admin@sunnyvet.com");
            admin.setFullName("System Administrator");
            admin.setPhoneNumber("555-000-1111");
            userRepository.save(admin);

            UserEntity admin1 = new UserEntity();
            admin1.setUsername("admin1");
            admin1.setPassword(passwordEncoder.encode("admin123"));
            admin1.setRole(Role.ADMIN);
            admin1.setEmail("admin1@sunnyvet.com");
            admin1.setFullName("System Administrator");
            admin1.setPhoneNumber("555-000-1111");
            userRepository.save(admin1);

            UserEntity doctorUser = new UserEntity();
            doctorUser.setUsername("doctor1");
            doctorUser.setPassword(passwordEncoder.encode("password123"));
            doctorUser.setRole(Role.DOCTOR);
            doctorUser.setEmail("doctor1@sunnyvet.com");
            doctorUser.setFullName("Dr. Sarah Jenkins");
            doctorUser.setPhoneNumber("555-000-2222");
            userRepository.save(doctorUser);

            Doctor doctor = new Doctor();
            doctor.setFullName("Dr. Sarah Jenkins");
            doctor.setSpecialization("Surgery");
            doctor.setUser(doctorUser);
            doctorRepository.save(doctor);

            UserEntity ownerUser = new UserEntity();
            ownerUser.setUsername("owner1");
            ownerUser.setPassword(passwordEncoder.encode("password123"));
            ownerUser.setRole(Role.USER);
            ownerUser.setEmail("owner1@email.com");
            ownerUser.setFullName("Mark Johnson");
            ownerUser.setPhoneNumber("555-0198");
            userRepository.save(ownerUser);

            Owner owner = new Owner();
            owner.setFullName("Mark Johnson");
            owner.setPhoneNumber("555-0198");
            owner.setUser(ownerUser);
            ownerRepository.save(owner);

            Pet pet = new Pet();
            pet.setName("Buddy");
            pet.setSpecies("Dog");
            pet.setAge(3);
            pet.setOwner(owner);
            petRepository.save(pet);

            Appointment appointment = new Appointment();
            appointment.setAppointmentTime(LocalDateTime.now().plusDays(2));
            appointment.setReason("Routine Checkup");
            appointment.setDoctor(doctor);
            appointment.setPet(pet);
            appointmentRepository.save(appointment);

            UserEntity user = new UserEntity();
            user.setUsername("client");
            user.setPassword(passwordEncoder.encode("client123"));
            user.setRole(Role.USER);
            user.setEmail("client@email.com");
            user.setFullName("John Doe");
            user.setPhoneNumber("555-123-4567");
            userRepository.save(user);

            System.out.println("Test data seeded successfully.");
        }
    }
}