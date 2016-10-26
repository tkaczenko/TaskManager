package io.github.tkaczenko.taskmanager.models;

/**
 * Created by tkaczenko on 26.10.16.
 */

public class Employee {
    private int id;
    private int idDepartment;
    private int idPosition;
    private String lastName;
    private String midName;
    private String firstName;
    private Contact contact;

    public Employee(int id, int idDepartment, int idPosition, String lastName, String midName, String firstName) {
        this.id = id;
        this.idDepartment = idDepartment;
        this.idPosition = idPosition;
        this.lastName = lastName;
        this.midName = midName;
        this.firstName = firstName;
    }

    public class Contact {
        private int id;
        private String phoneNum;
        private String email;

        public Contact(int id, String phoneNum, String email) {
            this.id = id;
            this.phoneNum = phoneNum;
            this.email = email;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdDepartment() {
        return idDepartment;
    }

    public void setIdDepartment(int idDepartment) {
        this.idDepartment = idDepartment;
    }

    public int getIdPosition() {
        return idPosition;
    }

    public void setIdPosition(int idPosition) {
        this.idPosition = idPosition;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMidName() {
        return midName;
    }

    public void setMidName(String midName) {
        this.midName = midName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
