package org.esourcer.orders.common;

import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.util.Optional.ofNullable;

@Value
public final class Money {

    private BigDecimal amount;
    private Currency currency;

    public static Money empty() {
        return new Money(null, null);
    }

    public Money add(final Money value) {
        if (currency != null && value.currency != currency) {
            throw new IllegalArgumentException("Cannot add different currency");
        }
        if (amount == null) {
            return value;
        }
        return new Money(amount.add(value.getAmount()), currency);
    }

    public Money multiply(final BigDecimal value) {
        if (amount == null) {
            throw new IllegalArgumentException("Cannot multiply on empty money");
        }
        return new Money(amount.multiply(value), currency);
    }

    public boolean greaterThanZero() {
        return ofNullable(amount).filter(bigDecimal -> bigDecimal.compareTo(BigDecimal.ZERO) > 0).isPresent();
    }

    private BigDecimal roundForCurrency(final BigDecimal amount, final Currency currency) {
        return amount.setScale(ofNullable(currency).map(Currency::getScale).orElse(2), RoundingMode.HALF_UP);
    }
}
