INSERT INTO business_functions (id, application_name, function_name)
VALUES (UUID_TO_BIN(UUID()), 'access-control-service', 'business-function'),
       (UUID_TO_BIN(UUID()), 'access-control-service', 'business-function-permission'),
       (UUID_TO_BIN(UUID()), 'access-control-service', 'permission'),
       (UUID_TO_BIN(UUID()), 'access-control-service', 'profile-business-function-permission'),
       (UUID_TO_BIN(UUID()), 'access-control-service', 'profile'),
       (UUID_TO_BIN(UUID()), 'access-control-service', 'user'),
       (UUID_TO_BIN(UUID()), 'access-control-service', 'user-profile'),
       (UUID_TO_BIN(UUID()), 'access-control-service', 'setup-all');

INSERT INTO permissions (id, name)
VALUES (UUID_TO_BIN(UUID()), 'create'),
       (UUID_TO_BIN(UUID()), 'read'),
       (UUID_TO_BIN(UUID()), 'update'),
       (UUID_TO_BIN(UUID()), 'delete');

INSERT INTO profiles (id, name)
VALUES (UUID_TO_BIN(UUID()), 'admin');

SET @profile_id := (SELECT BIN_TO_UUID(id) FROM profiles WHERE name = 'admin');

SET @create_permission_id := (SELECT BIN_TO_UUID(id) FROM permissions WHERE name = 'create');
SET @read_permission_id := (SELECT BIN_TO_UUID(id) FROM permissions WHERE name = 'read');
SET @update_permission_id := (SELECT BIN_TO_UUID(id) FROM permissions WHERE name = 'update');
SET @delete_permission_id := (SELECT BIN_TO_UUID(id) FROM permissions WHERE name = 'delete');


SET @bf_id := (SELECT BIN_TO_UUID(id) FROM business_functions WHERE function_name = 'business-function');
INSERT INTO business_function_permissions (id, business_function_id, permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@create_permission_id)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@read_permission_id)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@update_permission_id)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@delete_permission_id));
SET @bf_permission_id_create := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@create_permission_id));
SET @bf_permission_id_read := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@read_permission_id));
SET @bf_permission_id_update := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@update_permission_id));
SET @bf_permission_id_delete := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@delete_permission_id));
INSERT INTO profile_business_function_permissions (id, profile_id, business_function_permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_create)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_read)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_update)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_delete));

SET @bf_id := (SELECT BIN_TO_UUID(id) FROM business_functions WHERE function_name = 'business-function-permission');
INSERT INTO business_function_permissions (id, business_function_id, permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@create_permission_id)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@read_permission_id)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@delete_permission_id));
SET @bf_permission_id_create := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@create_permission_id));
SET @bf_permission_id_read := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@read_permission_id));
SET @bf_permission_id_delete := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@delete_permission_id));
INSERT INTO profile_business_function_permissions (id, profile_id, business_function_permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_create)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_read)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_delete));

SET @bf_id := (SELECT BIN_TO_UUID(id) FROM business_functions WHERE function_name = 'permission');
INSERT INTO business_function_permissions (id, business_function_id, permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@create_permission_id)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@read_permission_id)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@update_permission_id)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@delete_permission_id));
SET @bf_permission_id_create := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@create_permission_id));
SET @bf_permission_id_read := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@read_permission_id));
SET @bf_permission_id_update := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@update_permission_id));
SET @bf_permission_id_delete := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@delete_permission_id));
INSERT INTO profile_business_function_permissions (id, profile_id, business_function_permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_create)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_read)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_update)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_delete));

SET @bf_id := (SELECT BIN_TO_UUID(id) FROM business_functions WHERE function_name = 'profile-business-function-permission');
INSERT INTO business_function_permissions (id, business_function_id, permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@create_permission_id)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@read_permission_id)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@delete_permission_id));
SET @bf_permission_id_create := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@create_permission_id));
SET @bf_permission_id_read := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@read_permission_id));
SET @bf_permission_id_delete := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@delete_permission_id));
INSERT INTO profile_business_function_permissions (id, profile_id, business_function_permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_create)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_read)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_delete));

SET @bf_id := (SELECT BIN_TO_UUID(id) FROM business_functions WHERE function_name = 'profile');
INSERT INTO business_function_permissions (id, business_function_id, permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@create_permission_id)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@read_permission_id)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@update_permission_id)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@delete_permission_id));
SET @bf_permission_id_create := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@create_permission_id));
SET @bf_permission_id_read := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@read_permission_id));
SET @bf_permission_id_update := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@update_permission_id));
SET @bf_permission_id_delete := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@delete_permission_id));
INSERT INTO profile_business_function_permissions (id, profile_id, business_function_permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_create)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_read)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_update)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_delete));

SET @bf_id := (SELECT BIN_TO_UUID(id) FROM business_functions WHERE function_name = 'setup-all');
INSERT INTO business_function_permissions (id, business_function_id, permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@create_permission_id));
SET @bf_permission_id := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id));
INSERT INTO profile_business_function_permissions (id, profile_id, business_function_permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id));

SET @bf_id := (SELECT BIN_TO_UUID(id) FROM business_functions WHERE function_name = 'user');
INSERT INTO business_function_permissions (id, business_function_id, permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@read_permission_id));
SET @bf_permission_id_read := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@read_permission_id));
INSERT INTO profile_business_function_permissions (id, profile_id, business_function_permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_read));

SET @bf_id := (SELECT BIN_TO_UUID(id) FROM business_functions WHERE function_name = 'user-profile');
INSERT INTO business_function_permissions (id, business_function_id, permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@create_permission_id)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@read_permission_id)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@delete_permission_id));
SET @bf_permission_id_create := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@create_permission_id));
SET @bf_permission_id_read := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@read_permission_id));
SET @bf_permission_id_delete := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id) AND permission_id = UUID_TO_BIN(@delete_permission_id));
INSERT INTO profile_business_function_permissions (id, profile_id, business_function_permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_create)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_read)),
       (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id_delete));

-- insert UUID generated by Keycloak below
INSERT INTO user_profiles (id, application_user_id, profile_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN('replace with user UUID'), UUID_TO_BIN(@profile_id));