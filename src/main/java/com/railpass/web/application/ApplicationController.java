package com.railpass.web.application;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.railpass.web.Application;
import com.railpass.web.ApplicationRepository;
import com.railpass.web.Status;
import com.railpass.web.User;
import com.railpass.web.UserRepository;
import com.railpass.web.authentication.TokenGenerator;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/application")
public class ApplicationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @PostMapping("/modify")
    public ApplicationResponse modifyApplication(
        @RequestHeader("Authorization") String auth,
        @Valid @RequestBody ModificationRequest request
    ) {
        if (request.status == Status.Pending) return ApplicationResponse.failure("status cannot be changed to pending");

        if (!auth.startsWith("Bearer ")) ApplicationResponse.failure("authorization header has incorrect scheme");

        Optional<String> optionalSubject = TokenGenerator.validateToken(auth.substring(7));

        if (optionalSubject.isEmpty()) return ApplicationResponse.failure("token has expired, or is invalid");

        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(Long.parseLong(optionalSubject.get()));
        } catch (NumberFormatException e) {
            return ApplicationResponse.failure("token contains invalid subject");
        }

        if (optionalUser.isEmpty()) return ApplicationResponse.failure("user not found");

        User admin = optionalUser.get();

        if (!admin.isAdmin()) return ApplicationResponse.failure("user is not an admin");

        Optional<Application> optionalApplication = applicationRepository.findById(request.applicationId);

        if (optionalApplication.isEmpty()) return ApplicationResponse.failure("application not found");

        Application application = optionalApplication.get();

        if (application.getStatus() != Status.Pending) ApplicationResponse.failure("status cannot be changed now");

        application.setStatus(request.status);
        applicationRepository.save(application);

        return ApplicationResponse.success(
            application.getId(),
            null,
            application.getStart(),
            application.getEnd(),
            application.getTier(),
            application.getPeriod(),
            application.getStatus()
        );
    }

    @PostMapping("/create")
    public ApplicationResponse createApplication(
        @RequestHeader("Authorization") String auth,
        @Valid @RequestBody CreationRequest request
    ) {
        if (!auth.startsWith("Bearer ")) ApplicationResponse.failure("authorization header has incorrect scheme");

        Optional<String> optionalSubject = TokenGenerator.validateToken(auth.substring(7));

        if (optionalSubject.isEmpty()) return ApplicationResponse.failure("token has expired, or is invalid");

        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(Long.parseLong(optionalSubject.get()));
        } catch (NumberFormatException e) {
            return ApplicationResponse.failure("token contains invalid subject");
        }

        if (optionalUser.isEmpty()) return ApplicationResponse.failure("user not found");

        User student = optionalUser.get();

        if (student.isAdmin()) return ApplicationResponse.failure("user is not a student");

        if (applicationRepository.existsByStudentAndStatus(student, Status.Pending)) return ApplicationResponse.failure("user has pending applications");

        Application application = applicationRepository.save(new Application(student, request.start, request.end, request.tier, request.period, Status.Pending));

        return ApplicationResponse.success(
            application.getId(),
            student.getId(),
            application.getStart(),
            application.getEnd(),
            application.getTier(),
            application.getPeriod(),
            application.getStatus()
        );
    }

    @GetMapping("/get")
    public ApplicationResponse getApplication(
        @RequestHeader("Authorization") String auth,
        @RequestParam Long applicationId
    ) {
        if (!auth.startsWith("Bearer ")) ApplicationResponse.failure("authorization header has incorrect scheme");

        Optional<String> optionalSubject = TokenGenerator.validateToken(auth.substring(7));

        if (optionalSubject.isEmpty()) return ApplicationResponse.failure("token has expired, or is invalid");

        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(Long.parseLong(optionalSubject.get()));
        } catch (NumberFormatException e) {
            return ApplicationResponse.failure("token contains invalid subject");
        }

        if (optionalUser.isEmpty()) return ApplicationResponse.failure("user not found");

        User requestor = optionalUser.get();

        Optional<Application> optionalApplication;
        if (requestor.isAdmin()) optionalApplication = applicationRepository.findById(applicationId);
        else optionalApplication = applicationRepository.findByIdAndStudent(applicationId, requestor);

        if (optionalApplication.isEmpty()) return ApplicationResponse.failure("application not found");

        Application application = optionalApplication.get();

        return ApplicationResponse.success(
            application.getId(),
            requestor.isAdmin() ? null : requestor.getId(),
            application.getStart(),
            application.getEnd(),
            application.getTier(),
            application.getPeriod(),
            application.getStatus()
        );
    }
}
