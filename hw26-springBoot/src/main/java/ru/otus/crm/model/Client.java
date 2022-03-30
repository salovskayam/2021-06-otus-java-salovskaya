package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Table("client")
public class Client {
    @Id
    private Long id;

    @Nonnull
    @NotBlank(message = "Please provide a client name.")
    private String name;

    @Valid
    @MappedCollection(idColumn = "client_id")
    private Address address;

    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones;

    @Transient
    @Valid
    @NotEmpty(message = "Please provide at least one client phone number.")
    private List<Phone> phonesAsList;

    public Client() {
    }

    public Client(String name, Address address, Set<Phone> phones) {
        this(null, name, address, phones);
    }

    @PersistenceConstructor
    public Client(Long id, String name, Address address, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public Set<Phone> getPhones() {
        if (phones == null) {
            phones = new HashSet<>();
        }
        return Collections.unmodifiableSet(phones);
    }

    public List<Phone> getPhonesAsList() {
        if (phonesAsList == null) {
            phonesAsList = new ArrayList<>();
        }
        return phonesAsList;
    }

    public void setPhonesAsList(List<Phone> phoneList) {
        this.phonesAsList = phoneList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    public void addPhone() {
        this.getPhonesAsList().add(new Phone());
    }

    public void removePhone(int index) {
        this.getPhonesAsList().remove(index);
    }

    public void addPhones() {
        this.setPhones(new HashSet<>(this.getPhonesAsList()));
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }
}
