package org.esourcer.orders.common;

import lombok.Value;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Value
public class User {


    public static final String CUSTOMER_PERMISSION = "customer";
    public static final String ADMIN_PERMISSION = "admin";

    String id;
    String permissions;

    public boolean isCustomer() {
        return hasPermission(CUSTOMER_PERMISSION);
    }

    public boolean isAdmin() {
        return hasPermission(ADMIN_PERMISSION);
    }

    public boolean hasPermission(final String permission) {
        return permissions().contains(permission);
    }

    public Set<String> permissions() {
        return Optional.ofNullable(permissions)
                .map(s -> s.split(","))
                .map(Arrays::asList)
                .map(HashSet::new)
                .orElse(new HashSet<>());

    }
}
