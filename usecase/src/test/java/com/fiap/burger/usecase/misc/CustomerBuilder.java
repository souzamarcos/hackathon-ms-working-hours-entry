package com.fiap.burger.usecase.misc;

import com.fiap.burger.entity.customer.Customer;

public class CustomerBuilder {

    private String id = "1";

    private String cpf = "68203895077";

    private String email = "email@email.com";

    private String name = "Nome do Cliente";

    public CustomerBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public CustomerBuilder withCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public CustomerBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public CustomerBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public Customer build() {
        return new Customer(id, cpf, email, name);
    }

}
