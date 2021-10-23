package com.itechart.studets_lab.book_library.model;

import java.time.LocalDate;
import java.util.Objects;

public class Reader {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String gender;
    private final int phoneNumber;
    private final LocalDate dateOfRegistration;

    public Reader(int id, String firstName, String lastName, String email, String gender, int phoneNumber, LocalDate dateOfRegistration) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.dateOfRegistration = dateOfRegistration;
    }

    static class ReaderBuilder {
        private int id;
        private String firstName;
        private String lastName;
        private String email;
        private String gender;
        private int phoneNumber;
        private LocalDate dateOfRegistration;

        public ReaderBuilder id(int id) {
            this.id = id;
            return this;
        }

        public ReaderBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ReaderBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ReaderBuilder email(String email) {
            this.email = email;
            return this;
        }

        public ReaderBuilder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public ReaderBuilder phoneNumber(int phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public ReaderBuilder dateOfRegistration(LocalDate dateOfRegistration) {
            this.dateOfRegistration = dateOfRegistration;
            return this;
        }

        public Reader build() {
            return new Reader(
                    this.id,
                    this.firstName,
                    this.lastName,
                    this.email,
                    this.gender,
                    this.phoneNumber,
                    this.dateOfRegistration);
        }
    }

    public int getId() {
        return id;
    }

    public static ReaderBuilder builder() {
        return new ReaderBuilder();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getDateOfRegistration() {
        return dateOfRegistration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return id == reader.id && phoneNumber == reader.phoneNumber && Objects.equals(firstName, reader.firstName) && Objects.equals(lastName, reader.lastName) && Objects.equals(email, reader.email) && Objects.equals(gender, reader.gender) && Objects.equals(dateOfRegistration, reader.dateOfRegistration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, gender, phoneNumber, dateOfRegistration);
    }

    @Override
    public String toString() {
        return "{" +
                "id: " + id +
                ", firstName: '" + firstName + '\'' +
                ", lastName: '" + lastName + '\'' +
                ", email: '" + email + '\'' +
                ", gender: '" + gender + '\'' +
                ", phoneNumber: " + phoneNumber +
                ", dateOfRegistration: " + dateOfRegistration +
                '}';
    }
}
