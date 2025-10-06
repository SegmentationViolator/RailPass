package com.railpass.web.application;

import com.railpass.web.Period;
import com.railpass.web.Station;
import com.railpass.web.Tier;

import jakarta.validation.constraints.NotNull;

public class CreationRequest {
    @NotNull
    public Station start;

    @NotNull
    public Station end;

    @NotNull
    public Tier tier;

    @NotNull
    public Period period;
}
