/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author waldo
 */

/**
 * Simple one-way password hashing using SHA-256.
 * Note: no salt, so it is not as secure but it is the simplest method as 
 * to not just store a plain string in the database
 */
public class PasswordUtil {
    
    //Credit to this hasing method: https://compile7.org/hashing/how-to-use-sha-256-in-java/#converting-hash-bytes-to-a-readable-format
    public static String hash(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1){
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
    
    //This is a method that I (Waldo Blom) wrote this is used as follows:
    //Hashing is used to not store the plain passoword in the database
    //Example hash: e91717aba47d8e99f64449583d2013c2e5373fd988f223a349bfa1faf5ac3551
    //Then if the user logs in the new hash needs to be compared to the stored hash
    public static boolean verify(String password, String storedHash) throws Exception {
        return hash(password).equals(storedHash);
    }
}