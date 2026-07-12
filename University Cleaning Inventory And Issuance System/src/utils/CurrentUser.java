/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

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
 * - Stock Issuance (CRUD — own entries only)
 * 
 * So based on their access buttons are hidden and disabled on the dashboard. 
 * 
 */
public class CurrentUser {
    
}
