/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.acme.quarkus.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

/**
 * Simple JavaBean domain object representing an owner.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
@Entity
@Table(name = "owners", indexes = @Index(name = "owners_last_name", columnList = "last_name"))
public class Owner extends Person {
  @Column(name = "address")
  @NotEmpty
  public String address;

  @Column(name = "city")
  @NotEmpty
  public String city;

  @Column(name = "telephone")
  @NotEmpty
  @Digits(fraction = 0, integer = 10)
  public String telephone;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
  public Set<Pet> pets = new HashSet<>();

  public List<Pet> getPets() {
    List<Pet> sortedPets = new ArrayList<>(pets);
    sortedPets.sort(Comparator.comparing(NamedEntity::getName));
    return Collections.unmodifiableList(sortedPets);
  }

  public void addPet(Pet pet) {
    if (pet.id == null) {
      this.pets.add(pet);
    }
    pet.owner = this;
  }

  /**
   * Return the Pet with the given name, or null if none found for this Owner.
   *
   * @param name to test
   * @return true if pet name is already in use
   */
  public Pet getPetWithName(String name) {
    return pets.stream()
        .filter(pet -> name.toLowerCase().equals(pet.name.toLowerCase()))
        .findFirst()
        .orElse(null);
  }
}
