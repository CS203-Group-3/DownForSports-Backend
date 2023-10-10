package com.example.cs203g1t3.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.cs203g1t3.repository.RoleRepository;
import com.example.cs203g1t3.repository.UserRepository;
import java.util.*;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CommandLineAppStartupRunner(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String...args) {
        Role adminRole = new Role(ERole.ROLE_ADMIN);
        roleRepository.save(adminRole);

        String encodedPassword = passwordEncoder.encode("Pass1234!");
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);

        User admin = new User("admin", null, encodedPassword);
        admin.setRoles(roles);
        userRepository.save(admin);
    }
}
