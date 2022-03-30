package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Table("phones")
public class Phone {
    @Id
    private Long id;

    @Nonnull
    @NotBlank(message = "Please provide a client phone number.")
    private String clientPhone;

    @Nonnull
    private Long clientId;

    public Phone() {
    }

    public Phone(String clientPhone) {
        this(null, clientPhone, null);
    }

    public Phone(String clientPhone, Long clientId) {
        this(null, clientPhone, clientId);
    }

    @PersistenceConstructor
    public Phone(Long id, String clientPhone, Long clientId) {
        this.id = id;
        this.clientPhone = clientPhone;
        this.clientId = clientId;
    }

    public Long getId() {
        return id;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    @Override
    public String toString() {
        return clientPhone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return clientPhone.equals(phone.clientPhone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientPhone);
    }
}
