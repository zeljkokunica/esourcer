package org.esourcer.orders.common.web;

import org.esourcer.orders.common.Audit;
import org.esourcer.orders.common.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;

public class ControllerBase {

    protected Audit getAudit() {
        final User user = getUser();
        return new Audit(user.getId(), Instant.now());
    }

    protected User getUser() {
        final UsernamePasswordAuthenticationToken authenticationToken = ((UsernamePasswordAuthenticationToken) SecurityContextHolder
                .getContext().getAuthentication());
        return (User) authenticationToken.getDetails();
    }
}
