package bank;
import java.time.LocalDate;

public abstract class Person {
    private String firstName;
    private String lastName;
    private String address;
    private LocalDate dob;  // Date of Birth

    public Person(String firstName, String lastName, String address, LocalDate dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.dob = dob;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getDob() {
        return dob;
    }

    public abstract void displayInfo();
}

