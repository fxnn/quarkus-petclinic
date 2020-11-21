package com.github.acme.quarkus.petclinic.repository;

import com.github.acme.quarkus.petclinic.model.Pet;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PetRepository implements PanacheRepository<Pet> {}
