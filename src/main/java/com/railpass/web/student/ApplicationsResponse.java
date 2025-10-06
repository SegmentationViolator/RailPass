package com.railpass.web.student;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.railpass.web.application.ApplicationResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationsResponse {
    public List<ApplicationResponse> applications;
    public String error;

    private ApplicationsResponse(List<ApplicationResponse> applications, String error) {
        this.applications = applications;
        this.error = error;
    }

    public static ApplicationsResponse success(List<ApplicationResponse> applications) {
        return new ApplicationsResponse(applications, null);
    }

    public static ApplicationsResponse failure(String error) {
        return new ApplicationsResponse(null, error);
    }
}
