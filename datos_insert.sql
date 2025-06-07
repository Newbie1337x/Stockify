-- Tabla: roles
INSERT INTO roles (role) VALUES
                             ('ADMIN'),
                             ('MANAGER'),
                             ('EMPLOYEE');

-- Tabla: permits
INSERT INTO permits (permit) VALUES
                                 ('READ'),
                                 ('WRITE'),
                                 ('DELETE'),
                                 ('GENERATE_REPORTS'),
                                 ('MANAGE_USERS');

-- Tabla: employee
INSERT INTO employee (dni, last_name, name, status) VALUES
                                                        (53700662, 'Pérez', 'Juan', 'ONLINE'),
                                                        (30400123, 'Gómez', 'Lucía', 'OFFLINE'),
                                                        (32123123, 'Rodríguez', 'Carlos', 'ONLINE'),
                                                        (33222111, 'Fernández', 'Ana', 'OFFLINE'),
                                                        (30111222, 'López', 'María', 'ONLINE');

-- Tabla: credentials
INSERT INTO credentials (user_id, email, password, username) VALUES
                                                                 (53700662, 'juan.perez@email.com', 'pass1234', 'juanp'),
                                                                 (30400123, 'lucia.gomez@email.com', 'clave5678', 'luciag'),
                                                                 (32123123, 'carlos.rodriguez@email.com', '1234pass', 'carlitos'),
                                                                 (33222111, 'ana.fernandez@email.com', 'abcd1234', 'anafer'),
                                                                 (30111222, 'maria.lopez@email.com', 'passpass', 'marial');


-- Tabla: stores
INSERT INTO stores (city, store_name, address) VALUES
                                                   ('Rosario', 'Rosario Central', 'Bv. Oroño 1234'),
                                                   ('Mendoza', 'Mendoza Oeste', 'Av. San Martín 567'),
                                                   ('La Plata', 'La Plata Centro', 'Calle 8 Nº 123'),
                                                   ('Salta', 'Salta Norte', 'Av. Belgrano 890'),
                                                   ('Mar del Plata', 'MarCenter', 'Catamarca 456');

-- Tabla: providers
INSERT INTO providers (tax_id, business_name, contact_name, email, phone, tax_address) VALUES
                                                                                           ('20-12345678-1', 'Distribuidora Alfa', 'Ana Ruiz', 'ana@alfa.com', '3412345678', 'Córdoba 123'),
                                                                                           ('23-87654321-2', 'Importadora Beta', 'Luis Díaz', 'luis@beta.com', '3418765432', 'Rosario 456'),
                                                                                           ('27-11223344-3', 'Mayorista Gamma', 'María Pérez', 'maria@gamma.com', '3411122334', 'Mendoza 789'),
                                                                                           ('30-44332211-4', 'Proveedora Delta', 'Jorge López', 'jorge@delta.com', '3414433221', 'Salta 101'),
                                                                                           ('33-55667788-5', 'Servicios Épsilon', 'Lucía García', 'lucia@epsilon.com', '3415566778', 'La Plata 202');

-- Tabla: products y categories
INSERT INTO products (name) VALUES
                                        ('Auriculares'),
                                        ('Leche'),
                                        ('Notebook'),
                                        ('Lácteos'),
                                        ('Electrónica');

INSERT INTO categories (name) VALUES
                                           ('Tecnología'),
                                           ('Alimentos'),
                                           ('Computación'),
                                           ('Lácteos'),
                                           ('Electrodomésticos');

-- Tabla: products_categories
INSERT INTO products_categories (category_id, product_id) VALUES
                                                              (1, 1),
                                                              (2, 2),
                                                              (3, 3),
                                                              (4, 4),
                                                              (5, 5);

-- Tabla: products_providers
INSERT INTO products_providers (product_id, provider_id) VALUES
                                                             (1, 1),
                                                             (2, 2),
                                                             (3, 3),
                                                             (4, 4),
                                                             (5, 5);

-- Tabla: stock
INSERT INTO stock (quantity, product_id, store_id) VALUES

                                                       (30,1,2),
                                                       (30, 1, 1),
                                                       (50, 2, 2),
                                                       (20, 3, 3),
                                                       (15, 4, 4),
                                                       (25, 5, 5);


