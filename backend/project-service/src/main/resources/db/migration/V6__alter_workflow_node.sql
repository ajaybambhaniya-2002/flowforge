DROP TABLE workflow_transitions;
DROP TABLE workflow_nodes;

CREATE TABLE workflow_nodes (
            id UUID PRIMARY KEY,

            name VARCHAR(150) NOT NULL,

            description VARCHAR(500),

            node_type VARCHAR(50) NOT NULL,

            position_x INTEGER NOT NULL,

            position_y INTEGER NOT NULL,

            workflow_id UUID NOT NULL,

            created_at TIMESTAMP NOT NULL,

            updated_at TIMESTAMP NOT NULL,

            CONSTRAINT fk_workflow_nodes_workflow
                FOREIGN KEY (workflow_id)
                    REFERENCES workflows(id)
                    ON DELETE CASCADE
);

CREATE INDEX idx_workflow_nodes_workflow_id
    ON workflow_nodes(workflow_id);

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