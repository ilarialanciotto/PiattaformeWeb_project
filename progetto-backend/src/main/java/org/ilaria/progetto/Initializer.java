package org.ilaria.progetto;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.ilaria.progetto.Model.Entity.Classroom;
import org.ilaria.progetto.Model.Entity.Code;
import org.ilaria.progetto.Model.Entity.Content;
import org.ilaria.progetto.Model.Entity.User;
import org.ilaria.progetto.Repository.ClassroomRepository;
import org.ilaria.progetto.Repository.CodeRepository;
import org.ilaria.progetto.Repository.ContentRepository;
import org.ilaria.progetto.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class Initializer {

    private final ClassroomRepository classroomRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CodeRepository codeRepository;

    @PostConstruct
    public void initData() {
        if (userRepository.count() == 0) {
            String password = passwordEncoder.encode("password123");
            User admin = new User(null, "admin", "admin@gmail.com", password , null , -1 , Role.ADMIN );
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            if (classroomRepository.count() == 0) {
                loadClassroom(admin);
            }
            if (contentRepository.count() == 0) {
                loadContents();
            }
        }
        if(codeRepository.count() == 0) {
            loadCodes();
        }
    }

    private void loadCodes() {
        List<Code> codes = List.of(
                new Code(null,"123"),
                new Code(null,"234"),
                new Code(null,"345"),
                new Code(null,"456"),
                new Code(null,"567"),
                new Code(null,"678"),
                new Code(null,"789"),
                new Code(null,"890"),
                new Code(null,"901"),
                new Code(null,"012")
        );

        codeRepository.saveAll(codes);
    }

    private void loadClassroom(User admin) {
        List<Classroom> classrooms = List.of(
                new Classroom(null, "0A", 1, 1, 1, false, 0, admin),
                new Classroom(null, "1B", 80, 80, 2, true, 0, admin),
                new Classroom(null, "30A", 1, 1, 1, true, 0, admin),
                new Classroom(null, "42B2", 100, 100, 0, false, 0, admin),
                new Classroom(null, "2C", 100, 100,6, false, 0, admin),
                new Classroom(null, "3D", 80, 80,7, true, 0, admin),
                new Classroom(null, "11B", 80, 80,4, true, 0, admin),
                new Classroom(null, "12C", 100, 100, 3, false, 0, admin),
                new Classroom(null, "20A", 150, 150,1, false, 0, admin),
                new Classroom(null, "21B", 100, 100,0, false, 0, admin),
                new Classroom(null, "22C", 80,80, 3, true, 0, admin),
                new Classroom(null, "23D", 100, 100, 4, false, 0,admin),
                new Classroom(null, "31B2", 100, 100, 2, false, 0, admin),
                new Classroom(null, "33D", 130, 130,6, false, 0, admin),
                new Classroom(null, "40Z", 150, 150,5, false, 0, admin),
                new Classroom(null, "41B0A", 120, 120,  7, false, 0, admin),
                new Classroom(null, "43C", 150, 150,  1, false, 0, admin),
                new Classroom(null, "10A", 100, 100,5, false, 0, admin),
                new Classroom(null, "13D", 130, 130, 2, false, 0, admin),
                new Classroom(null, "32C", 50, 50, 0, true, 0, admin)
        );

        classroomRepository.saveAll(classrooms);
    }

    private void loadContents() {
        Map<String, List<String>> contentsForClassroom = Map.ofEntries(
                Map.entry("0A", List.of("microphone", "projector", "power outlets")),
                Map.entry("1B", List.of("pc", "projector")),
                Map.entry("2C", List.of("projector", "microphone")),
                Map.entry("3D", List.of("pc", "projector")),
                Map.entry("11B", List.of("pc", "projector")),
                Map.entry("12C", List.of("projector", "microphone")),
                Map.entry("13D", List.of("projector", "microphone", "power outlets")),
                Map.entry("10A", List.of("projector", "power outlets")),
                Map.entry("20A", List.of("projector", "power outlets", "microphone")),
                Map.entry("21B", List.of("microphone", "projector")),
                Map.entry("22C", List.of("microphone", "projector")),
                Map.entry("23D", List.of("projector", "microphone")),
                Map.entry("30A", List.of("pc", "projector")),
                Map.entry("31B2", List.of("projector", "power outlets")),
                Map.entry("32C", List.of("pc", "projector")),
                Map.entry("33D", List.of("projector", "microphone")),
                Map.entry("40Z", List.of("projector", "microphone", "power outlets")),
                Map.entry("41B0A", List.of("projector", "power outlets", "microphone")),
                Map.entry("42B2", List.of("microphone", "projector")),
                Map.entry("43C", List.of("microphone", "projector", "power outlets"))
        );

        List<Content> contents = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : contentsForClassroom.entrySet()) {
            String cube = entry.getKey();
            List<String> items = entry.getValue();
            classroomRepository.findByCube(cube).ifPresent(classroom -> {
                for (String item : items) {
                    contents.add(new Content(null, item, classroom));
                }
            });
        }

        contentRepository.saveAll(contents);
    }
}
