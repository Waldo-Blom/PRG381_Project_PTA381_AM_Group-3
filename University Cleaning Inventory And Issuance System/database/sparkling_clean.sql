-- 1. Users Table (Role-based access)
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) CHECK (role IN ('Storekeeper', 'Supervisor')) NOT NULL
);

-- 2. Suppliers Table
CREATE TABLE suppliers (
    supplier_id SERIAL PRIMARY KEY,
    supplier_name VARCHAR(100) NOT NULL,
    contact_name VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100) UNIQUE
);

-- 3. Materials Table
CREATE TABLE materials (
    material_id SERIAL PRIMARY KEY,
    material_name VARCHAR(100) NOT NULL,
    category VARCHAR(50) CHECK (category IN ('Cleaners', 'Disinfectants', 'Tools', 'Safety', 'Consumables', 'Hygiene')) NOT NULL,
    description TEXT,
    quantity INT DEFAULT 0 CHECK (quantity >= 0),
    unit VARCHAR(20) NOT NULL, -- e.g., 'liters', 'packs', 'units'
    reorder_level INT DEFAULT 5 CHECK (reorder_level >= 0),
    unit_cost DECIMAL(10, 2) NOT NULL CHECK (unit_cost >= 0),
    supplier_id INT REFERENCES suppliers(supplier_id) ON DELETE SET NULL
);

-- 4. Cleaners Table
CREATE TABLE cleaners (
    cleaner_id SERIAL PRIMARY KEY,
    employee_id VARCHAR(20) UNIQUE NOT NULL, -- e.g., 'EMP-001'
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    department VARCHAR(50) NOT NULL, -- e.g., 'Science Building', 'Library'
    hire_date DATE NOT NULL,
    status VARCHAR(15) CHECK (status IN ('active', 'inactive')) DEFAULT 'active'
);

-- 5. Stock Issuance Table (Transactions)
CREATE TABLE stock_issuances (
    issuance_id SERIAL PRIMARY KEY,
    issuance_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    material_id INT REFERENCES materials(material_id) ON DELETE RESTRICT,
    cleaner_id INT REFERENCES cleaners(cleaner_id) ON DELETE RESTRICT,
    quantity_issued INT NOT NULL CHECK (quantity_issued > 0),
    issued_by_user_id INT REFERENCES users(user_id),
    notes TEXT
);