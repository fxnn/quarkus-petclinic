package com.github.acme.quarkus.petclinic.repository;

import com.github.acme.quarkus.petclinic.model.Owner;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OwnerRepository implements PanacheRepository<Owner> {}
