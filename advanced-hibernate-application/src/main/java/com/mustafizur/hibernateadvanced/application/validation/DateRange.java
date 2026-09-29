package com.mustafizur.hibernateadvanced.application.validation;

import java.time.LocalDate;

public interface DateRange {
    LocalDate start();
    LocalDate end();
}
