CREATE DATABASE IF NOT EXISTS car_service;
CREATE DATABASE IF NOT EXISTS service_service;
CREATE DATABASE IF NOT EXISTS audit_service;

GRANT ALL PRIVILEGES ON car_service.* TO 'car_user'@'%';
GRANT ALL PRIVILEGES ON service_service.* TO 'car_user'@'%';
GRANT ALL PRIVILEGES ON audit_service.* TO 'car_user'@'%';
FLUSH PRIVILEGES;
