

-- Categories
INSERT INTO categories (name) VALUES ('Electronics');
INSERT INTO categories (name) VALUES ('Clothing');
INSERT INTO categories (name) VALUES ('Home & Kitchen');
INSERT INTO categories (name) VALUES ('Books');
INSERT INTO categories (name) VALUES ('Sports & Outdoors');
INSERT INTO categories (name) VALUES ('Toys & Games');
INSERT INTO categories (name) VALUES ('Beauty & Personal Care');
INSERT INTO categories (name) VALUES ('Health & Household');
INSERT INTO categories (name) VALUES ('Grocery');
INSERT INTO categories (name) VALUES ('Office Products');

-- Providers
INSERT INTO providers (business_name, tax_id, tax_address, phone, email, contact_name, active)
VALUES ('TechSupplies Inc.', '123456789', '123 Tech St, Silicon Valley, CA', '555-123-4567', 'contact@techsupplies.com', 'John Smith', true);

INSERT INTO providers (business_name, tax_id, tax_address, phone, email, contact_name, active)
VALUES ('Fashion Wholesale', '987654321', '456 Fashion Ave, New York, NY', '555-987-6543', 'info@fashionwholesale.com', 'Emma Johnson', true);

INSERT INTO providers (business_name, tax_id, tax_address, phone, email, contact_name, active)
VALUES ('Home Essentials Co.', '456789123', '789 Home Blvd, Chicago, IL', '555-456-7890', 'sales@homeessentials.com', 'Michael Brown', true);

INSERT INTO providers (business_name, tax_id, tax_address, phone, email, contact_name, active)
VALUES ('Book Distributors Ltd.', '789123456', '101 Book Lane, Boston, MA', '555-789-1234', 'orders@bookdistributors.com', 'Sarah Davis', true);

INSERT INTO providers (business_name, tax_id, tax_address, phone, email, contact_name, active)
VALUES ('Sports Gear Supply', '321654987', '202 Sports Way, Denver, CO', '555-321-6549', 'info@sportsgearsupply.com', 'David Wilson', true);

-- Stores
INSERT INTO stores (store_name, address, city) VALUES ('Downtown Store', '123 Main St', 'New York');
INSERT INTO stores (store_name, address, city) VALUES ('Westside Mall', '456 West Ave', 'Los Angeles');
INSERT INTO stores (store_name, address, city) VALUES ('Eastside Plaza', '789 East Blvd', 'Chicago');
INSERT INTO stores (store_name, address, city) VALUES ('Northside Center', '101 North St', 'Boston');
INSERT INTO stores (store_name, address, city) VALUES ('Southside Outlet', '202 South Rd', 'Miami');

-- Products
INSERT INTO products (name, description, price, stock, sku, barcode, brand)
VALUES ('Laptop Pro X', 'High-performance laptop with 16GB RAM and 512GB SSD', 1299.99, 0, 'LP-001', 'LP001-BARCODE', 'TechBrand');

INSERT INTO products (name, description, price, stock, sku, barcode, brand)
VALUES ('Smartphone Y', 'Latest smartphone with 128GB storage and dual camera', 899.99, 0, 'SP-002', 'SP002-BARCODE', 'MobileTech');

INSERT INTO products (name, description, price, stock, sku, barcode, brand)
VALUES ('Designer T-Shirt', 'Premium cotton t-shirt with designer logo', 49.99, 0, 'TS-003', 'TS003-BARCODE', 'FashionBrand');

INSERT INTO products (name, description, price, stock, sku, barcode, brand)
VALUES ('Jeans Classic', 'Classic fit jeans with durable denim', 79.99, 0, 'JN-004', 'JN004-BARCODE', 'DenimCo');

INSERT INTO products (name, description, price, stock, sku, barcode, brand)
VALUES ('Coffee Maker', 'Programmable coffee maker with 12-cup capacity', 89.99, 0, 'CM-005', 'CM005-BARCODE', 'HomeAppliances');

INSERT INTO products (name, description, price, stock, sku, barcode, brand)
VALUES ('Blender Pro', 'High-power blender with multiple speed settings', 129.99, 0, 'BP-006', 'BP006-BARCODE', 'KitchenPro');

INSERT INTO products (name, description, price, stock, sku, barcode, brand)
VALUES ('Bestseller Novel', 'Award-winning fiction novel by renowned author', 24.99, 0, 'BN-007', 'BN007-BARCODE', 'BookPublisher');

INSERT INTO products (name, description, price, stock, sku, barcode, brand)
VALUES ('Cookbook Collection', 'Collection of gourmet recipes from around the world', 34.99, 0, 'CC-008', 'CC008-BARCODE', 'CulinaryPress');

INSERT INTO products (name, description, price, stock, sku, barcode, brand)
VALUES ('Tennis Racket', 'Professional-grade tennis racket with carbon fiber frame', 159.99, 0, 'TR-009', 'TR009-BARCODE', 'SportsPro');

INSERT INTO products (name, description, price, stock, sku, barcode, brand)
VALUES ('Yoga Mat', 'Non-slip yoga mat with carrying strap', 29.99, 0, 'YM-010', 'YM010-BARCODE', 'FitnessCo');

-- Stock
INSERT INTO stock (store_id, product_id, quantity) VALUES (1, 1, 25);
INSERT INTO stock (store_id, product_id, quantity)VALUES (1, 2, 30);
INSERT INTO stock (store_id, product_id, quantity) VALUES (1, 3, 50);
INSERT INTO stock (store_id, product_id, quantity) VALUES (2, 1, 15);
INSERT INTO stock (store_id, product_id, quantity) VALUES (2, 4, 40);
INSERT INTO stock (store_id, product_id, quantity) VALUES (2, 5, 20);
INSERT INTO stock (store_id, product_id, quantity) VALUES (3, 6, 10);
INSERT INTO stock (store_id, product_id, quantity) VALUES (3, 7, 35);
INSERT INTO stock (store_id, product_id, quantity) VALUES (4, 8, 25);
INSERT INTO stock (store_id, product_id, quantity) VALUES (4, 9, 15);
INSERT INTO stock (store_id, product_id, quantity) VALUES (5, 10, 30);

-- Products-Categories relationships
INSERT INTO products_categories (product_id, category_id) VALUES (1, 1); -- Laptop in Electronics
INSERT INTO products_categories (product_id, category_id) VALUES (2, 1); -- Smartphone in Electronics
INSERT INTO products_categories (product_id, category_id) VALUES (3, 2); -- T-Shirt in Clothing
INSERT INTO products_categories (product_id, category_id) VALUES (4, 2); -- Jeans in Clothing
INSERT INTO products_categories (product_id, category_id) VALUES (5, 3); -- Coffee Maker in Home & Kitchen
INSERT INTO products_categories (product_id, category_id) VALUES (6, 3); -- Blender in Home & Kitchen
INSERT INTO products_categories (product_id, category_id) VALUES (7, 4); -- Novel in Books
INSERT INTO products_categories (product_id, category_id) VALUES (8, 4); -- Cookbook in Books
INSERT INTO products_categories (product_id, category_id) VALUES (9, 5); -- Tennis Racket in Sports & Outdoors
INSERT INTO products_categories (product_id, category_id) VALUES (10, 5); -- Yoga Mat in Sports & Outdoors

-- Products-Providers relationships
INSERT INTO products_providers (product_id, provider_id) VALUES (1, 1); -- Laptop from TechSupplies
INSERT INTO products_providers (product_id, provider_id) VALUES (2, 1); -- Smartphone from TechSupplies
INSERT INTO products_providers (product_id, provider_id) VALUES (3, 2); -- T-Shirt from Fashion Wholesale
INSERT INTO products_providers (product_id, provider_id) VALUES (4, 2); -- Jeans from Fashion Wholesale
INSERT INTO products_providers (product_id, provider_id) VALUES (5, 3); -- Coffee Maker from Home Essentials
INSERT INTO products_providers (product_id, provider_id) VALUES (6, 3); -- Blender from Home Essentials
INSERT INTO products_providers (product_id, provider_id) VALUES (7, 4); -- Novel from Book Distributors
INSERT INTO products_providers (product_id, provider_id) VALUES (8, 4); -- Cookbook from Book Distributors
INSERT INTO products_providers (product_id, provider_id) VALUES (9, 5); -- Tennis Racket from Sports Gear Supply
INSERT INTO products_providers (product_id, provider_id) VALUES (10, 5); -- Yoga Mat from Sports Gear Supply