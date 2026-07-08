# Sparkling Clean

**University Cleaning Inventory & Issuance System**
Belgium Campus iTversity — PRG381 (Programming 3(7)(8)1) Group Project | Group 3

Sparkling Clean is a Java desktop application (Track B) built to help university staff manage cleaning materials, suppliers, cleaners, and stock issuances through a single, easy-to-use interface.

---

## Overview

The system is built as a **Java Swing desktop application** using Core Java, JDBC, and a relational database, following Object-Oriented Programming principles. It allows a Storekeeper/Supervisor to:

- Log in / register staff securely
- View a dashboard with live stock and activity stats
- Manage cleaning materials, suppliers, and cleaners (full CRUD)
- Issue stock to cleaners, with automatic stock deduction and validation
- Generate inventory, low-stock, and usage reports
- Search and filter records across the system

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java (Core Java, OOP) |
| GUI | Java Swing (built with NetBeans GUI Builder) |
| Data Access | JDBC |
| Database | Relational database (PostgreSQL / Derby / JavaDB) |
| IDE | Apache NetBeans |

---

## Project Structure

```
University Cleaning Inventory And Issuance System/
├── src/
│   └── ui/
│       ├── LoginFrame.java / .form       # Login screen (entry point)
│       ├── RegisterFrame.java / .form    # Staff registration screen
│       ├── MainFrame.java / .form        # Main application shell (post-login)
│       │
│       └── panels/                       # Content panels shown inside MainFrame
│           ├── DashboardPnl.java / .form
│           ├── MaterialsPnl.java / .form
│           ├── SuppliersPnl.java / .form
│           ├── CleanersPnl.java / .form
│           ├── StockIssuancePnl.java / .form
│           └── ReportsPnl.java / .form
│
├── nbproject/                            # NetBeans project configuration
├── build.xml                             # Ant build script
└── manifest.mf
```

### How the UI fits together

The application is built around **three top-level `JFrame` components** in `ui/`:

1. **`LoginFrame`** — the application's entry point (`main.class`). Handles staff login and links to registration.
2. **`RegisterFrame`** — handles new staff member registration.
3. **`MainFrame`** — loaded after a successful login. This is the main application window.

`MainFrame` is split into two areas:

- A **left-hand sidebar** — logo, navigation buttons (Dashboard, Materials, Suppliers, Cleaners, Stock Issuance, Reports), and logged-in user info.
- A **right-hand content area** (`contentPanel`) — a `CardLayout` container that swaps between the six `JPanel` forms in `ui/panels/`, depending on which sidebar button is clicked:

Each panel in `ui/panels/` is a self-contained `JPanel` responsible for one module's CRUD screen and logic (e.g. `MaterialsPnl` handles adding/editing/deleting cleaning materials), keeping `MainFrame` itself focused purely on layout and navigation.

### Role-based navigation

On login, the user's role is detected from their login details, and the sidebar in `MainFrame` displays only the menu options relevant to that role. There are **three roles**: Owner, Storekeeper, and Cleaner.

**Owner**
The role of the owner is ....
-
-
-

**Storekeeper**
The role of the storekeeper is ....
-
-
-

**Cleaner**
The role of the cleaner is ....
-
-
-

---

## Core Features

- **User Authentication** — registration, login, logout, password validation
- **Dashboard** — total materials, low-stock items, total cleaners, recent issuances
- **Materials Management** — add/view/update/delete materials, quantities, reorder levels
- **Suppliers Management** — full CRUD with contact details
- **Cleaners Management** — full CRUD, optional department assignment
- **Stock Issuance** — issue materials to cleaners, auto stock deduction, prevents over-issuing, keeps issuance history
- **Reports** — inventory, low-stock, issuance history, material usage
- **Validation & Business Rules** — duplicate prevention, required fields, no negative stock, meaningful error messages

---

## Screenshots

*Screenshots to be added.*

**Login Screen**

![Login Screen](screenshots/login.png)

**Register Screen**

![Register Screen](screenshots/register.png)

**Dashboard**

![Dashboard](screenshots/dashboard.png)

**Materials Management**

![Materials Management](screenshots/materials.png)

**Suppliers Management**

![Suppliers Management](screenshots/suppliers.png)

**Cleaners Management**

![Cleaners Management](screenshots/cleaners.png)

**Stock Issuance**

![Stock Issuance](screenshots/stock-issuance.png)

**Reports**

![Reports](screenshots/reports.png)

---

## Getting Started

### Prerequisites
- Java JDK ??
- Apache NetBeans IDE (recommended, project uses NetBeans project files)
- A relational database (PostgreSQL or Derby/JavaDB)

### Running the project

---

## Team — Group 3

- [Waldo Blom](https://github.com/Waldo-Blom) — 578068
- [Tobie Jansen van Vuuren](https://github.com/TobesOfCode) — Student Number
- [Arnold Mabope](https://github.com/ArnoldDECODER) — Student Number
- [Jonathan Rossouw](https://github.com/Jonoefen) — Student Number
- [Lindokuhle Shangase](https://github.com/lindoB) — Student Number

---

## Project Brief

This project is submitted for the PRG381 Programming 3(7)(8)1 Project assessment (Track B: Desktop Application), Belgium Campus iTversity.
