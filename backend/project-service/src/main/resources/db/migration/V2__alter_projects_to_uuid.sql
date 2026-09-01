DROP TABLE projects;

CREATE TABLE projects (
      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

      name VARCHAR(150) NOT NULL,

      description VARCHAR(500),

      project_key VARCHAR(100) NOT NULL,

      owner_id UUID NOT NULL,

      status VARCHAR(30) NOT NULL,

      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);