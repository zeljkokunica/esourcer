package org.esourcer.core.entity;

import lombok.Value;

import java.util.Optional;

@Value
public class CurrentEntityState<Entity> {
    final Optional<Long> lastEventId;
    final Optional<Entity> entity;

    public CurrentEntityState<Entity> applyEntityUpdate(final Optional<Entity> entity) {
        return new CurrentEntityState<>(Optional.of(lastEventId.orElse(0L) + 1), entity);
    }
}
