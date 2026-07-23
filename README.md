# Sparkling Clean

**University Cleaning Inventory & Issuance System**
Belgium Campus iTversity, PRG381 (Programming 3(7)(8)1) Group Project, Group 3

Sparkling Clean is our Track B submission for the PRG381 Project. 

It's a Java Swing desktop app that lets university staff manage cleaning materials, suppliers, cleaners, and stock issuances. Built to meet the assignment's required modules (authentication, dashboard, CRUD, stock issuance, reports, and role-based access).

---
## Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
  - [Package Breakdown](#package-breakdown)
  - [How the UI fits together](#how-the-ui-fits-together)
- [Role-Based Access Control](#role-based-access-control)
  - [How role-based access is implemented](#how-role-based-access-is-implemented)
  - [Owner](#owner)
  - [Storekeeper](#storekeeper)
  - [Cleaner](#cleaner)
- [Login and Register Screenshots](#Login-And-Register-Screenshots)
- [Database Schema (ERD)](#database-schema-erd)
- [Security](#security)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Database Setup](#database-setup)
  - [Opening the Project in NetBeans](#opening-the-project-in-netbeans)
  - [Running the project](#running-the-project)
- [Resolving Errors](#resolving-errors)
  - [1. Adding the PostgreSQL JDBC Driver (By default it should be there)](#1-adding-the-postgresql-jdbc-driver-by-default-it-should-be-there)
  - [2. Fixing JDK Version Errors in NetBeans](#2-fixing-jdk-version-errors-in-netbeans)
  - [3. Troubleshooting Common Setup & Runtime Issues](#3-troubleshooting-common-setup--runtime-issues)
- [Team, Group 3](#team-group-3)
  - [Contributors & Code Attribution](#contributors--code-attribution)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java  |
| GUI | Java Swing (built with NetBeans GUI Builder) |
| Data Access | JDBC |
| Database | PostgreSQL 18 |
| IDE | Apache NetBeans 30 |
| JDK | JDK 26 |
| Operating System | Windows (developed and tested on Windows) |

---

## Project Structure

```text
/PRG381_Project_PTA381_AM_Group-3
│   .gitignore
│   PRG381_Project_PTA381_AM_Group-3.iml
│   README.md
├── .idea
├── images
└── University Cleaning Inventory And Issuance System
    ├── build
    ├── database
    │       ERD.jpeg
    │       schema_with_mockdata.sql    
    ├── dist
    ├── lib
    ├── nbproject
    ├── src
    │   ├── controller
    │   │       AuthController.java
    │   │       CleanerController.java
    │   │       MaterialController.java
    │   │       MaterialDAOInterface.java
    │   │       StockIssuanceController.java
    │   │       SupplierController.java
    │   │       
    │   ├── dao
    │   │       CleanerDAO.java
    │   │       ReportsDAO.java
    │   │       StockIssuanceDAO.java
    │   │       SupplierDAO.java
    │   │       UserDAO.java
    │   │       
    │   ├── model
    │   │       Cleaner.java
    │   │       Material.java
    │   │       StockIssuance.java
    │   │       Supplier.java
    │   │       User.java
    │   │       
    │   ├── services
    │   │       CleanerService.java
    │   │       SupplierService.java
    │   │       
    │   ├── utils
    │   │       AlertUtils.java
    │   │       CurrentUser.java
    │   │       DBConnection.java
    │   │       PasswordUtil.java
    │   │       uiUtilities.java
    │   │       
    │   └── view
    │           LoginFrame.form
    │           LoginFrame.java
    │           MainFrame.form
    │           MainFrame.java
    │           RegisterFrame.form
    │           RegisterFrame.java
    │           
    │           ├── panels
    │           │       CleanersPnl.form
    │           │       CleanersPnl.java
    │           │       DashboardPnl.form
    │           │       DashboardPnl.java
    │           │       MaterialsPnl.form
    │           │       MaterialsPnl.java
    │           │       ReportsPnl.form
    │           │       ReportsPnl.java
    │           │       StockIssuancePnl.form
    │           │       StockIssuancePnl.java
    │           │       SuppliersPnl.form
    │           │       SuppliersPnl.java
    │           │       
    │           └── popUpDialogs
    │                   AddCleanersDialog.form
    │                   AddCleanersDialog.java
    │                   AddMaterialDialog.form
    │                   AddMaterialDialog.java
    │                   AddStockIssuanceDialog.form
    │                   AddStockIssuanceDialog.java
    │                   AddSuppliersDialog.form
    │                   AddSuppliersDialog.java
    │                   EditCleanersDialog.java
    │                   EditSuppliersDialog.java
    │                   
    └── test
```
### Package Breakdown

| Package / Folder | Purpose |
| --- | --- |
| `view` | Top-level Swing frames (`LoginFrame`, `RegisterFrame`, `MainFrame`) that manage application startup, authentication screens, and the main layout shell with sidebar navigation. |
| `view.panels` | Embedded `JPanel` components swapped inside `MainFrame`'s main layout (`DashboardPnl`, `MaterialsPnl`, `SuppliersPnl`, `CleanersPnl`, `StockIssuancePnl`, `ReportsPnl`). Each panel presents the actual screen for that page. |
| `view.popUpDialogs` | Modal GUI dialogs (`JDialog`) used for creating and editing records (`AddCleanersDialog`, `EditSuppliersDialog`, `AddStockIssuanceDialog`, etc.) without navigating away from the active panel. |
| `controller` | Application controllers containing business logic and intermediary validation between the presentation views (`view`) and data persistence layer (`dao`). |
| `dao` | Data Access Object layer (`CleanerDAO`, `SupplierDAO`, `ReportsDAO`, `UserDAO`, etc.) encapsulating raw SQL queries and database CRUD operations. |
| `model` | Model classes (`Cleaner`, `Material`, `Supplier`, `StockIssuance`, `User`) mapping database tables to structured Java objects. |
| `services` | Service layer components handling higher-level business rules and orchestration across DAO operations (e.g., `CleanerService`, `SupplierService`). |
| `utils` | Shared helper utilities: `DBConnection` for centralized JDBC connectivity, `PasswordUtil` for secure hashing, `CurrentUser` for session and role state tracking, and `AlertUtils` / `uiUtilities` for UI styling and notifications. |
| `database` | Storage for system database artifacts, including the ER Diagram (`ERD.jpeg`) and initialization scripts (`schema_with_mockdata.sql`). |
| `nbproject` | Auto-generated NetBeans project configuration and build metadata. |


### How the UI fits together

The application is built around three top-level `JFrame` components in `ui/`:

1. **`LoginFrame`**, the application's entry point (`main.class`). Handles staff login and links to registration.
2. **`RegisterFrame`**, handles new staff member registration.
3. **`MainFrame`**, loaded after a successful login. This is the main application window.

`MainFrame` is split into two areas:

- A left-hand sidebar with the logo, navigation buttons (Dashboard, Materials, Suppliers, Cleaners, Stock Issuance, Reports), and logged-in user info.
- A right-hand content area (`contentPanel`), a `CardLayout` container that swaps between the six `JPanel` forms in `ui/panels/`, depending on which sidebar button is clicked.

Each panel in `ui/panels/` is a self-contained `JPanel` responsible for one module's CRUD screen and logic (e.g. `MaterialsPnl` handles adding, editing, and deleting cleaning materials), which keeps `MainFrame` itself focused purely on layout and navigation.

---

## Role-Based Access Control

There are three roles: Owner, Storekeeper, and Cleaner. 

Which sidebar buttons appear and which page actions are enabled depend entirely on the role of the currently logged-in staff member.

> The mock data (loaded via the SQL script in [Database Setup](#database-setup)) includes a ready-to-use login for each role, see the credentials table in the [Running the project](#running-the-project) section to try each role out.

### How role-based access is implemented

`src/utils/CurrentUser.java` keeps track of whoever is currently logged in and offers simple yes/no checks for their role:

```java

public class CurrentUser {
    public static final String ROLE_CLEANER = "Cleaner";
    public static final String ROLE_STOREKEEPER = "Storekeeper";
    public static final String ROLE_OWNER = "Owner";
    private static User loggedInUser;

    private CurrentUser() {
    }

    public static void set(User user) {
        loggedInUser = user;
    }

    public static void clear() {
        loggedInUser = null;
    }

    public static User get() {
        return loggedInUser;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public static boolean hasRole(String role) {
        return loggedInUser != null && loggedInUser.getRole().equalsIgnoreCase(role);
    }

    public static boolean isCleaner() {
        return hasRole(ROLE_CLEANER);
    }

    public static boolean isStorekeeper() {
        return hasRole(ROLE_STOREKEEPER);
    }

    public static boolean isOwner() {
        return hasRole(ROLE_OWNER);
    }
}
```

Each panel uses these checks to decide what to show. There are two patterns depending on the page:

1. **Dashboard and Reports**, Owner and Storekeeper see exactly the same thing here (full access, no restrictions), so these two panels don't need any role checks at all. The Cleaner never sees these pages because the sidebar itself doesn't show Dashboard or Reports buttons for that role, so there's nothing to restrict once you're on them.

2. **Materials, Suppliers, Cleaners, Stock Issuance**, access differs between roles on these pages, so each of these panels calls its own `applyRoleRestrictions()` method right after `initComponents()` in its constructor. This method checks the role and then hides or disables whatever that role isn't allowed to use. For example, in `CleanersPnl`, only the Storekeeper can add, edit, or delete, so the Owner gets the "Add Cleaner" button hidden and the Edit/Delete table columns removed entirely:

```java
public CleanersPnl() {
    initComponents();
    applyRoleRestrictions();
}

private void applyRoleRestrictions() {
    boolean canEdit = CurrentUser.isStorekeeper();

    btnAddCleaner.setEnabled(canEdit);
    btnAddCleaner.setVisible(canEdit);

    if (!canEdit) {
        javax.swing.table.TableColumnModel columnModel = jTable1.getColumnModel();
        columnModel.removeColumn(jTable1.getColumn("Edit"));
        columnModel.removeColumn(jTable1.getColumn("Delete"));
    }
}
```

Every other CRUD panel follows this same type of pattern. A role never sees a control it isn't allowed to use: buttons are disabled and set to not visible, and columns are removed outright, not just greyed out.

> The screenshots under each role below show what these restrictions look like side by side, compare the same page across roles to see which buttons/columns disappear.

### Owner

Starting screen: **A blank pannel, user must choose a option to navigate to**

| Module | Access |
|---|---|
| Dashboard | Full view |
| Materials | View only |
| Suppliers | Create, Read, Update, Delete |
| Cleaners | View only |
| Stock Issuance | View only |
| Reports | Full access |

The Owner's sidebar shows all modules. Dashboard and Reports are identical to what the Storekeeper sees (no restrictions on either role there); the difference between the two roles shows up on Materials, Suppliers, Cleaners, and Stock Issuance, where the Owner is read-only everywhere except Suppliers.

**Dashboard**

![Owner Dashboard](images/role-based-access-control/owner/dashboard.png)


**Materials (View Only)**

![Owner Materials](images/role-based-access-control/owner/materials.png)


**Suppliers (Full CRUD)**

![Owner Suppliers](images/role-based-access-control/owner/suppliers.png)


#### Supplier Pop-ups

**Add Supplier (Create)**

![Add Supplier](images/role-based-access-control/owner/popups/supplier-create.png)


**Edit Supplier (Update)**

![Edit Supplier](images/role-based-access-control/owner/popups/suppliers-update.png)


**Delete Supplier (Delete)**

![Delete Supplier](images/role-based-access-control/owner/popups/suppliers-delete.png)


**Cleaners (View Only)**

![Owner Cleaners](images/role-based-access-control/owner/cleaners.png)


**Stock Issuance (View Only)**

![Owner Stock Issuance](images/role-based-access-control/owner/stock-issuance.png)


**Reports (Full Access)**

#### Inventory Report

![Owner Inventory Report](images/role-based-access-control/owner/reports-inventory.png)


#### Low Stock Report

![Owner Low Stock Report](images/role-based-access-control/owner/reports-low-stock.png)


#### Issuance Report

![Owner Issuance Report](images/role-based-access-control/owner/reports-issuance.png)


#### Material Report

![Owner Material Report](images/role-based-access-control/owner/reports-material.png)


### Storekeeper

Starting screen: **A blank pannel, user must choose a option to navigate to**

| Module | Access |
|---|---|
| Dashboard | Full view |
| Materials | Create, Read, Update, Delete |
| Suppliers | View only |
| Cleaners | Create, Read, Update, Delete |
| Stock Issuance | Create, Read, Update, Delete |
| Reports | Full access |

The Storekeeper has the widest day-to-day operational access. Dashboard and Reports are identical to what the Owner sees; the difference shows up on Materials, Suppliers, Cleaners, and Stock Issuance, where the Storekeeper has full CRUD everywhere except Suppliers.

**Dashboard**

![Storekeeper Dashboard](images/role-based-access-control/storekeeper/dashboard.png)


**Materials (Full CRUD)**

![Storekeeper Materials](images/role-based-access-control/storekeeper/materials.png)


#### Material Pop-ups

**Add Material (Create)**

![Add Material](images/role-based-access-control/storekeeper/popups/materials-create.png)


**Edit Material (Update)**

![Edit Material](images/role-based-access-control/storekeeper/popups/materials-update.png)


**Delete Material (Delete)**

![Delete Material](images/role-based-access-control/storekeeper/popups/materials-delete.png)


**Suppliers (View Only)**

![Storekeeper Suppliers](images/role-based-access-control/storekeeper/suppliers.png)


**Cleaners (Full CRUD)**

![Storekeeper Cleaners](images/role-based-access-control/storekeeper/cleaners.png)


#### Cleaner Pop-ups

**Add Cleaner (Create)**

![Add Cleaner](images/role-based-access-control/storekeeper/popups/cleaners-create.png)


**Edit Cleaner (Update)**

![Edit Cleaner](images/role-based-access-control/storekeeper/popups/cleaners-update.png)


**Delete Cleaner (Delete)**

![Delete Cleaner](images/role-based-access-control/storekeeper/popups/cleaners-delete.png)


**Stock Issuance (Full CRUD)**

![Storekeeper Stock Issuance](images/role-based-access-control/storekeeper/stock-issuance.png)


#### Stock Issuance Pop-up

**Issue Stock (Create)**

![Issue Stock](images/role-based-access-control/storekeeper/popups/stock-issuance-create.png)


**Edit Stock issuance (Update)**

![Edit Stock Issuance](images/role-based-access-control/storekeeper/popups/stock-issuance-update.png)


**Delete stock issuance (Delete)**

![Delete Stock Issuance](images/role-based-access-control/storekeeper/popups/stock-issuance-delete.png)


**Reports (Full Access)**

#### Inventory Report

![Storekeeper Inventory Report](images/role-based-access-control/storekeeper/reports-inventory.png)


#### Low Stock Report

![Storekeeper Low Stock Report](images/role-based-access-control/storekeeper/reports-low-stock.png)


#### Issuance Report

![Storekeeper Issuance Report](images/role-based-access-control/storekeeper/reports-issuance.png)


#### Material Report

![Storekeeper Material Report](images/role-based-access-control/storekeeper/reports-material.png)


### Cleaner

Starting screen: **A blank pannel, user must choose a option to navigate to**

| Module | Access |
|---|---|
| Materials | View only |
| Stock Issuance | Create, Read, Update, Delete, own entries only |

The Cleaner's sidebar is limited to just Materials and Stock Issuance. The Dashboard, Suppliers, Cleaners, and Reports buttons don't appear at all for this role, since a Cleaner has no need for them. Within Stock Issuance, Cleaners can only see and manage entries tied to their own account.

**Materials (View Only)**

![Cleaner Materials](images/role-based-access-control/cleaner/materials.png)


**Stock Issuance (CRUD, Own Entries Only)**

![Cleaner Stock Issuance](images/role-based-access-control/cleaner/stock-issue.png)


#### Stock Issuance Pop-up

**Issue Stock (Create)**

![Issue Stock](images/role-based-access-control/cleaner/popups/stock-issuance-create.png)


**Update Stock issuance (Update)**

![Edit Stock Issuance](images/role-based-access-control/cleaner/popups/stock-issuance-update.png)


**Delete stock issuance (Delete)**

![Delete Stock Issuance](images/role-based-access-control/cleaner/popups/stock-issuance-delete.png)

## Login And Register Screenshots

**Login Screen**

![Login Screen](images/screenshots/login.png)

**Register Screen**

![Register Screen](images/screenshots/register.png)

> Role-specific page screenshots (Dashboard, Materials, Suppliers, Cleaners, Stock Issuance, Reports) are shown under each role in the [Role-Based Access Control](#role-based-access-control) section above, since each role sees a different starting screen and a different level of access on shared pages.

---

## Database Schema (ERD)

The diagram below shows how the core tables (users, suppliers, materials, cleaners, stock_issuances) relate to one another.

![Entity Relationship Diagram](images/database-schema/ERD.jpeg)

---

## Security

Passwords are never stored in plaintext. When a staff member registers or logs in, their password is hashed using SHA-256 and stored, via `src/utils/PasswordUtil.java`:

- `PasswordUtil.hash(password)`, hashes a plaintext password into its SHA-256 hex digest before it is stored (on registration) or compared (on login).
- `PasswordUtil.verify(password, storedHash)`, re-hashes an entered password and compares it against the stored hash, returning `true`/`false` without ever decrypting anything (SHA-256 is one-way).

```java
public static String hash(String password) throws Exception {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
    return bytesToHex(encodedHash);
}

public static boolean verify(String password, String storedHash) throws Exception {
    return hash(password).equals(storedHash);
}
```

> Note: this implementation hashes without a salt, so it's a simpler approach that is easier to implement.

The seeded login passwords shown elsewhere in this README (e.g. `Password@1234`) are the plaintext passwords you type into the login screen, the database only ever stores their SHA-256 hash.

---

## Getting Started

### Prerequisites

- **Windows**, this project is developed and tested on Windows; other operating systems are not officially supported
- **JDK 26**
- **Apache NetBeans 30** (the project uses NetBeans project files, so NetBeans is recommended over other IDEs)
- **PostgreSQL 18** (latest stable release), this project is configured for a local PostgreSQL setup only (no remote/cloud database support), so make sure PostgreSQL is installed and running on your own machine
- The **PostgreSQL JDBC driver** (`.jar`), see [Adding the PostgreSQL JDBC Driver](#adding-the-postgresql-jdbc-driver) below

### Installation

1. Go to the repository's GitHub page.
2. Click the green **`Code`** button, then **`Download ZIP`** (this downloads the `main` branch).
3. Extract the downloaded ZIP file to a folder on your computer.
4. Continue with the Database Setup and Opening the Project in NetBeans sections below.

### Database Setup

> This project uses a local PostgreSQL setup only, it does not support connecting to a remote or cloud-hosted database. You must install PostgreSQL on your own machine and create the database locally before running the app.

1. Install PostgreSQL if you haven't already, and make sure the PostgreSQL service is running.
2. Open pgAdmin and connect to your local PostgreSQL server.
3. In the left-hand tree, right-click on **Databases**, then select **Create → Database...**
4. In the dialog that opens, set the Database name to `sparkling_clean` (matching the name expected in `DBConnection.java`, see step 6 below), then click **Save**.
5. Run the project's SQL script against the new database to create the tables and load the mock/seed data. (Replace `db/sparkling_clean.sql` below with the actual filename/path of the script included in the project.)

   - In pgAdmin: right-click the `sparkling_clean` database → **Query Tool**, then open the `.sql` file (**File → Open...** within the Query Tool) and click **Execute/Run**.
   - Alternatively, from a terminal:
     ```bash
     psql -U postgres -d sparkling_clean -f db/sparkling_clean.sql
     ```

   This single script creates all required tables (`staff`, `materials`, `suppliers`, `cleaners`, `stock_issuance`, etc.) and inserts the mock data used for testing (sample materials, suppliers, cleaners, and a default login for each role).

   Once the script has run, you can log in with one of these seeded accounts to explore each role:

   | Role | Email | Password |
   |---|---|---|
   | Owner | `owner@example.com` | `Password@1234` |
   | Storekeeper | `storekeeper@example.com` | `Password@1234` |
   | Cleaner | `cleaner@example.com` | `Password@1234` |

6. Edit `src/utils/DBConnection.java` to match your local PostgreSQL setup. This is the file the app uses to connect to the database, so it must reflect the database name, username, and password you actually created:

   ```java
   package utils;

   import java.sql.Connection;
   import java.sql.DriverManager;
   import java.sql.SQLException;

   public class DBConnection {
       // PostgreSQL JDBC driver class
       private static final String DRIVER = "org.postgresql.Driver";
       // JDBC URL to connect to the local PostgreSQL server + database
       private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/sparkling_clean";
       // PostgreSQL local username and password required to access PostgreSQL
       private static final String USER = "postgres";
       private static final String PASSWORD = "password";

       public DBConnection() {
       }

       // Create a connection to the database
       public static Connection getConnection() throws ClassNotFoundException {
           Connection con = null;
           try {
               // Dynamically load the JDBC driver class at runtime
               Class.forName(DRIVER);
               con = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
               if (con != null) {
                   System.out.println("Connected to database");
               }
           } catch (SQLException ex) {
               ex.printStackTrace();
           }
           return con;
       }
   }
   ```

   Update these three values to match what you configured in step 2:

   | Field | What to change it to |
   |---|---|
   | `JDBC_URL` | `jdbc:postgresql://localhost:<port>/<your_database_name>`, keep `localhost` since this project only supports a local database; just update the port (if you changed it during install) and the database name if it differs from `sparkling_clean` |
   | `USER` | The PostgreSQL username you connect with (default install often uses `postgres`) |
   | `PASSWORD` | The password you set for that PostgreSQL user during installation |

   > Without this step, the app will fail to connect and you'll see errors like `FATAL: password authentication failed` or `database "sparkling_clean" does not exist` when you try to log in.

### Opening the Project in NetBeans

1. Open Apache NetBeans.
2. Go to **File → Open Project...**
3. Browse to the folder where you extracted the ZIP file and select the project folder (the one containing `nbproject/` and `build.xml`).
4. Click **Open Project**.
5. Wait for NetBeans to index the project. If you see a red exclamation mark on the project name, see [Fixing JDK Version Errors](#fixing-jdk-version-errors-in-netbeans) below.

### Running the project

1. Make sure PostgreSQL is running and the database/tables/seed data are set up (see Database Setup above).
2. In NetBeans, right-click the project and select **Run**.
3. The `LoginFrame` should appear as the entry point, log in with one of the seeded accounts below (or register a new staff account) to continue:

   | Role | Email | Password |
   |---|---|---|
   | Owner | `owner@example.com` | `Password@1234` |
   | Storekeeper | `storekeeper@example.com` | `Password@1234` |
   | Cleaner | `cleaner@example.com` | `Password@1234` |

Here is the consolidated section merged under **Resolving Errors**, cleanly organized into logical subsections:

---

## Resolving Errors

### 1. Adding the PostgreSQL JDBC Driver (By default it should be there)

The project needs the PostgreSQL JDBC driver (`postgresql-<version>.jar`) on its classpath to connect to the database. This should allready be added and should not be needed but if the problem does occur here is what to do 

1. Download the driver `.jar` from [jdbc.postgresql.org](https://jdbc.postgresql.org/download/) if you don't already have it.
2. In NetBeans, expand the project and right-click on **Libraries**.
3. Select **Add JAR/Folder...**
4. Browse to and select the downloaded `postgresql-<version>.jar` file.
5. Click **Open**; the driver should now appear under Libraries in the project tree.
6. Clean and build the project to confirm it compiles (right-click the project → **Clean and Build**).

---

### 2. Fixing JDK Version Errors in NetBeans

This project targets JDK 26. If NetBeans shows the project with a red marker or "missing JDK" / "invalid source/target release" type errors, it usually means the JDK configured on your machine doesn't match JDK 26.

*What appears when the target JDK version or PostgreSQL JDBC driver is missing or misconfigured.*

1. Right-click the project → **Properties**.
2. Go to **Sources**, and check the **Source/Binary Format** field, noting the Java version listed.
3. Go to **Libraries → Java Platform**, and check which JDK is selected.
4. If the required JDK isn't installed or listed:
* Download and install the matching JDK version from [Adoptium](https://adoptium.net/) or [Oracle](https://www.oracle.com/java/technologies/downloads/).
* In NetBeans, go to **Tools → Java Platforms → Add Platform...** and point it to your new JDK installation folder.


5. Back in **Project Properties → Libraries**, select the correct Java Platform from the dropdown.
6. Apply changes, then **Clean and Build** the project again.

*Reference view of what your Libraries / Java Platform configuration should look like once setup is correct.*

---

### 3. Troubleshooting Common Setup & Runtime Issues

If something isn't working, check the reference screenshots below against what you are seeing—they cover the most common database setup and login authentication issues.

#### Database Setup Errors

* **Database Not Created in pgAdmin**
*What you'll see if the `sparkling_clean` database was never created in pgAdmin.*
![Consle output error](images/errors/Did-not-create-database-in-pgAdmin.png)


* **Database Not Populated**
*What you'll see if the database exists but the SQL script was never executed against it.*
![Consle output error](images/errors/Did-not-populate-the-database.png)



* **`DBConnection.java` Not Updated**
*What you'll see if `src/utils/DBConnection.java` still has default values instead of your own PostgreSQL credentials.*
![Consle output error](images/errors/Did-not-setup-DBConnectionFile-with-own-details.png)


---

#### Authentication & Login Errors

* **Invalid Password or Email Pop up**
*The error prompt displayed when attempting to log in with an unrecognized email or incorrect password.*
![Invalid Password error](images/errors/Incorrect login/Incorrect Password or email.png)

* **Failed Login Console Output**
*The console output when the login fails but doesn't throw an unhandled exception, confirming active DB connection.*
![Consle output error](images/errors/Incorrect login/Output-shows-no-errors.png)


---

## Team, Group 3
Here is the updated contributors list with the corresponding `.java` files assigned to each person's functional contributions based on the project structure:

---

### Contributors & Code Attribution

* **[Waldo Blom](https://github.com/Waldo-Blom)** - 578068
* **Contribution:**
* Created the initial UI shell & navigation for the entire application using NetBeans GUI Builder 
* Login and Register functionality (`LoginFrame.java`, `RegisterFrame.java`, `AuthController.java`, `UserDAO.java`)
* User authentication and session management (`PasswordUtil.java`, `CurrentUser.java`, `User.java`)
* Reports screen functionality implemented (`ReportsPnl.java`, `ReportsDAO.java`)


* **[Tobie Jansen van Vuuren](https://github.com/TobesOfCode)** - 602050
* **Contribution:**
* Dashboard screen functionality (`DashboardPnl.java`)
* Materials screen functionality (`MaterialsPnl.java`, `MaterialDAOInterface.java`, `Material.java`, `AddMaterialDialog.java`)


* **[Arnold Mabope](https://github.com/ArnoldDECODER)** - 603261
* **Contribution:**
* Suppliers screen functionality (`SuppliersPnl.java`, `SupplierController.java`, `SupplierService.java`, `SupplierDAO.java`, `Supplier.java`, `AddSuppliersDialog.java`, `EditSuppliersDialog.java`)
* Cleaners screen functionality (`CleanersPnl.java`, `CleanerController.java`, `CleanerService.java`, `CleanerDAO.java`, `Cleaner.java`, `AddCleanersDialog.java`, `EditCleanersDialog.java`)


* **[Jonathan Rossouw](https://github.com/Jonoefen)** - 601761
* **Contribution:**
* Database creation and architecture design 
* SQL schema creation script and mock data generation (`schema_with_mockdata.sql`, `ERD.jpeg`)
* Video presentation editing


* **[Lindokuhle Shangase](https://github.com/lindoB)** - 602395
* **Contribution:**
* Stock Issuance screen functionality (`StockIssuancePnl.java`, `StockIssuanceController.java`, `StockIssuanceDAO.java`, `StockIssuance.java`, `AddStockIssuanceDialog.java`, `MaterialController.java`)