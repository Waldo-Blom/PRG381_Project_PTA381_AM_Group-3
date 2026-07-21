--
-- PostgreSQL database dump
--

\restrict GmUGGWtw748YTUChXc13VDV7qvTbccxXcqHy7bSVWdndPrmbU9K4dLfQpsueoh9

-- Dumped from database version 18.4
-- Dumped by pg_dump version 18.4

-- Started on 2026-07-14 10:22:08

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 226 (class 1259 OID 16650)
-- Name: cleaners; Type: TABLE; Schema: public; Owner: postgres
--

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


ALTER TABLE public.cleaners OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16649)
-- Name: cleaners_cleaner_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cleaners_cleaner_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.cleaners_cleaner_id_seq OWNER TO postgres;

--
-- TOC entry 5078 (class 0 OID 0)
-- Dependencies: 225
-- Name: cleaners_cleaner_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.cleaners_cleaner_id_seq OWNED BY public.cleaners.cleaner_id;


--
-- TOC entry 224 (class 1259 OID 16625)
-- Name: materials; Type: TABLE; Schema: public; Owner: postgres
--

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


ALTER TABLE public.materials OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16624)
-- Name: materials_material_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.materials_material_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.materials_material_id_seq OWNER TO postgres;

--
-- TOC entry 5079 (class 0 OID 0)
-- Dependencies: 223
-- Name: materials_material_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.materials_material_id_seq OWNED BY public.materials.material_id;


--
-- TOC entry 228 (class 1259 OID 16669)
-- Name: stock_issuances; Type: TABLE; Schema: public; Owner: postgres
--

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


ALTER TABLE public.stock_issuances OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 16668)
-- Name: stock_issuances_issuance_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.stock_issuances_issuance_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.stock_issuances_issuance_id_seq OWNER TO postgres;

--
-- TOC entry 5080 (class 0 OID 0)
-- Dependencies: 227
-- Name: stock_issuances_issuance_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.stock_issuances_issuance_id_seq OWNED BY public.stock_issuances.issuance_id;


--
-- TOC entry 222 (class 1259 OID 16614)
-- Name: suppliers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.suppliers (
    supplier_id integer NOT NULL,
    supplier_name character varying(100) NOT NULL,
    contact_name character varying(100),
    phone character varying(20),
    email character varying(100)
);


ALTER TABLE public.suppliers OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16613)
-- Name: suppliers_supplier_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.suppliers_supplier_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.suppliers_supplier_id_seq OWNER TO postgres;

--
-- TOC entry 5081 (class 0 OID 0)
-- Dependencies: 221
-- Name: suppliers_supplier_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.suppliers_supplier_id_seq OWNED BY public.suppliers.supplier_id;


--
-- TOC entry 220 (class 1259 OID 16594)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id integer NOT NULL,
    full_name character varying(100) NOT NULL,
    username character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    password_hash character varying(255) NOT NULL,
    role character varying(20) NOT NULL,
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['Storekeeper'::character varying, 'Supervisor'::character varying, 'Owner'::character varying])::text[])))
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16593)
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_user_id_seq OWNER TO postgres;

--
-- TOC entry 5082 (class 0 OID 0)
-- Dependencies: 219
-- Name: users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_user_id_seq OWNED BY public.users.user_id;


--
-- TOC entry 4881 (class 2604 OID 16653)
-- Name: cleaners cleaner_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cleaners ALTER COLUMN cleaner_id SET DEFAULT nextval('public.cleaners_cleaner_id_seq'::regclass);


--
-- TOC entry 4878 (class 2604 OID 16628)
-- Name: materials material_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.materials ALTER COLUMN material_id SET DEFAULT nextval('public.materials_material_id_seq'::regclass);


--
-- TOC entry 4883 (class 2604 OID 16672)
-- Name: stock_issuances issuance_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_issuances ALTER COLUMN issuance_id SET DEFAULT nextval('public.stock_issuances_issuance_id_seq'::regclass);


--
-- TOC entry 4877 (class 2604 OID 16617)
-- Name: suppliers supplier_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suppliers ALTER COLUMN supplier_id SET DEFAULT nextval('public.suppliers_supplier_id_seq'::regclass);


--
-- TOC entry 4876 (class 2604 OID 16597)
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.users_user_id_seq'::regclass);


--
-- TOC entry 5070 (class 0 OID 16650)
-- Dependencies: 226
-- Data for Name: cleaners; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cleaners (cleaner_id, employee_id, full_name, email, phone, department, hire_date, status) FROM stdin;
1	EMP-001	Maria Santos	m.santos@university.edu	(555) 111-2222	Science Building	2022-03-15	active
2	EMP-002	James Wilson	j.wilson@university.edu	(555) 222-3333	Library	2021-08-01	active
3	EMP-003	Patricia Garcia	p.garcia@university.edu	(555) 333-4444	Administration Building	2023-01-10	active
4	EMP-004	Robert Johnson	r.johnson@university.edu	(555) 444-5555	Student Center	2020-11-20	active
5	EMP-005	Elena Rodriguez	e.rodriguez@university.edu	(555) 555-6666	Science Building	2019-05-12	inactive
6	EMP-006	David Chen	d.chen@university.edu	(555) 666-7777	Gymnasium	2024-02-28	active
\.


--
-- TOC entry 5068 (class 0 OID 16625)
-- Dependencies: 224
-- Data for Name: materials; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.materials (material_id, material_name, category, description, quantity, unit, reorder_level, unit_cost, supplier_id) FROM stdin;
1	All-Purpose Cleaner	Cleaners	Multi-surface cleaning solution	45	liters	20	8.50	1
2	Floor Disinfectant	Disinfectants	Hospital-grade floor disinfectant	12	liters	15	12.00	1
3	Microfiber Cloths	Tools	Reusable cleaning cloths (pack of 50)	8	packs	10	25.00	2
4	Disposable Gloves	Safety	Nitrile gloves (box of 100)	35	boxes	15	18.00	2
5	Glass Cleaner	Cleaners	Streak-free glass cleaning spray	5	liters	10	9.50	3
6	Mop Heads	Tools	Replacement mop heads (standard size)	22	units	8	15.00	2
7	Trash Bags (Large)	Consumables	Heavy-duty trash bags (roll of 50)	60	rolls	20	22.00	1
8	Hand Sanitizer	Hygiene	Alcohol-based hand sanitizer (5L)	3	containers	10	35.00	3
\.


--
-- TOC entry 5072 (class 0 OID 16669)
-- Dependencies: 228
-- Data for Name: stock_issuances; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.stock_issuances (issuance_id, issuance_date, material_id, cleaner_id, quantity_issued, issued_by_user_id, notes) FROM stdin;
1	2026-06-28 09:15:00	1	1	5	2	Weekly supply for Science Building
2	2026-06-27 11:30:00	3	2	2	2	Replace worn cloths
3	2026-06-26 14:00:00	4	3	3	2	Routine replenishment
4	2026-06-25 10:45:00	2	4	4	2	Deep cleaning request
5	2026-06-24 08:30:00	7	1	3	2	Regular stock replenishment
6	2026-06-23 13:10:00	5	2	2	2	Library window cleaning
7	2026-06-22 15:20:00	1	6	4	2	Gymnasium floor sanitization
8	2026-06-21 09:05:00	8	3	2	2	Hand sanitation stations replenishment
\.


--
-- TOC entry 5066 (class 0 OID 16614)
-- Dependencies: 222
-- Data for Name: suppliers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.suppliers (supplier_id, supplier_name, contact_name, phone, email) FROM stdin;
1	CleanPro Supplies	Alice Carter	(555) 019-8833	sales@cleanpro.com
2	Janitorial Essentials	Bob Miller	(555) 014-9922	orders@janitorialessentials.com
3	EcoClean Solutions	Diana Prince	(555) 017-7744	support@ecocleansolutions.com
\.


--
-- TOC entry 5064 (class 0 OID 16594)
-- Dependencies: 220
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (user_id, full_name, username, email, password_hash, role) FROM stdin;
1	System Administrator	admin	owner@example.com	179588aa09cff63f9230f9d80fb2d294159e3aababd22b3338e922a879f92eca	Owner
2	Sarah Jenkins	sjenkins	supervisor@example.com	179588aa09cff63f9230f9d80fb2d294159e3aababd22b3338e922a879f92eca	Supervisor
3	John Doe	jdoe	storekeeper@example.com	179588aa09cff63f9230f9d80fb2d294159e3aababd22b3338e922a879f92eca	Storekeeper
\.


--
-- TOC entry 5083 (class 0 OID 0)
-- Dependencies: 225
-- Name: cleaners_cleaner_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cleaners_cleaner_id_seq', 6, true);


--
-- TOC entry 5084 (class 0 OID 0)
-- Dependencies: 223
-- Name: materials_material_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.materials_material_id_seq', 8, true);


--
-- TOC entry 5085 (class 0 OID 0)
-- Dependencies: 227
-- Name: stock_issuances_issuance_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.stock_issuances_issuance_id_seq', 8, true);


--
-- TOC entry 5086 (class 0 OID 0)
-- Dependencies: 221
-- Name: suppliers_supplier_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.suppliers_supplier_id_seq', 3, true);


--
-- TOC entry 5087 (class 0 OID 0)
-- Dependencies: 219
-- Name: users_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_user_id_seq', 3, true);


--
-- TOC entry 4905 (class 2606 OID 16667)
-- Name: cleaners cleaners_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cleaners
    ADD CONSTRAINT cleaners_email_key UNIQUE (email);


--
-- TOC entry 4907 (class 2606 OID 16665)
-- Name: cleaners cleaners_employee_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cleaners
    ADD CONSTRAINT cleaners_employee_id_key UNIQUE (employee_id);


--
-- TOC entry 4909 (class 2606 OID 16663)
-- Name: cleaners cleaners_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cleaners
    ADD CONSTRAINT cleaners_pkey PRIMARY KEY (cleaner_id);


--
-- TOC entry 4903 (class 2606 OID 16643)
-- Name: materials materials_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.materials
    ADD CONSTRAINT materials_pkey PRIMARY KEY (material_id);


--
-- TOC entry 4911 (class 2606 OID 16680)
-- Name: stock_issuances stock_issuances_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_issuances
    ADD CONSTRAINT stock_issuances_pkey PRIMARY KEY (issuance_id);


--
-- TOC entry 4899 (class 2606 OID 16623)
-- Name: suppliers suppliers_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suppliers
    ADD CONSTRAINT suppliers_email_key UNIQUE (email);


--
-- TOC entry 4901 (class 2606 OID 16621)
-- Name: suppliers suppliers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suppliers
    ADD CONSTRAINT suppliers_pkey PRIMARY KEY (supplier_id);


--
-- TOC entry 4893 (class 2606 OID 16612)
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- TOC entry 4895 (class 2606 OID 16608)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 4897 (class 2606 OID 16610)
-- Name: users users_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);


--
-- TOC entry 4912 (class 2606 OID 16644)
-- Name: materials materials_supplier_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.materials
    ADD CONSTRAINT materials_supplier_id_fkey FOREIGN KEY (supplier_id) REFERENCES public.suppliers(supplier_id) ON DELETE SET NULL;


--
-- TOC entry 4913 (class 2606 OID 16686)
-- Name: stock_issuances stock_issuances_cleaner_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_issuances
    ADD CONSTRAINT stock_issuances_cleaner_id_fkey FOREIGN KEY (cleaner_id) REFERENCES public.cleaners(cleaner_id) ON DELETE RESTRICT;


--
-- TOC entry 4914 (class 2606 OID 16691)
-- Name: stock_issuances stock_issuances_issued_by_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_issuances
    ADD CONSTRAINT stock_issuances_issued_by_user_id_fkey FOREIGN KEY (issued_by_user_id) REFERENCES public.users(user_id) ON DELETE SET NULL;


--
-- TOC entry 4915 (class 2606 OID 16681)
-- Name: stock_issuances stock_issuances_material_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.stock_issuances
    ADD CONSTRAINT stock_issuances_material_id_fkey FOREIGN KEY (material_id) REFERENCES public.materials(material_id) ON DELETE RESTRICT;


-- Completed on 2026-07-14 10:22:09

--
-- PostgreSQL database dump complete
--

\unrestrict GmUGGWtw748YTUChXc13VDV7qvTbccxXcqHy7bSVWdndPrmbU9K4dLfQpsueoh9

