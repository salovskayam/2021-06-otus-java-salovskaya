package ru.otus.crm.model;


import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_seq", sequenceName = "client_hibernate_sequence", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_seq")
    @Column(name = "client_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Address address;

    //owning side - Phone
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Phone> phones = new ArrayList<>();

    public Client() {
    }

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Client clone() {
        Client newClient = new Client(this.id, this.name);
        Address newAddress = this.address.clone();
        List<Phone> newPhones = this.phones.stream().map(Phone::clone)
            .collect(Collectors.toUnmodifiableList());
        newClient.setAddress(newAddress);
        newPhones.forEach(newClient::addPhone);
        return newClient;
    }

    public void addPhone(Phone phone) {
        this.phones.add(phone);
        phone.setClient(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(Address address) {
        if (address == null) {
            if (this.address != null) {
                this.address.setClient(null);
            }
        } else {
            address.setClient(this);
        }
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address="  + address +
                ", phones=" + phones +
                '}';
    }
}
