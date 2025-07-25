package com.academicaffairs.odtrack.dataloader;

import com.academicaffairs.odtrack.entity.ODRequest;
import com.academicaffairs.odtrack.entity.Timetable;
import com.academicaffairs.odtrack.entity.User;
import com.academicaffairs.odtrack.repository.ODRequestRepository;
import com.academicaffairs.odtrack.repository.TimetableRepository;
import com.academicaffairs.odtrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ODRequestRepository odRequestRepository;
    private final TimetableRepository timetableRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create Admin User
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = User.builder()
                    .role(User.UserRole.ADMIN)
                    .email("admin@example.com")
                    .passwordHash(passwordEncoder.encode("adminpass"))
                    .fullName("Admin User")
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userRepository.save(admin);
            System.out.println("Created Admin User: " + admin.getEmail());
        }

        // Create Staff User
        if (userRepository.findByEmail("staff@example.com").isEmpty()) {
            User staff = User.builder()
                    .role(User.UserRole.STAFF)
                    .email("staff@example.com")
                    .passwordHash(passwordEncoder.encode("staffpass"))
                    .fullName("Staff Member")
                    .subject("Computer Science")
                    .yearHandled("3rd Year")
                    .phone("1234567890")
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userRepository.save(staff);
            System.out.println("Created Staff User: " + staff.getEmail());
        }

        // Create Student User
        if (userRepository.findByRegisterNumber("STU12345").isEmpty()) {
            User student = User.builder()
                    .role(User.UserRole.STUDENT)
                    .registerNumber("STU12345")
                    .dateOfBirth(LocalDate.of(2000, 5, 15))
                    .fullName("Student One")
                    .phone("0987654321")
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userRepository.save(student);
            System.out.println("Created Student User: " + student.getRegisterNumber());
        }

        // Create Sample OD Request (assuming student and staff exist)
        User student = userRepository.findByRegisterNumber("STU12345").orElse(null);
        User staff = userRepository.findByEmail("staff@example.com").orElse(null);

        if (student != null && staff != null && odRequestRepository.count() == 0) {
            ODRequest odRequest = ODRequest.builder()
                    .student(student)
                    .date(LocalDate.now())
                    .periods(List.of(1, 2))
                    .reason("Attending workshop")
                    .status(ODRequest.ODStatus.PENDING)
                    .staff(staff)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            odRequestRepository.save(odRequest);
            System.out.println("Created Sample OD Request for: " + student.getFullName());
        }

        // Create Sample Timetable Entry (assuming staff exists)
        if (staff != null && timetableRepository.count() == 0) {
            Timetable timetable = Timetable.builder()
                    .staff(staff)
                    .date(LocalDate.now())
                    .period(1)
                    .year(3)
                    .hall("A101")
                    .subject("Data Structures")
                    .dayOfWeek("Monday")
                    .startTime("09:00")
                    .endTime("10:00")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            timetableRepository.save(timetable);
            System.out.println("Created Sample Timetable Entry for: " + staff.getFullName());
        }
    }
}
