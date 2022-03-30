package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotBlank;

@Table("address")
public class Address {
    @Id
    private Long id;

    @Nonnull
    @NotBlank(message = "Please provide a client address.")
    private String street;

    @Nonnull
    private Long clientId;

    public Address() {
    }

    public Address(String street) {
        this(null, street, null);
    }

    public Address(String street, Long clientId) {
        this(null, street, clientId);
    }

    @PersistenceConstructor
    public Address(Long id, String street, Long clientId) {
        this.id = id;
        this.street = street;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", clientId=" + clientId +
                '}';
    }
}
