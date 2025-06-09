-- SQL Script for Stockify Database
-- This script creates all tables and inserts sample data

-- Create tables in order (respecting foreign key constraints)

-- 1. Categories Table


-- Insert sample data

-- Categories
INSERT INTO categories (name) VALUES ('Electronics');
INSERT INTO categories (name) VALUES ('Clothing');
INSERT INTO categories (name) VALUES ('Food');
INSERT INTO categories (name) VALUES ('Home');
INSERT INTO categories (name) VALUES ('Beauty');

-- Stores
INSERT INTO stores (store_name, address, city) VALUES ('Main Store', '123 Main St', 'New York');
INSERT INTO stores (store_name, address, city) VALUES ('Downtown Branch', '456 Downtown Ave', 'Los Angeles');
INSERT INTO stores (store_name, address, city) VALUES ('Shopping Mall Store', '789 Mall Rd', 'Chicago');

-- Providers
INSERT INTO providers (business_name, tax_id, tax_address, phone, email, contact_name, active) 
VALUES ('Electronics Supplier Inc.', '12345678', '100 Supplier St', '555-1234', 'contact@electronics.com', 'John Supplier', TRUE);
INSERT INTO providers (business_name, tax_id, tax_address, phone, email, contact_name, active) 
VALUES ('Clothing Wholesale Ltd.', '87654321', '200 Wholesale Ave', '555-5678', 'sales@clothing.com', 'Jane Wholesale', TRUE);
INSERT INTO providers (business_name, tax_id, tax_address, phone, email, contact_name, active) 
VALUES ('Food Distributors Co.', '11223344', '300 Food Blvd', '555-9012', 'orders@food.com', 'Bob Distributor', TRUE);

-- Employee
INSERT INTO employee (name, last_name, dni, status, active) 
VALUES ('Alice', 'Johnson', '12345678', 'ONLINE', TRUE);
INSERT INTO employee (name, last_name, dni, status, active) 
VALUES ('Bob', 'Smith', '23456789', 'ONLINE', TRUE);
INSERT INTO employee (name, last_name, dni, status, active) 
VALUES ('Charlie', 'Brown', '34567890', 'ONLINE', FALSE);

-- Products
INSERT INTO products (name, description, price, unit_price, sku, barcode, brand) 
VALUES ('Smartphone X', 'Latest smartphone model', 999.99, 800.00, 'PHONE-001', 'BARCODE-001', 'TechBrand');
INSERT INTO products (name, description, price, unit_price, sku, barcode, brand) 
VALUES ('T-shirt Basic', 'Cotton basic t-shirt', 19.99, 10.00, 'SHIRT-001', 'BARCODE-002', 'FashionBrand');
INSERT INTO products (name, description, price, unit_price, sku, barcode, brand) 
VALUES ('Organic Apples', 'Fresh organic apples', 3.99, 2.50, 'FRUIT-001', 'BARCODE-003', 'FreshFoods');
INSERT INTO products (name, description, price, unit_price, sku, barcode, brand) 
VALUES ('Coffee Maker', 'Automatic coffee maker', 89.99, 60.00, 'HOME-001', 'BARCODE-004', 'HomePlus');
INSERT INTO products (name, description, price, unit_price, sku, barcode, brand) 
VALUES ('Shampoo', 'Moisturizing shampoo', 7.99, 5.00, 'BEAUTY-001', 'BARCODE-005', 'BeautyEssentials');

-- Products_Categories
INSERT INTO products_categories (product_id, category_id) VALUES (1, 1); -- Smartphone in Electronics
INSERT INTO products_categories (product_id, category_id) VALUES (2, 2); -- T-shirt in Clothing
INSERT INTO products_categories (product_id, category_id) VALUES (3, 3); -- Apples in Food
INSERT INTO products_categories (product_id, category_id) VALUES (4, 4); -- Coffee Maker in Home
INSERT INTO products_categories (product_id, category_id) VALUES (5, 5); -- Shampoo in Beauty

-- Products_Providers
INSERT INTO products_providers (product_id, provider_id) VALUES (1, 1); -- Smartphone from Electronics Supplier
INSERT INTO products_providers (product_id, provider_id) VALUES (2, 2); -- T-shirt from Clothing Wholesale
INSERT INTO products_providers (product_id, provider_id) VALUES (3, 3); -- Apples from Food Distributors
INSERT INTO products_providers (product_id, provider_id) VALUES (4, 1); -- Coffee Maker from Electronics Supplier
INSERT INTO products_providers (product_id, provider_id) VALUES (5, 2); -- Shampoo from Clothing Wholesale

-- Stock
INSERT INTO stock (store_id, product_id, quantity) VALUES (1, 1, 50); -- 50 Smartphones in Main Store
INSERT INTO stock (store_id, product_id, quantity) VALUES (1, 2, 100); -- 100 T-shirts in Main Store
INSERT INTO stock (store_id, product_id, quantity) VALUES (1, 3, 200); -- 200 Apples in Main Store
INSERT INTO stock (store_id, product_id, quantity) VALUES (2, 1, 30); -- 30 Smartphones in Downtown Branch
INSERT INTO stock (store_id, product_id, quantity) VALUES (2, 4, 20); -- 20 Coffee Makers in Downtown Branch
INSERT INTO stock (store_id, product_id, quantity) VALUES (3, 5, 75); -- 75 Shampoos in Shopping Mall Store

-- Shifts
INSERT INTO shifts (shift_day, entry_time, exit_time) 
VALUES ('2023-06-01', '2023-06-01 08:00:00', '2023-06-01 16:00:00');
INSERT INTO shifts (shift_day, entry_time, exit_time) 
VALUES ('2023-06-01', '2023-06-01 16:00:00', '2023-06-02 00:00:00');
INSERT INTO shifts (shift_day, entry_time, exit_time) 
VALUES ('2023-06-02', '2023-06-02 08:00:00', '2023-06-02 16:00:00');

-- Shift_Employee
INSERT INTO shift_employee (shift_id, employee_id) VALUES (1, 1); -- Alice works morning shift on June 1
INSERT INTO shift_employee (shift_id, employee_id) VALUES (2, 2); -- Bob works evening shift on June 1
INSERT INTO shift_employee (shift_id, employee_id) VALUES (3, 1); -- Alice works morning shift on June 2

-- Time_Log
INSERT INTO time_log (date, clock_in_time, clock_out_time, employee_id)
VALUES ('2023-06-01', '08:05:00', '16:02:00', 1); -- Alice's time log for June 1
INSERT INTO time_log (date, clock_in_time, clock_out_time, employee_id) 
VALUES ('2023-06-01', '15:55:00', '00:03:00', 2); -- Bob's time log for June 1
INSERT INTO time_log (date, clock_in_time, employee_id) 
VALUES ('2023-06-02', '08:00:00', 1); -- Alice clocked in but not out yet

-- POS
INSERT INTO pos (current_amount, status, employee_id, store_id) 
VALUES (1000.00, 'ONLINE', 1, 1); -- POS 1 in Main Store operated by Alice
INSERT INTO pos (current_amount, status, employee_id, store_id) 
VALUES (1500.00, 'ONLINE', 2, 2); -- POS 2 in Downtown Branch operated by Bob
INSERT INTO pos (current_amount, status, store_id) 
VALUES (0.00, 'ONLINE', 3); -- Inactive POS in Shopping Mall Store

-- Session_POS
INSERT INTO session_pos (opening_time, close_time, opening_amount, close_amount, cash_difference, expected_amount, employee_id, pos_id) 
VALUES ('2023-06-01 08:00:00', '2023-06-01 16:00:00', 1000.00, 1500.00, 0.00, 1500.00, 1, 1); -- Alice's session on POS 1
INSERT INTO session_pos (opening_time, opening_amount, employee_id, pos_id) 
VALUES ('2023-06-02 08:00:00', 1500.00, 1, 1); -- Alice's current open session on POS 1
INSERT INTO session_pos (opening_time, close_time, opening_amount, close_amount, cash_difference, expected_amount, employee_id, pos_id) 
VALUES ('2023-06-01 16:00:00', '2023-06-02 00:00:00', 1500.00, 2000.00, 0.00, 2000.00, 2, 2); -- Bob's session on POS 2

-- Clients
INSERT INTO clients (client_first_name, client_last_name, client_dni, client_email, client_phone, client_date_of_registration) 
VALUES ('David', 'Wilson', '45678901', 'david@example.com', '555-1111', '2023-01-15');
INSERT INTO clients (client_first_name, client_last_name, client_dni, client_email, client_phone, client_date_of_registration) 
VALUES ('Emma', 'Davis', '56789012', 'emma@example.com', '555-2222', '2023-02-20');
INSERT INTO clients (client_first_name, client_last_name, client_dni, client_email, client_phone, client_date_of_registration) 
VALUES ('Frank', 'Miller', '67890123', 'frank@example.com', '555-3333', '2023-03-25');

-- Transactions
INSERT INTO transactions (total, date_time, payment_method, description, type, session_pos_id, store_id) 
VALUES (1019.98, '2023-06-01 10:30:00', 'CREDIT', 'Sale of smartphone and t-shirt', 'SALE', 1, 1);
INSERT INTO transactions (total, date_time, payment_method, description, type, session_pos_id, store_id) 
VALUES (89.99, '2023-06-01 14:45:00', 'CREDIT', 'Sale of coffee maker', 'SALE', 1, 1);
INSERT INTO transactions (total, date_time, payment_method, description, type, session_pos_id, store_id) 
VALUES (2000.00, '2023-06-01 18:20:00', 'CREDIT', 'Purchase of smartphones', 'PURCHASE', 3, 2);

-- Details_Transactions
INSERT INTO details_transactions (product_id, quantity, subtotal, transaction_id) 
VALUES (1, 1, 999.99, 1); -- 1 Smartphone in transaction 1
INSERT INTO details_transactions (product_id, quantity, subtotal, transaction_id) 
VALUES (2, 1, 19.99, 1); -- 1 T-shirt in transaction 1
INSERT INTO details_transactions (product_id, quantity, subtotal, transaction_id) 
VALUES (4, 1, 89.99, 2); -- 1 Coffee Maker in transaction 2
INSERT INTO details_transactions (product_id, quantity, subtotal, transaction_id) 
VALUES (1, 2, 2000.00, 3); -- 2 Smartphones in transaction 3 (purchase)

-- Sales
INSERT INTO sales (transaction_id, client_id) 
VALUES (1, 1); -- Transaction 1 is a sale to David
INSERT INTO sales (transaction_id, client_id) 
VALUES (2, 2); -- Transaction 2 is a sale to Emma

-- Purchases
INSERT INTO purchases (transaction_id, provider_id) 
VALUES (3, 1); -- Transaction 3 is a purchase from Electronics Supplier
