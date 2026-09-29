package com.mustafizur.hibernateadvanced.infrastructure.validation;

import com.mustafizur.hibernateadvanced.application.validation.Box;
import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.ValueExtractor;

public final class BoxValueExtractor implements ValueExtractor<Box<@ExtractedValue ?>> {
    @Override
    public void extractValues(Box<?> originalValue, ValueReceiver receiver) {
        if (originalValue != null) receiver.value(null, originalValue.value());
    }
}
