package com.railpass.web.student;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.railpass.web.User;
import com.railpass.web.UserRepository;
import com.railpass.web.application.ApplicationResponse;
import com.railpass.web.authentication.TokenGenerator;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/applications")
    public ApplicationsResponse getApplications(@RequestHeader("Authorization") String auth) {
        if (!auth.startsWith("Bearer ")) ApplicationResponse.failure("authorization header has incorrect scheme");

        Optional<String> optionalSubject = TokenGenerator.validateToken(auth.substring(7));

        if (optionalSubject.isEmpty()) return ApplicationsResponse.failure("token has expired, or is invalid");

        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(Long.parseLong(optionalSubject.get()));
        } catch (NumberFormatException e) {
            return ApplicationsResponse.failure("token contains invalid subject");
        }

        if (optionalUser.isEmpty()) return ApplicationsResponse.failure("user not found");

        User user = optionalUser.get();

        if (user.isAdmin()) return ApplicationsResponse.failure("user is not a student");

        List<ApplicationResponse> applications = user.getApplications().stream().map(application -> ApplicationResponse.success(
            application.getId(),
            user.getId(),
            application.getStart(),
            application.getEnd(),
            application.getTier(),
            application.getPeriod(),
            application.getStatus()
        )).collect(Collectors.toList());

        return ApplicationsResponse.success(applications);
    }
}
