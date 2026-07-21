package model;

/**
 * Supplier model class representing a cleaning material supplier.
 * Mirrors the real "suppliers" table: supplier_id, supplier_name,
 * contact_name, phone, email. There is no address or status column.
 */
public class Supplier {
    private int supplierId;
    private String name;
    private String contactName;
    private String email;
    private String phone;

    /**
     * Constructor with all fields
     */
    public Supplier(int supplierId, String name, String contactName, String email, String phone) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactName = contactName;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Constructor without ID (for new suppliers)
     */
    public Supplier(String name, String contactName, String email, String phone) {
        this.name = name;
        this.contactName = contactName;
        this.email = email;
        this.phone = phone;
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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

    @Override
    public String toString() {
        return "Supplier{" +
                "supplierId=" + supplierId +
                ", name='" + name + '\'' +
                ", contactName='" + contactName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
