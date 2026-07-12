package models;

/**
 * Supplier model class representing a cleaning material supplier
 */
public class Supplier {
    private int supplierId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String status;

    /**
     * Constructor with all fields
     */
    public Supplier(int supplierId, String name, String email, String phone, String address, String status) {
        this.supplierId = supplierId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status;
    }

    /**
     * Constructor without ID (for new suppliers)
     */
    public Supplier(String name, String email, String phone, String address, String status) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status;
    }

    // Getters and Setters
    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "supplierId=" + supplierId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
