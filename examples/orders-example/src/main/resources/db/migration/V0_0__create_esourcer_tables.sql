CREATE TABLE journal
(
    id                  SERIAL,
    event_group         TEXT      NOT NULL,
    entity_id           TEXT      NOT NULL,
    event_group_ordinal INT       NOT NULL,
    event_class_name    TEXT      NOT NULL,
    event_body          TEXT      NOT NULL,
    created_at          TIMESTAMP NOT NULL,
    CONSTRAINT journal_pk PRIMARY KEY (id)
);
CREATE INDEX i_journal_event_group ON journal (event_group, entity_id);

CREATE TABLE snapshots
(
    id                  SERIAL,
    event_group         TEXT      NOT NULL,
    entity_id           TEXT      NOT NULL,
    snapshot_class_name TEXT      NOT NULL,
    snapshot            TEXT      NOT NULL,
    last_applied_event  INT       NOT NULL,
    created_at          TIMESTAMP NOT NULL,
    CONSTRAINT snapshots_pk PRIMARY KEY (id)
);
CREATE INDEX i_snapshots_event_group ON snapshots (event_group, entity_id);
