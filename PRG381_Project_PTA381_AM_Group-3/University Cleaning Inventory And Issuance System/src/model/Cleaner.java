package model;

import java.time.LocalDate;

/**
 * Cleaner model class representing a cleaning staff member
 */
public class Cleaner {
    private int cleanerId;
    private String name;
    private String email;
    private String phone;
    private String department;
    private LocalDate hireDate;
    private String status;

    /**
     * Constructor with all fields
     */
    public Cleaner(int cleanerId, String name, String email, String phone, String department, LocalDate hireDate, String status) {
        this.cleanerId = cleanerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.hireDate = hireDate;
        this.status = status;
    }

    /**
     * Constructor without ID (for new cleaners)
     */
    public Cleaner(String name, String email, String phone, String department, LocalDate hireDate, String status) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.hireDate = hireDate;
        this.status = status;
    }

    // Getters and Setters
    public int getCleanerId() {
        return cleanerId;
    }

    public void setCleanerId(int cleanerId) {
        this.cleanerId = cleanerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Cleaner{" +
                "cleanerId=" + cleanerId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", department='" + department + '\'' +
                ", hireDate=" + hireDate +
                ", status='" + status + '\'' +
                '}';
    }
}
