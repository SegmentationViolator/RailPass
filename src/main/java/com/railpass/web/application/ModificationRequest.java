package com.railpass.web.application;

import com.railpass.web.Status;

import jakarta.validation.constraints.NotNull;

public class ModificationRequest {
    @NotNull
    public Long applicationId;

    @NotNull
    public Status status;
}
