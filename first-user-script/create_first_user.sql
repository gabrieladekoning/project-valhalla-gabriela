INSERT INTO business_functions (id, application_name, function_name)
VALUES (UUID_TO_BIN(UUID()), 'access-control-service', 'business-function'),
       (UUID_TO_BIN(UUID()), 'access-control-service', 'business-function-permission'),
       (UUID_TO_BIN(UUID()), 'access-control-service', 'permission'),
       (UUID_TO_BIN(UUID()), 'access-control-service', 'profile-business-function-permission'),
       (UUID_TO_BIN(UUID()), 'access-control-service', 'profile'),
       (UUID_TO_BIN(UUID()), 'access-control-service', 'user'),
       (UUID_TO_BIN(UUID()), 'access-control-service', 'user-profile');

INSERT INTO permissions (id, name)
VALUES (UUID_TO_BIN(UUID()), 'create');

INSERT INTO profiles (id, name)
VALUES (UUID_TO_BIN(UUID()), 'admin');

SET @profile_id := (SELECT BIN_TO_UUID(id) FROM profiles WHERE name = 'admin');

SET @create_permission_id := (SELECT BIN_TO_UUID(id) FROM permissions WHERE name = 'create');


SET @bf_id := (SELECT BIN_TO_UUID(id) FROM business_functions WHERE function_name = 'business-function');
INSERT INTO business_function_permissions (id, business_function_id, permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@create_permission_id));

SET @bf_id := (SELECT BIN_TO_UUID(id) FROM business_functions WHERE function_name = 'business-function-permission');
INSERT INTO business_function_permissions (id, business_function_id, permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@create_permission_id));

SET @bf_id := (SELECT BIN_TO_UUID(id) FROM business_functions WHERE function_name = 'permission');
INSERT INTO business_function_permissions (id, business_function_id, permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@create_permission_id));

SET @bf_id := (SELECT BIN_TO_UUID(id) FROM business_functions WHERE function_name = 'profile-business-function-permission');
INSERT INTO business_function_permissions (id, business_function_id, permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@create_permission_id));

SET @bf_id := (SELECT BIN_TO_UUID(id) FROM business_functions WHERE function_name = 'profile');
INSERT INTO business_function_permissions (id, business_function_id, permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@create_permission_id));

SET @bf_id := (SELECT BIN_TO_UUID(id) FROM business_functions WHERE function_name = 'user-profile');
INSERT INTO business_function_permissions (id, business_function_id, permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@bf_id), UUID_TO_BIN(@create_permission_id));

SET @bf_permission_id := (SELECT BIN_TO_UUID(id) FROM business_function_permissions WHERE business_function_id = UUID_TO_BIN(@bf_id));
INSERT INTO profile_business_function_permissions (id, profile_id, business_function_permission_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN(@profile_id), UUID_TO_BIN(@bf_permission_id));

INSERT INTO user_profiles (id, application_user_id, profile_id)
VALUES (UUID_TO_BIN(UUID()), UUID_TO_BIN('replace with user UUID'), UUID_TO_BIN(@profile_id));