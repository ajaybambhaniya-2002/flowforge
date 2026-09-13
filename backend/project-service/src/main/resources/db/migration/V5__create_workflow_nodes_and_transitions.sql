CREATE TABLE workflow_nodes (
    id UUID PRIMARY KEY,
    workflow_id UUID NOT NULL,
    name VARCHAR(150) NOT NULL,
    type VARCHAR(30) NOT NULL,
    position_x DOUBLE PRECISION,
    position_y DOUBLE PRECISION,
    configuration TEXT,

    CONSTRAINT fk_workflow_node_workflow
        FOREIGN KEY (workflow_id)
            REFERENCES workflows(id)
            ON DELETE CASCADE
);
CREATE TABLE workflow_transitions (
      id UUID PRIMARY KEY,
      workflow_id UUID NOT NULL,
      source_node_id UUID NOT NULL,
      target_node_id UUID NOT NULL,
      label VARCHAR(150),
      condition_expression TEXT,

  CONSTRAINT fk_transition_workflow
      FOREIGN KEY (workflow_id)
          REFERENCES workflows(id)
          ON DELETE CASCADE,

  CONSTRAINT fk_transition_source_node
      FOREIGN KEY (source_node_id)
          REFERENCES workflow_nodes(id)
          ON DELETE CASCADE,

  CONSTRAINT fk_transition_target_node
      FOREIGN KEY (target_node_id)
          REFERENCES workflow_nodes(id)
          ON DELETE CASCADE
);