package com.mustafizur.hibernateadvanced.application.order;

import com.mustafizur.hibernateadvanced.application.validation.ConsistentOrderRequest;
import com.mustafizur.hibernateadvanced.application.validation.Severity;
import com.mustafizur.hibernateadvanced.application.validation.ValidSku;
import com.mustafizur.hibernateadvanced.application.validation.ValidationGroups;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@ConsistentOrderRequest(groups = ValidationGroups.Business.class)
public record CreateOrderCommand(
    @NotNull(groups = ValidationGroups.Basic.class) UUID customerId,
    @NotEmpty(groups = ValidationGroups.Basic.class) List<@Valid Line> lines,
    @Size(max = 20, payload = Severity.Info.class) List<@NotBlank String> tags
) {
    public record Line(
        @NotNull UUID productId,
        @NotBlank @ValidSku String sku,
        @NotBlank @Size(max = 200) String productName,
        @Positive int quantity,
        @NotNull @DecimalMin(value = "0.01") BigDecimal unitPrice,
        @NotNull Currency currency,
        List<@Email String> notificationEmails
    ) { }
}
