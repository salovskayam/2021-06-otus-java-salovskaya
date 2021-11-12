package ru.otus.crm.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

@Entity
@Table(name = "phones")
public class Phone implements Cloneable {
    @Expose
    @Id
    @SequenceGenerator(name = "phones_seq", sequenceName = "phones_hibernate_sequence", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phones_seq")
    @Column(name = "id")
    private Long id;

    @Expose
    @Column(name = "client_phone")
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    public Phone() {
    }

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Phone(String number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "number='" + number + '\'' +
                '}';
    }

    @Override
    public Phone clone() {
        return new Phone(this.id, this.number);
    }
}
