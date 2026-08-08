INSERT INTO roles (
    created_at,
    updated_at,
    role_name,
    description
)
VALUES
    (
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                'USER',
                'Default application user'
    ),
    (
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP,
                'ADMIN',
                'System administrator'
    )ON CONFLICT (role_name) DO NOTHING;