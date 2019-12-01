package org.esourcer.core.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public final class EntityMangerOptions {
    @Builder.Default
    final Long snapshotBatch = 100L;

}
