package co.edu.unicauca.monitoring.tool.backend.users.ms.config;

import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.To;
import co.edu.unicauca.monitoring.tool.backend.users.ms.domain.WelcomePasswordNotificationDto;
import co.edu.unicauca.monitoring.tool.backend.users.ms.model.Role;
import co.edu.unicauca.monitoring.tool.backend.users.ms.model.User;
import co.edu.unicauca.monitoring.tool.backend.users.ms.publisher.IWelcomePasswordPublisher;
import co.edu.unicauca.monitoring.tool.backend.users.ms.repository.IRoleRepository;
import co.edu.unicauca.monitoring.tool.backend.users.ms.repository.IUserRepository;
import co.edu.unicauca.monitoring.tool.backend.users.ms.util.PasswordGeneratorUtil;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final IWelcomePasswordPublisher welcomePasswordPublisher;
    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.name}")
    private String adminName;
    @Value("${admin.lastname}")
    private String adminLastName;
    @Value("${admin.document}")
    private String adminDocument;
    @Value("${admin.phone}")
    private Long adminPhone;

    private static final String ADMIN_ROLE_NAME = "ADMIN";

    @PostConstruct
    @Transactional
    public void initAdminUser() {
        Optional<User> existingAdmin = userRepository.findByEmailIgnoreCase(adminEmail);

        if (existingAdmin.isPresent()) {
            log.info("Admin user already exists: {}", adminEmail);
            return;
        }

        Role adminRole = roleRepository.findByNameIgnoreCase(ADMIN_ROLE_NAME)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(ADMIN_ROLE_NAME);
                    role.setDescription("Administrator role");
                    return role;
                });

        String rawPassword = PasswordGeneratorUtil.generatePassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User adminUser = new User();
        adminUser.setName(adminName);
        adminUser.setLastName(adminLastName);
        adminUser.setDocumentNumber(adminDocument);
        adminUser.setPhoneNumber(adminPhone);
        adminUser.setEmail(adminEmail);
        adminUser.setPassword(encodedPassword);
        adminUser.setRoles(List.of(adminRole));
        var userSaved = userRepository.save(adminUser);
        this.welcomePasswordPublisher.publish(WelcomePasswordNotificationDto.builder().generatedPassword(rawPassword)
                .recipient(To.builder().name(userSaved.getName()).email(userSaved.getEmail()).build()).build());
    }
}