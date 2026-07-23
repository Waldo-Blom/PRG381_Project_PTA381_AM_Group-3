-- Sparkling Clean Database Schema and Mock Data for University Cleaning Inventory and Issuance System

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;
SET default_tablespace = '';
SET default_table_access_method = heap;

-- ========================================
-- Tables
-- ========================================

CREATE TABLE public.users (
    user_id integer NOT NULL,
    full_name character varying(100) NOT NULL,
    username character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    password_hash character varying(255) NOT NULL,
    role character varying(20) NOT NULL,
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['Storekeeper'::character varying, 'Cleaner'::character varying, 'Owner'::character varying])::text[])))
);

CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;

CREATE TABLE public.suppliers (
    supplier_id integer NOT NULL,
    supplier_name character varying(100) NOT NULL,
    contact_name character varying(100),
    phone character varying(20),
    email character varying(100)
);

CREATE SEQUENCE public.suppliers_supplier_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.suppliers_supplier_id_seq OWNED BY public.suppliers.supplier_id;

CREATE TABLE public.materials (
    material_id integer NOT NULL,
    material_name character varying(100) NOT NULL,
    category character varying(50) NOT NULL,
    description text,
    quantity integer DEFAULT 0,
    unit character varying(20) NOT NULL,
    reorder_level integer DEFAULT 5,
    unit_cost numeric(10,2) NOT NULL,
    supplier_id integer,
    CONSTRAINT materials_category_check CHECK (((category)::text = ANY ((ARRAY['Cleaners'::character varying, 'Disinfectants'::character varying, 'Tools'::character varying, 'Safety'::character varying, 'Consumables'::character varying, 'Hygiene'::character varying])::text[]))),
    CONSTRAINT materials_quantity_check CHECK ((quantity >= 0)),
    CONSTRAINT materials_reorder_level_check CHECK ((reorder_level >= 0)),
    CONSTRAINT materials_unit_cost_check CHECK ((unit_cost >= (0)::numeric))
);

CREATE SEQUENCE public.materials_material_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.materials_material_id_seq OWNED BY public.materials.material_id;

CREATE TABLE public.cleaners (
    cleaner_id integer NOT NULL,
    employee_id character varying(20) NOT NULL,
    full_name character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    phone character varying(20),
    department character varying(50) NOT NULL,
    hire_date date NOT NULL,
    status character varying(15) DEFAULT 'active'::character varying,
    CONSTRAINT cleaners_status_check CHECK (((status)::text = ANY ((ARRAY['active'::character varying, 'inactive'::character varying])::text[])))
);

CREATE SEQUENCE public.cleaners_cleaner_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.cleaners_cleaner_id_seq OWNED BY public.cleaners.cleaner_id;

CREATE TABLE public.stock_issuances (
    issuance_id integer NOT NULL,
    issuance_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    material_id integer,
    cleaner_id integer,
    quantity_issued integer NOT NULL,
    issued_by_user_id integer,
    notes text,
    CONSTRAINT stock_issuances_quantity_issued_check CHECK ((quantity_issued > 0))
);

CREATE SEQUENCE public.stock_issuances_issuance_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.stock_issuances_issuance_id_seq OWNED BY public.stock_issuances.issuance_id;

-- ========================================
-- Default column values
-- ========================================

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);
ALTER TABLE ONLY public.suppliers ALTER COLUMN supplier_id SET DEFAULT nextval('public.suppliers_supplier_id_seq'::regclass);
ALTER TABLE ONLY public.materials ALTER COLUMN material_id SET DEFAULT nextval('public.materials_material_id_seq'::regclass);
ALTER TABLE ONLY public.cleaners ALTER COLUMN cleaner_id SET DEFAULT nextval('public.cleaners_cleaner_id_seq'::regclass);
ALTER TABLE ONLY public.stock_issuances ALTER COLUMN issuance_id SET DEFAULT nextval('public.stock_issuances_issuance_id_seq'::regclass);

-- ========================================
-- Seed data
-- ========================================

INSERT INTO public.users (user_id, full_name, username, email, password_hash, role) VALUES
(1, 'System Administrator', 'admin', 'owner@example.com', '179588aa09cff63f9230f9d80fb2d294159e3aababd22b3338e922a879f92eca', 'Owner'),
(2, 'Sarah Jenkins', 'sjenkins', 'cleaner@example.com', '179588aa09cff63f9230f9d80fb2d294159e3aababd22b3338e922a879f92eca', 'Cleaner'),
(3, 'John Doe', 'jdoe', 'storekeeper@example.com', '179588aa09cff63f9230f9d80fb2d294159e3aababd22b3338e922a879f92eca', 'Storekeeper'),
(4, 'Maria Santos', 'msantos', 'm.santos@university.edu', '179588aa09cff63f9230f9d80fb2d294159e3aababd22b3338e922a879f92eca', 'Cleaner'),
(5, 'James Wilson', 'jwilson', 'j.wilson@university.edu', '179588aa09cff63f9230f9d80fb2d294159e3aababd22b3338e922a879f92eca', 'Cleaner'),
(6, 'Patricia Garcia', 'pgarcia', 'p.garcia@university.edu', '179588aa09cff63f9230f9d80fb2d294159e3aababd22b3338e922a879f92eca', 'Cleaner'),
(7, 'Robert Johnson', 'rjohnson', 'r.johnson@university.edu', '179588aa09cff63f9230f9d80fb2d294159e3aababd22b3338e922a879f92eca', 'Cleaner'),
(8, 'Elena Rodriguez', 'erodriguez', 'e.rodriguez@university.edu', '179588aa09cff63f9230f9d80fb2d294159e3aababd22b3338e922a879f92eca', 'Cleaner'),
(9, 'David Chen', 'dchen', 'd.chen@university.edu', '179588aa09cff63f9230f9d80fb2d294159e3aababd22b3338e922a879f92eca', 'Cleaner');

INSERT INTO public.suppliers (supplier_id, supplier_name, contact_name, phone, email) VALUES
(1, 'CleanPro Supplies', 'Alice Carter', '(012) 019-8833', 'sales@cleanpro.com'),
(2, 'Janitorial Essentials', 'Bob Miller', '(012) 014-9922', 'orders@janitorialessentials.com'),
(3, 'EcoClean Solutions', 'Diana Prince', '(012) 017-7744', 'support@ecocleansolutions.com');

INSERT INTO public.materials (material_id, material_name, category, description, quantity, unit, reorder_level, unit_cost, supplier_id) VALUES
(1, 'All-Purpose Cleaner', 'Cleaners', 'Multi-surface cleaning solution', 45, 'liters', 20, 8.50, 1),
(2, 'Floor Disinfectant', 'Disinfectants', 'Hospital-grade floor disinfectant', 12, 'liters', 15, 12.00, 1),
(3, 'Microfiber Cloths', 'Tools', 'Reusable cleaning cloths (pack of 50)', 8, 'packs', 10, 25.00, 2),
(4, 'Disposable Gloves', 'Safety', 'Nitrile gloves (box of 100)', 35, 'boxes', 15, 18.00, 2),
(5, 'Glass Cleaner', 'Cleaners', 'Streak-free glass cleaning spray', 5, 'liters', 10, 9.50, 3),
(6, 'Mop Heads', 'Tools', 'Replacement mop heads (standard size)', 22, 'units', 8, 15.00, 2),
(7, 'Trash Bags (Large)', 'Consumables', 'Heavy-duty trash bags (roll of 50)', 60, 'rolls', 20, 22.00, 1),
(8, 'Hand Sanitizer', 'Hygiene', 'Alcohol-based hand sanitizer (5L)', 3, 'containers', 10, 35.00, 3);

INSERT INTO public.cleaners (cleaner_id, employee_id, full_name, email, phone, department, hire_date, status) VALUES
(1, 'EMP-001', 'Maria Santos', 'm.santos@university.edu', '(012) 111-2222', 'Science Building', '2022-03-15', 'active'),
(2, 'EMP-002', 'James Wilson', 'j.wilson@university.edu', '(012) 222-3333', 'Library', '2021-08-01', 'active'),
(3, 'EMP-003', 'Patricia Garcia', 'p.garcia@university.edu', '(012) 333-4444', 'Administration Building', '2023-01-10', 'active'),
(4, 'EMP-004', 'Robert Johnson', 'r.johnson@university.edu', '(012) 444-5555', 'Student Center', '2020-11-20', 'active'),
(5, 'EMP-005', 'Elena Rodriguez', 'e.rodriguez@university.edu', '(012) 555-6666', 'Science Building', '2019-05-12', 'inactive'),
(6, 'EMP-006', 'David Chen', 'd.chen@university.edu', '(012) 666-7777', 'Gymnasium', '2024-02-28', 'active'),
(7, 'EMP-007', 'Sarah Jenkins', 'cleaner@example.com', '(012) 777-8888', 'Science Building', '2021-03-15', 'active');

INSERT INTO public.stock_issuances (issuance_id, issuance_date, material_id, cleaner_id, quantity_issued, issued_by_user_id, notes) VALUES
(1, '2026-06-28 09:15:00', 1, 1, 5, 3, 'Weekly supply for Science Building'),
(2, '2026-06-27 11:30:00', 3, 2, 2, 3, 'Replace worn cloths'),
(3, '2026-06-26 14:00:00', 4, 3, 3, 3, 'Routine replenishment'),
(4, '2026-06-25 10:45:00', 2, 4, 4, 3, 'Deep cleaning request'),
(5, '2026-06-24 08:30:00', 7, 1, 3, 3, 'Regular stock replenishment'),
(6, '2026-06-23 13:10:00', 5, 2, 2, 3, 'Library window cleaning'),
(7, '2026-06-22 15:20:00', 6, 3, 4, 3, 'Administration floor upkeep'),
(8, '2026-06-21 09:05:00', 8, 4, 2, 3, 'Hand sanitation stations replenishment'),
(9, '2026-06-20 10:00:00', 6, 5, 2, 3, 'Mop head replacement for Science Building'),
(10, '2026-06-19 14:30:00', 1, 7, 3, 3, 'Weekly supply restock'),
(11, '2026-06-18 08:45:00', 2, 1, 2, 2, 'Floor disinfectant top-up'),
(12, '2026-06-17 11:15:00', 7, 2, 2, 2, 'Trash bag restock for Library'),
(13, '2026-06-16 13:40:00', 1, 3, 4, 3, 'General cleaning supplies'),
(14, '2026-06-15 09:50:00', 3, 4, 1, 3, 'Microfiber cloth replacement'),
(15, '2026-06-14 10:20:00', 4, 5, 2, 3, 'Gloves for lab cleanup'),
(16, '2026-06-13 15:00:00', 5, 7, 1, 2, 'Glass cleaner for windows'),
(17, '2026-06-12 08:10:00', 8, 1, 1, 3, 'Hand sanitizer refill'),
(18, '2026-06-11 12:30:00', 6, 2, 3, 3, 'Mop head replacement for Library'),
(19, '2026-06-10 09:35:00', 7, 3, 2, 3, 'Trash bag restock'),
(20, '2026-06-09 14:05:00', 2, 4, 2, 3, 'Floor disinfectant for Student Center'),
(21, '2026-06-08 10:50:00', 1, 5, 3, 3, 'All-purpose cleaner restock'),
(22, '2026-06-07 13:20:00', 3, 7, 2, 2, 'Microfiber cloths for Science Building');

-- ========================================
-- Sequence values
-- ========================================

SELECT pg_catalog.setval('public.users_user_id_seq', 9, true);
SELECT pg_catalog.setval('public.suppliers_supplier_id_seq', 3, true);
SELECT pg_catalog.setval('public.materials_material_id_seq', 8, true);
SELECT pg_catalog.setval('public.cleaners_cleaner_id_seq', 7, true);
SELECT pg_catalog.setval('public.stock_issuances_issuance_id_seq', 22, true);

-- ========================================
-- Constraints
-- ========================================

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);
ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);
ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);

ALTER TABLE ONLY public.suppliers
    ADD CONSTRAINT suppliers_pkey PRIMARY KEY (supplier_id);
ALTER TABLE ONLY public.suppliers
    ADD CONSTRAINT suppliers_email_key UNIQUE (email);

ALTER TABLE ONLY public.materials
    ADD CONSTRAINT materials_pkey PRIMARY KEY (material_id);

ALTER TABLE ONLY public.cleaners
    ADD CONSTRAINT cleaners_pkey PRIMARY KEY (cleaner_id);
ALTER TABLE ONLY public.cleaners
    ADD CONSTRAINT cleaners_employee_id_key UNIQUE (employee_id);
ALTER TABLE ONLY public.cleaners
    ADD CONSTRAINT cleaners_email_key UNIQUE (email);

ALTER TABLE ONLY public.stock_issuances
    ADD CONSTRAINT stock_issuances_pkey PRIMARY KEY (issuance_id);

-- ========================================
-- Foreign keys
-- ========================================

ALTER TABLE ONLY public.materials
    ADD CONSTRAINT materials_supplier_id_fkey FOREIGN KEY (supplier_id) REFERENCES public.suppliers(supplier_id) ON DELETE SET NULL;

ALTER TABLE ONLY public.stock_issuances
    ADD CONSTRAINT stock_issuances_material_id_fkey FOREIGN KEY (material_id) REFERENCES public.materials(material_id) ON DELETE RESTRICT;
ALTER TABLE ONLY public.stock_issuances
    ADD CONSTRAINT stock_issuances_cleaner_id_fkey FOREIGN KEY (cleaner_id) REFERENCES public.cleaners(cleaner_id) ON DELETE RESTRICT;
ALTER TABLE ONLY public.stock_issuances
    ADD CONSTRAINT stock_issuances_issued_by_user_id_fkey FOREIGN KEY (issued_by_user_id) REFERENCES public.users(user_id) ON DELETE SET NULL;