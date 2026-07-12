/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import model.User;
        

/**
 *
 * @author waldo
 */

/**
 * Tracks which staff member is currently logged into the application,
 * and provides role-based access control checks.
 *
 * For example:
 * Owner has access to:
 * - Dashboard (Full view)
 * - Materials (View Only)
 * - Suppliers (Create, Read, Update, Delete)
 * - Cleaners (View only)
 * - Stock Issuance (View Only)
 * - Reports (Full Access)
 * 
 * Storekeeper has access to:
 * - Dashboard (Full view)
 * - Materials (Create, Read, Update, Delete)
 * - Suppliers (View only)
 * - Cleaners (Create, Read, Update, Delete)
 * - Stock Issuance (Create, Read, Update, Delete)
 * - Reports (Full Access)
 * 
 * Cleaner has access to:
 * - Materials (View only)
 * - Stock Issuance (CRUD, own entries only)
 * 
 * So based on their access buttons are hidden and disabled on the dashboard. 
 * 
 */
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
