package com.payroll.app.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class EmployeeDeduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double lop;
    private Double pf;
    private Double loan;
    private Double totalDeduction;

    @ManyToOne
    @JoinColumn(name = "salary_id", nullable = false)
    private EmployeeSalary salary;

    public void calculateTotalDeduction() {
        this.totalDeduction =
                (lop == null ? 0 : lop) +
                (pf == null ? 0 : pf) +
                (loan == null ? 0 : loan);
    }
}
