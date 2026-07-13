/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import java.util.Date;
import java.sql.ResultSet;


/**
 *
 * @author BC-STUDENT
 */
public class ReportController {
    
    private int issueId;
    private int materialId;
    private int cleanerId;
    private int quantityIssuead;
    private Date issueDate;

    public ReportController(int issueId, int materialId, int cleanerId, int quantityIssuead, Date issueDate) {
        this.issueId = issueId;
        this.materialId = materialId;
        this.cleanerId = cleanerId;
        this.quantityIssuead = quantityIssuead;
        this.issueDate = issueDate;
    }
    
    
    
}
