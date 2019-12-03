package org.esourcer.jpa.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQuery(
        name = "JpaEvent.findEventsByGroupAndEntityId",
        query = "select e from JpaEvent e "
                + "where e.eventGroup = :eventGroup "
                + "and e.entityId = :entityId "
                + "order by e.eventGroupOrdinal asc ")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "journal")
public class JpaEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "event_group", nullable = false)
    private String eventGroup;
    @Column(name = "entity_id", nullable = false)
    private String entityId;
    @Column(name = "event_group_ordinal", nullable = false)
    private Long eventGroupOrdinal;
    @Column(name = "event_class_name", nullable = false)
    private String eventClassName;
    @Column(name = "event_body", nullable = false)
    private String eventBody;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
