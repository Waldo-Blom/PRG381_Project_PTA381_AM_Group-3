-- Database setup script for Suppliers and Cleaners Management
-- Run this script to create the necessary tables for the application

-- Create suppliers table
CREATE TABLE IF NOT EXISTS suppliers (
    supplier_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(255),
    status VARCHAR(20) DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create cleaners table
CREATE TABLE IF NOT EXISTS cleaners (
    cleaner_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    department VARCHAR(100),
    hire_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create indexes for better search performance
CREATE INDEX idx_suppliers_name ON suppliers(name);
CREATE INDEX idx_suppliers_email ON suppliers(email);
CREATE INDEX idx_suppliers_status ON suppliers(status);

CREATE INDEX idx_cleaners_name ON cleaners(name);
CREATE INDEX idx_cleaners_email ON cleaners(email);
CREATE INDEX idx_cleaners_department ON cleaners(department);
CREATE INDEX idx_cleaners_status ON cleaners(status);

-- Insert sample data (optional - remove if not needed)
INSERT INTO suppliers (name, email, phone, address, status) VALUES
('ABC Cleaning Supplies', 'contact@abc-supplies.com', '555-0101', '123 Main St', 'Active'),
('Fresh Clean Ltd', 'info@freshclean.com', '555-0102', '456 Oak Ave', 'Active'),
('Super Supplies Co', 'sales@supersupplies.com', '555-0103', '789 Pine Rd', 'Inactive'),
('Eco Clean Solutions', 'support@ecoclean.com', '555-0104', '321 Green Ln', 'Active');

INSERT INTO cleaners (name, email, phone, department, hire_date, status) VALUES
('John Smith', 'john.smith@university.edu', '555-1001', 'Administration', '2022-01-15', 'Active'),
('Mary Johnson', 'mary.johnson@university.edu', '555-1002', 'Facilities', '2021-06-20', 'Active'),
('Robert Williams', 'robert.williams@university.edu', '555-1003', 'Administration', '2023-02-10', 'Active'),
('Sarah Davis', 'sarah.davis@university.edu', '555-1004', 'Facilities', '2022-09-05', 'Inactive'),
('Michael Brown', 'michael.brown@university.edu', '555-1005', 'Maintenance', '2023-01-12', 'Active'),
('Jessica Miller', 'jessica.miller@university.edu', '555-1006', 'Facilities', '2021-11-08', 'Active'),
('David Jones', 'david.jones@university.edu', '555-1007', 'Administration', '2022-03-22', 'Active'),
('Lisa Garcia', 'lisa.garcia@university.edu', '555-1008', 'Maintenance', '2023-04-03', 'Inactive');
