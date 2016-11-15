package io.github.tkaczenko.taskmanager.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.github.tkaczenko.taskmanager.database.model.dictionary.Department;
import io.github.tkaczenko.taskmanager.database.model.dictionary.Position;

/**
 * Created by tkaczenko on 26.10.16.
 */

public class Employee implements Parcelable {
    private int id;
    private Department department;
    private Position position;
    private String lastName;
    private String midName;
    private String firstName;
    private Contact contact;

    public Employee() {

    }

    public Employee(Parcel in) {
        this.id = in.readInt();
        this.department = in.readParcelable(Department.class.getClassLoader());
        this.position = in.readParcelable(Position.class.getClassLoader());
        this.lastName = in.readString();
        this.midName = in.readString();
        this.firstName = in.readString();
        this.contact = in.readParcelable(Contact.class.getClassLoader());
    }

    public Employee(int id, Department department, Position position, String lastName, String midName, String firstName) {
        this.id = id;
        this.department= department;
        this.position = position;
        this.lastName = lastName;
        this.midName = midName;
        this.firstName = firstName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeParcelable(getDepartment(), flags);
        dest.writeParcelable(getPosition(), flags);
        dest.writeString(lastName);
        dest.writeString(midName);
        dest.writeString(firstName);
        dest.writeParcelable(getContact(), flags);
    }

    public static final Parcelable.Creator<Employee> CREATOR = new Parcelable.Creator<Employee>() {
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public class Contact implements Parcelable {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(getId());
            dest.writeString(getPhoneNum());
            dest.writeString(getEmail());
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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
