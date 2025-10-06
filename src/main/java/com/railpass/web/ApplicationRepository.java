package com.railpass.web;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ApplicationRepository extends CrudRepository<Application, Long> {
    boolean existsByStudentAndStatus(User student, Status status);
    Optional<Application> findByIdAndStudent(Long id, User student);
    List<Application> findByStatus(Status status);
}
