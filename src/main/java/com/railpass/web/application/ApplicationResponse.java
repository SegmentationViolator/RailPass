package com.railpass.web.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.railpass.web.Period;
import com.railpass.web.Station;
import com.railpass.web.Status;
import com.railpass.web.Tier;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationResponse {
    public Long applicationId;
    public Long userId;
    public Station start;
    public Station end;
    public Tier tier;
    public Period period;
    public Status status;
    public String error;

    private ApplicationResponse(Long applicationId, Long userId, Station start, Station end, Tier tier, Period period, Status status, String error) {
        this.applicationId = applicationId;
        this.userId = userId;
        this.start = start;
        this.end = end;
        this.period = period;
        this.status = status;
        this.error = error;
    }

    public static ApplicationResponse success(Long applicationId, Long userId, Station start, Station end, Tier tier, Period period, Status status) {
        return new ApplicationResponse(
            applicationId,
            userId,
            start,
            end,
            tier,
            period,
            status,
            null
        );
    }

    public static ApplicationResponse failure(String error) {
        return new ApplicationResponse(null, null, null, null, null, null, null, error);
    }
}
