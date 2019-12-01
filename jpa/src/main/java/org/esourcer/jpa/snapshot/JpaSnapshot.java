package org.esourcer.jpa.snapshot;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@NamedQuery(
        name = "JpaEvent.findByEventGroupAndEntityId",
        query = "select e from JpaSnapshot e "
                + "where e.eventGroup = :eventGroup "
                + "and e.entityId = :entityId ")
@AllArgsConstructor
@Data
public class JpaSnapshot {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "event_group", nullable = false)
    private String eventGroup;
    @Column(name = "entity_id", nullable = false)
    private String entityId;
    @Column(name = "snapshot_class_name", nullable = false)
    private String snapshotClassName;
    @Column(name = "snapshot", nullable = false)
    private String snapshot;
    @Column(name = "last_applied_event", nullable = false)
    private Long lastAppliedEvent;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
