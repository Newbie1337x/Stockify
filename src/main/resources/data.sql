-- SQL Script for Stockify Database
-- This script creates all tables and inserts sample data

-- Insert sample data

-- Categories
INSERT INTO categories (name) VALUES
                                  ('Electronics'),
                                  ('Clothing'),
                                  ('Food'),
                                  ('Home'),
                                  ('Beauty');

-- Stores
INSERT INTO stores (store_name, address, city) VALUES
                                                   ('Main Store', '123 Main St', 'New York'),
                                                   ('Downtown Branch', '456 Downtown Ave', 'Los Angeles'),
                                                   ('Shopping Mall Store', '789 Mall Rd', 'Chicago');

-- Providers
INSERT INTO providers (business_name, tax_id, tax_address, phone, email, contact_name, active) VALUES
                                                                                                   ('Electronics Supplier Inc.', '12345678', '100 Supplier St', '555-1234', 'contact@electronics.com', 'John Supplier', TRUE),
                                                                                                   ('Clothing Wholesale Ltd.', '87654321', '200 Wholesale Ave', '555-5678', 'sales@clothing.com', 'Jane Wholesale', TRUE),
                                                                                                   ('Food Distributors Co.', '11223344', '300 Food Blvd', '555-9012', 'orders@food.com', 'Bob Distributor', TRUE);

-- Employees
INSERT INTO employee (name, last_name, dni, status, active) VALUES
                                                                ('Alice', 'Johnson', '12345678', 'ONLINE', TRUE),
                                                                ('Bob', 'Smith', '23456789', 'ONLINE', TRUE),
                                                                ('Charlie', 'Brown', '34567890', 'ONLINE', FALSE);

-- Products
INSERT INTO products (name, description, price, unit_price, sku, barcode, brand) VALUES
                                                                                     ('Smartphone X', 'Latest smartphone model', 999.99, 800.00, 'PHONE-001', 'BARCODE-001', 'TechBrand'),
                                                                                     ('T-shirt Basic', 'Cotton basic t-shirt', 19.99, 10.00, 'SHIRT-001', 'BARCODE-002', 'FashionBrand'),
                                                                                     ('Organic Apples', 'Fresh organic apples', 3.99, 2.50, 'FRUIT-001', 'BARCODE-003', 'FreshFoods'),
                                                                                     ('Coffee Maker', 'Automatic coffee maker', 89.99, 60.00, 'HOME-001', 'BARCODE-004', 'HomePlus'),
                                                                                     ('Shampoo', 'Moisturizing shampoo', 7.99, 5.00, 'BEAUTY-001', 'BARCODE-005', 'BeautyEssentials');

-- Product-Category
INSERT INTO products_categories (product_id, category_id) VALUES
                                                              (1, 1),
                                                              (2, 2),
                                                              (3, 3),
                                                              (4, 4),
                                                              (5, 5);

-- Product-Provider
INSERT INTO products_providers (product_id, provider_id) VALUES
                                                             (1, 1),
                                                             (2, 2),
                                                             (3, 3),
                                                             (4, 1),
                                                             (5, 2);

-- Stock
INSERT INTO stock (store_id, product_id, quantity) VALUES
                                                       (1, 1, 50),
                                                       (1, 2, 100),
                                                       (1, 3, 200),
                                                       (2, 1, 30),
                                                       (2, 4, 20),
                                                       (3, 5, 75);

-- Shifts
INSERT INTO shifts (shift_day, entry_time, exit_time) VALUES
                                                          ('2023-06-01', '2023-06-01 08:00:00', '2023-06-01 16:00:00'),
                                                          ('2023-06-01', '2023-06-01 16:00:00', '2023-06-02 00:00:00'),
                                                          ('2023-06-02', '2023-06-02 08:00:00', '2023-06-02 16:00:00');

-- Shift_Employee
INSERT INTO shift_employee (shift_id, employee_id) VALUES
                                                       (1, 1),
                                                       (2, 2),
                                                       (3, 1);

-- Time Log
INSERT INTO time_log (date, clock_in_time, clock_out_time, employee_id) VALUES
                                                                            ('2023-06-01', '08:05:00', '16:02:00', 1),
                                                                            ('2023-06-01', '15:55:00', '00:03:00', 2);
INSERT INTO time_log (date, clock_in_time, employee_id) VALUES
    ('2023-06-02', '08:00:00', 1);

-- POS (todos cerrados)
INSERT INTO pos (current_amount, status, employee_id, store_id) VALUES
                                                                    (1000.00, 'OFFLINE', 1, 1),
                                                                    (1500.00, 'OFFLINE', 2, 2);
INSERT INTO pos (current_amount, status, store_id) VALUES
    (0.00, 'OFFLINE', 3);

-- Session_POS (todas cerradas con diferencia 0)
INSERT INTO session_pos (opening_time, close_time, opening_amount, close_amount, cash_difference, expected_amount, employee_id, pos_id) VALUES
                                                                                                                                            ('2023-06-01 08:00:00', '2023-06-01 16:00:00', 1000.00, 1500.00, 0.00, 1500.00, 1, 1),
                                                                                                                                            ('2023-06-01 16:00:00', '2023-06-02 00:00:00', 1500.00, 2000.00, 0.00, 2000.00, 2, 2),
                                                                                                                                            ('2023-06-02 08:00:00', '2023-06-02 16:00:00', 1500.00, 1500.00, 0.00, 1500.00, 1, 1);

-- Clients
INSERT INTO clients (client_first_name, client_last_name, client_dni, client_email, client_phone, client_date_of_registration) VALUES
                                                                                                                                   ('David', 'Wilson', '45678901', 'david@example.com', '555-1111', '2023-01-15'),
                                                                                                                                   ('Emma', 'Davis', '56789012', 'emma@example.com', '555-2222', '2023-02-20'),
                                                                                                                                   ('Frank', 'Miller', '67890123', 'frank@example.com', '555-3333', '2023-03-25');

-- Transactions (con closing_time)
INSERT INTO transactions (total, date_time, payment_method, description, type, session_pos_id, store_id) VALUES
                                                                                                             (1019.98, '2023-06-01 10:30:00',  'CREDIT', 'Sale of smartphone and t-shirt', 'SALE', 1, 1),
                                                                                                             (89.99, '2023-06-01 14:45:00',  'CREDIT', 'Sale of coffee maker', 'SALE', 1, 1),
                                                                                                             (2000.00, '2023-06-01 18:20:00', 'CREDIT', 'Purchase of smartphones', 'PURCHASE', 2, 2);

-- Details_Transactions
INSERT INTO details_transactions (product_id, quantity, subtotal, transaction_id) VALUES
                                                                                      (1, 1, 999.99, 1),
                                                                                      (2, 1, 19.99, 1),
                                                                                      (4, 1, 89.99, 2),
                                                                                      (1, 2, 2000.00, 3);

-- Sales
INSERT INTO sales (transaction_id, client_id) VALUES
                                                  (1, 1),
                                                  (2, 2);

-- Purchases
INSERT INTO purchases (transaction_id, provider_id) VALUES
    (3, 1);
