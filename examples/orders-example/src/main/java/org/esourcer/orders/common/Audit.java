package org.esourcer.orders.common;

import lombok.Value;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Value
public final class Audit {

    String userId;
    Instant initiatedAt;

    public static Audit test() {
        return new Audit("TEST", LocalDate.of(2019, 11, 16).atStartOfDay(ZoneOffset.UTC).toInstant());
    }
}
