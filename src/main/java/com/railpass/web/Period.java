package com.railpass.web;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public enum Period {
    Monthly,
    Quaterly,
    Semiyearly,
    Yearly
}
