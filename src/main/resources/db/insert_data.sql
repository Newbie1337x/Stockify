-- Script de inserción de datos para la base de datos Stockify
-- Este script debe ejecutarse después de que la base de datos haya sido creada
--
-- Propósito: Este script inserta datos iniciales en todas las tablas de la base de datos Stockify.
-- Incluye datos para:
--   - Permisos y roles
--   - Categorías de productos
--   - Tiendas
--   - Empleados y credenciales
--   - Proveedores
--   - Clientes
--   - Productos y stock
--   - Puntos de venta (POS)
--   - Turnos
--   - Sesiones de POS
--   - Transacciones (ventas y compras)
--
-- Uso: Ejecutar este script después de crear la base de datos y las tablas.
-- Ejemplo: psql -U usuario -d stockify -f insert_data.sql
--
-- Nota: Algunos inserts pueden requerir ajustes dependiendo del sistema de base de datos utilizado.

-- Inserción de datos en la tabla permits
INSERT INTO permits (permit) VALUES ('READ');
INSERT INTO permits (permit) VALUES ('WRITE');
INSERT INTO permits (permit) VALUES ('DELETE');
INSERT INTO permits (permit) VALUES ('ADMIN');
INSERT INTO permits (permit) VALUES ('MANAGE_USERS');
INSERT INTO permits (permit) VALUES ('MANAGE_ROLES');
INSERT INTO permits (permit) VALUES ('GENERATE_REPORTS');

-- Inserción de datos en la tabla roles
INSERT INTO roles (role) VALUES ('ADMIN');
INSERT INTO roles (role) VALUES ('MANAGER');
INSERT INTO roles (role) VALUES ('EMPLOYEE');

-- Inserción de datos en la tabla role_permits
-- ADMIN role permissions
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 1); -- READ
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 2); -- WRITE
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 3); -- DELETE
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 4); -- ADMIN
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 5); -- MANAGE_USERS
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 6); -- MANAGE_ROLES
INSERT INTO role_permits (role_id, permit_id) VALUES (1, 7); -- GENERATE_REPORTS

-- MANAGER role permissions
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 1); -- READ
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 2); -- WRITE
INSERT INTO role_permits (role_id, permit_id) VALUES (2, 7); -- GENERATE_REPORTS

-- EMPLOYEE role permissions
INSERT INTO role_permits (role_id, permit_id) VALUES (3, 1); -- READ

-- Inserción de datos en la tabla categories
INSERT INTO categories (name) VALUES ('Electrónicos');
INSERT INTO categories (name) VALUES ('Ropa');
INSERT INTO categories (name) VALUES ('Alimentos');
INSERT INTO categories (name) VALUES ('Hogar');
INSERT INTO categories (name) VALUES ('Juguetes');

-- Inserción de datos en la tabla stores
INSERT INTO stores (store_name, address, city) VALUES ('Tienda Central', 'Av. Principal 123', 'Buenos Aires');
INSERT INTO stores (store_name, address, city) VALUES ('Sucursal Norte', 'Calle Norte 456', 'Córdoba');
INSERT INTO stores (store_name, address, city) VALUES ('Sucursal Sur', 'Av. Sur 789', 'Rosario');

-- Inserción de datos en la tabla employee
INSERT INTO employee (name, last_name, dni, status, active) VALUES ('Juan', 'Pérez', '12345678', 'OFFLINE', true);
INSERT INTO employee (name, last_name, dni, status, active) VALUES ('María', 'González', '23456789', 'OFFLINE', true);
INSERT INTO employee (name, last_name, dni, status, active) VALUES ('Carlos', 'Rodríguez', '34567890', 'OFFLINE', true);
INSERT INTO employee (name, last_name, dni, status, active) VALUES ('Ana', 'Martínez', '45678901', 'OFFLINE', true);
INSERT INTO employee (name, last_name, dni, status, active) VALUES ('Pedro', 'López', '56789012', 'OFFLINE', true);

-- Inserción de datos en la tabla credentials
-- Contraseña: password123 (debe estar encriptada en producción)
INSERT INTO credentials (username, email, password, employee_dni) VALUES ('admin', 'admin@stockify.com', '$2a$10$rPiEAgQNIT1TCoKi.Iy9wuaZMhDU9Ocs9XTTaP.IS6xCxfGtJ9ZYy', '12345678');
INSERT INTO credentials (username, email, password, employee_dni) VALUES ('manager', 'manager@stockify.com', '$2a$10$rPiEAgQNIT1TCoKi.Iy9wuaZMhDU9Ocs9XTTaP.IS6xCxfGtJ9ZYy', '23456789');
INSERT INTO credentials (username, email, password, employee_dni) VALUES ('employee1', 'employee1@stockify.com', '$2a$10$rPiEAgQNIT1TCoKi.Iy9wuaZMhDU9Ocs9XTTaP.IS6xCxfGtJ9ZYy', '34567890');
INSERT INTO credentials (username, email, password, employee_dni) VALUES ('employee2', 'employee2@stockify.com', '$2a$10$rPiEAgQNIT1TCoKi.Iy9wuaZMhDU9Ocs9XTTaP.IS6xCxfGtJ9ZYy', '45678901');
INSERT INTO credentials (username, email, password, employee_dni) VALUES ('employee3', 'employee3@stockify.com', '$2a$10$rPiEAgQNIT1TCoKi.Iy9wuaZMhDU9Ocs9XTTaP.IS6xCxfGtJ9ZYy', '56789012');

-- Inserción de datos en la tabla credentials_roles
INSERT INTO credentials_roles (credential_id, role_id) VALUES (1, 1); -- admin -> ADMIN
INSERT INTO credentials_roles (credential_id, role_id) VALUES (2, 2); -- manager -> MANAGER
INSERT INTO credentials_roles (credential_id, role_id) VALUES (3, 3); -- employee1 -> EMPLOYEE
INSERT INTO credentials_roles (credential_id, role_id) VALUES (4, 3); -- employee2 -> EMPLOYEE
INSERT INTO credentials_roles (credential_id, role_id) VALUES (5, 3); -- employee3 -> EMPLOYEE

-- Inserción de datos en la tabla providers
INSERT INTO providers (business_name, tax_id, tax_address, phone, email, contact_name, active) 
VALUES ('Electro SA', 'A12345678', 'Calle Electrónica 123', '1122334455', 'contacto@electrosa.com', 'Roberto Gómez', true);
INSERT INTO providers (business_name, tax_id, tax_address, phone, email, contact_name, active) 
VALUES ('Textil Moda', 'B23456789', 'Av. Moda 456', '2233445566', 'ventas@textilmoda.com', 'Laura Sánchez', true);
INSERT INTO providers (business_name, tax_id, tax_address, phone, email, contact_name, active) 
VALUES ('Alimentos Frescos', 'C34567890', 'Ruta 7 Km 5', '3344556677', 'pedidos@alimentosfrescos.com', 'Miguel Fernández', true);
INSERT INTO providers (business_name, tax_id, tax_address, phone, email, contact_name, active) 
VALUES ('Hogar y Deco', 'D45678901', 'Calle Decoración 789', '4455667788', 'info@hogarydeco.com', 'Silvia Ramírez', true);
INSERT INTO providers (business_name, tax_id, tax_address, phone, email, contact_name, active) 
VALUES ('Juguetes Divertidos', 'E56789012', 'Av. Juegos 101', '5566778899', 'ventas@juguetesdivertidos.com', 'Fernando Torres', true);

-- Inserción de datos en la tabla clients
INSERT INTO clients (client_first_name, client_last_name, client_dni, client_email, client_phone, client_date_of_registration) 
VALUES ('Pablo', 'García', '11223344', 'pablo@email.com', '1122334455', CURRENT_DATE);
INSERT INTO clients (client_first_name, client_last_name, client_dni, client_email, client_phone, client_date_of_registration) 
VALUES ('Lucía', 'Fernández', '22334455', 'lucia@email.com', '2233445566', CURRENT_DATE);
INSERT INTO clients (client_first_name, client_last_name, client_dni, client_email, client_phone, client_date_of_registration) 
VALUES ('Martín', 'López', '33445566', 'martin@email.com', '3344556677', CURRENT_DATE);
INSERT INTO clients (client_first_name, client_last_name, client_dni, client_email, client_phone, client_date_of_registration) 
VALUES ('Valentina', 'Rodríguez', '44556677', 'valentina@email.com', '4455667788', CURRENT_DATE);
INSERT INTO clients (client_first_name, client_last_name, client_dni, client_email, client_phone, client_date_of_registration) 
VALUES ('Santiago', 'Martínez', '55667788', 'santiago@email.com', '5566778899', CURRENT_DATE);

-- Inserción de datos en la tabla products
INSERT INTO products (name, description, price, unit_price, sku, barcode, brand) 
VALUES ('Smartphone X', 'Smartphone de última generación', 1200.00, 1000.00, 'SP001', 'SP001BAR', 'TechBrand');
INSERT INTO products (name, description, price, unit_price, sku, barcode, brand) 
VALUES ('Laptop Pro', 'Laptop para profesionales', 2500.00, 2200.00, 'LP002', 'LP002BAR', 'TechBrand');
INSERT INTO products (name, description, price, unit_price, sku, barcode, brand) 
VALUES ('Camiseta Casual', 'Camiseta de algodón', 25.00, 15.00, 'CC003', 'CC003BAR', 'FashionBrand');
INSERT INTO products (name, description, price, unit_price, sku, barcode, brand) 
VALUES ('Pantalón Jeans', 'Pantalón de mezclilla', 45.00, 30.00, 'PJ004', 'PJ004BAR', 'FashionBrand');
INSERT INTO products (name, description, price, unit_price, sku, barcode, brand) 
VALUES ('Arroz Premium', 'Arroz de grano largo', 5.00, 3.50, 'AP005', 'AP005BAR', 'FoodBrand');
INSERT INTO products (name, description, price, unit_price, sku, barcode, brand) 
VALUES ('Aceite de Oliva', 'Aceite de oliva extra virgen', 8.00, 6.00, 'AO006', 'AO006BAR', 'FoodBrand');
INSERT INTO products (name, description, price, unit_price, sku, barcode, brand) 
VALUES ('Sillón Reclinable', 'Sillón reclinable de cuero', 350.00, 280.00, 'SR007', 'SR007BAR', 'HomeBrand');
INSERT INTO products (name, description, price, unit_price, sku, barcode, brand) 
VALUES ('Mesa de Centro', 'Mesa de centro de madera', 120.00, 90.00, 'MC008', 'MC008BAR', 'HomeBrand');
INSERT INTO products (name, description, price, unit_price, sku, barcode, brand) 
VALUES ('Muñeca Interactiva', 'Muñeca con funciones interactivas', 35.00, 25.00, 'MI009', 'MI009BAR', 'ToyBrand');
INSERT INTO products (name, description, price, unit_price, sku, barcode, brand) 
VALUES ('Set de Bloques', 'Set de bloques de construcción', 20.00, 15.00, 'SB010', 'SB010BAR', 'ToyBrand');

-- Inserción de datos en la tabla products_categories
INSERT INTO products_categories (product_id, category_id) VALUES (1, 1); -- Smartphone X -> Electrónicos
INSERT INTO products_categories (product_id, category_id) VALUES (2, 1); -- Laptop Pro -> Electrónicos
INSERT INTO products_categories (product_id, category_id) VALUES (3, 2); -- Camiseta Casual -> Ropa
INSERT INTO products_categories (product_id, category_id) VALUES (4, 2); -- Pantalón Jeans -> Ropa
INSERT INTO products_categories (product_id, category_id) VALUES (5, 3); -- Arroz Premium -> Alimentos
INSERT INTO products_categories (product_id, category_id) VALUES (6, 3); -- Aceite de Oliva -> Alimentos
INSERT INTO products_categories (product_id, category_id) VALUES (7, 4); -- Sillón Reclinable -> Hogar
INSERT INTO products_categories (product_id, category_id) VALUES (8, 4); -- Mesa de Centro -> Hogar
INSERT INTO products_categories (product_id, category_id) VALUES (9, 5); -- Muñeca Interactiva -> Juguetes
INSERT INTO products_categories (product_id, category_id) VALUES (10, 5); -- Set de Bloques -> Juguetes

-- Inserción de datos en la tabla products_providers
INSERT INTO products_providers (product_id, provider_id) VALUES (1, 1); -- Smartphone X -> Electro SA
INSERT INTO products_providers (product_id, provider_id) VALUES (2, 1); -- Laptop Pro -> Electro SA
INSERT INTO products_providers (product_id, provider_id) VALUES (3, 2); -- Camiseta Casual -> Textil Moda
INSERT INTO products_providers (product_id, provider_id) VALUES (4, 2); -- Pantalón Jeans -> Textil Moda
INSERT INTO products_providers (product_id, provider_id) VALUES (5, 3); -- Arroz Premium -> Alimentos Frescos
INSERT INTO products_providers (product_id, provider_id) VALUES (6, 3); -- Aceite de Oliva -> Alimentos Frescos
INSERT INTO products_providers (product_id, provider_id) VALUES (7, 4); -- Sillón Reclinable -> Hogar y Deco
INSERT INTO products_providers (product_id, provider_id) VALUES (8, 4); -- Mesa de Centro -> Hogar y Deco
INSERT INTO products_providers (product_id, provider_id) VALUES (9, 5); -- Muñeca Interactiva -> Juguetes Divertidos
INSERT INTO products_providers (product_id, provider_id) VALUES (10, 5); -- Set de Bloques -> Juguetes Divertidos

-- Inserción de datos en la tabla stock
INSERT INTO stock (store_id, product_id, quantity, low_stock_alert_sent) VALUES (1, 1, 50.0, false);
INSERT INTO stock (store_id, product_id, quantity, low_stock_alert_sent) VALUES (1, 2, 30.0, false);
INSERT INTO stock (store_id, product_id, quantity, low_stock_alert_sent) VALUES (1, 3, 100.0, false);
INSERT INTO stock (store_id, product_id, quantity, low_stock_alert_sent) VALUES (1, 4, 80.0, false);
INSERT INTO stock (store_id, product_id, quantity, low_stock_alert_sent) VALUES (1, 5, 200.0, false);
INSERT INTO stock (store_id, product_id, quantity, low_stock_alert_sent) VALUES (2, 6, 150.0, false);
INSERT INTO stock (store_id, product_id, quantity, low_stock_alert_sent) VALUES (2, 7, 20.0, false);
INSERT INTO stock (store_id, product_id, quantity, low_stock_alert_sent) VALUES (2, 8, 25.0, false);
INSERT INTO stock (store_id, product_id, quantity, low_stock_alert_sent) VALUES (3, 9, 40.0, false);
INSERT INTO stock (store_id, product_id, quantity, low_stock_alert_sent) VALUES (3, 10, 60.0, false);

-- Inserción de datos en la tabla pos
INSERT INTO pos (current_amount, status, employee_id, store_id) VALUES (1000.00, 'OFFLINE', '12345678', 1);
INSERT INTO pos (current_amount, status, employee_id, store_id) VALUES (1500.00, 'OFFLINE', '23456789', 2);
INSERT INTO pos (current_amount, status, employee_id, store_id) VALUES (2000.00, 'OFFLINE', '34567890', 3);

-- Inserción de datos en la tabla shifts
INSERT INTO shifts (shift_day, entry_time, exit_time) VALUES (CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO shifts (shift_day, entry_time, exit_time) VALUES (CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO shifts (shift_day, entry_time, exit_time) VALUES (CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserción de datos en la tabla shift_employee
INSERT INTO shift_employee (shift_id, employee_id) VALUES (1, '12345678');
INSERT INTO shift_employee (shift_id, employee_id) VALUES (2, '23456789');
INSERT INTO shift_employee (shift_id, employee_id) VALUES (3, '34567890');

-- Inserción de datos en la tabla time_log
-- Commented out due to schema issues, uncomment and adjust as needed
-- INSERT INTO time_log (date, clock_in_time, clock_out_time, employee_id, store_id) 
-- VALUES (CURRENT_DATE, '08:00:00', '16:00:00', '12345678', 1);
-- INSERT INTO time_log (date, clock_in_time, clock_out_time, employee_id, store_id) 
-- VALUES (CURRENT_DATE, '09:00:00', '17:00:00', '23456789', 2);
-- INSERT INTO time_log (date, clock_in_time, clock_out_time, employee_id, store_id) 
-- VALUES (CURRENT_DATE, '10:00:00', '18:00:00', '34567890', 3);

-- Inserción de datos en la tabla session_pos
INSERT INTO session_pos (opening_time, close_time, opening_amount, close_amount, cash_difference, expected_amount, employee_id, pos_id) 
VALUES (CURRENT_TIMESTAMP, NULL, 1000.00, NULL, NULL, NULL, '12345678', 1);
INSERT INTO session_pos (opening_time, close_time, opening_amount, close_amount, cash_difference, expected_amount, employee_id, pos_id) 
VALUES (CURRENT_TIMESTAMP, NULL, 1500.00, NULL, NULL, NULL, '23456789', 2);
INSERT INTO session_pos (opening_time, close_time, opening_amount, close_amount, cash_difference, expected_amount, employee_id, pos_id) 
VALUES (CURRENT_TIMESTAMP, NULL, 2000.00, NULL, NULL, NULL, '34567890', 3);

-- Inserción de datos en la tabla transactions (ejemplos de ventas)
INSERT INTO transactions (total, date_time, payment_method, description, type, session_pos_id, store_id) 
VALUES (1225.00, CURRENT_TIMESTAMP, 'CASH', 'Venta de smartphone', 'SALE', 1, 1);
INSERT INTO transactions (total, date_time, payment_method, description, type, session_pos_id, store_id) 
VALUES (70.00, CURRENT_TIMESTAMP, 'CREDIT', 'Venta de ropa', 'SALE', 2, 2);
INSERT INTO transactions (total, date_time, payment_method, description, type, session_pos_id, store_id) 
VALUES (55.00, CURRENT_TIMESTAMP, 'DEBIT', 'Venta de juguetes', 'SALE', 3, 3);

-- Inserción de datos en la tabla sales
INSERT INTO sales (transaction_id, client_id) VALUES (1, 1);
INSERT INTO sales (transaction_id, client_id) VALUES (2, 2);
INSERT INTO sales (transaction_id, client_id) VALUES (3, 3);

-- Inserción de datos en la tabla details_transactions
INSERT INTO details_transactions (product_id, quantity, subtotal, transaction_id) VALUES (1, 1.0, 1200.00, 1);
INSERT INTO details_transactions (product_id, quantity, subtotal, transaction_id) VALUES (5, 5.0, 25.00, 1);
INSERT INTO details_transactions (product_id, quantity, subtotal, transaction_id) VALUES (3, 2.0, 50.00, 2);
INSERT INTO details_transactions (product_id, quantity, subtotal, transaction_id) VALUES (4, 0.5, 20.00, 2);
INSERT INTO details_transactions (product_id, quantity, subtotal, transaction_id) VALUES (9, 1.0, 35.00, 3);
INSERT INTO details_transactions (product_id, quantity, subtotal, transaction_id) VALUES (10, 1.0, 20.00, 3);

-- Inserción de datos en la tabla transactions (ejemplos de compras)
INSERT INTO transactions (total, date_time, payment_method, description, type, session_pos_id, store_id) 
VALUES (10000.00, CURRENT_TIMESTAMP, 'BANK_TRANSFER', 'Compra de electrónicos', 'PURCHASE', 1, 1);
INSERT INTO transactions (total, date_time, payment_method, description, type, session_pos_id, store_id) 
VALUES (5000.00, CURRENT_TIMESTAMP, 'BANK_TRANSFER', 'Compra de ropa', 'PURCHASE', 2, 2);
INSERT INTO transactions (total, date_time, payment_method, description, type, session_pos_id, store_id) 
VALUES (3000.00, CURRENT_TIMESTAMP, 'BANK_TRANSFER', 'Compra de juguetes', 'PURCHASE', 3, 3);

-- Inserción de datos en la tabla purchases
INSERT INTO purchases (transaction_id, provider_id) VALUES (4, 1);
INSERT INTO purchases (transaction_id, provider_id) VALUES (5, 2);
INSERT INTO purchases (transaction_id, provider_id) VALUES (6, 5);

-- Inserción de datos en la tabla details_transactions para las compras
INSERT INTO details_transactions (product_id, quantity, subtotal, transaction_id) VALUES (1, 10.0, 10000.00, 4);
INSERT INTO details_transactions (product_id, quantity, subtotal, transaction_id) VALUES (3, 200.0, 3000.00, 5);
INSERT INTO details_transactions (product_id, quantity, subtotal, transaction_id) VALUES (4, 100.0, 2000.00, 5);
INSERT INTO details_transactions (product_id, quantity, subtotal, transaction_id) VALUES (9, 50.0, 1250.00, 6);
INSERT INTO details_transactions (product_id, quantity, subtotal, transaction_id) VALUES (10, 100.0, 1750.00, 6);
