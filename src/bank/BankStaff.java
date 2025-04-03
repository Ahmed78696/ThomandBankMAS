// BankStaff.java
package bank;

import java.time.LocalDate;

public abstract class BankStaff extends Person {
    private int empNo;
    private String jobTitle;

    public BankStaff(String firstName, String lastName, String address, LocalDate dob, int empNo, String jobTitle) {
        super(firstName, lastName, address, dob);
        this.empNo = empNo;
        this.jobTitle = jobTitle;
    }

    public int getEmpNo() {
        return empNo;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    @Override
    public void displayInfo() {
        System.out.println("Staff Info: " + getFirstName() + " " + getLastName() + ", " + getJobTitle() + ", DOB: " + getDob());
    }
}
