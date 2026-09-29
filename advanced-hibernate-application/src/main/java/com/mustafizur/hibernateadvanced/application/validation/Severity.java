package com.mustafizur.hibernateadvanced.application.validation;

import jakarta.validation.Payload;

public final class Severity {
    private Severity() { }
    public static final class Info implements Payload { private Info() {} }
    public static final class Error implements Payload { private Error() {} }
}
