package com.mustafizur.hibernateadvanced.application.validation;

import jakarta.validation.GroupSequence;

public final class ValidationGroups {
    private ValidationGroups() { }
    public interface Basic { }
    public interface Business { }
    public interface ExpensiveChecks { }

    @GroupSequence({Basic.class, Business.class, ExpensiveChecks.class})
    public interface OrderedChecks { }
}
