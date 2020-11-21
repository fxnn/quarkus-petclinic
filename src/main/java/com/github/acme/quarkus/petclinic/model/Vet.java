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

import java.util.*;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;

/**
 * Simple JavaBean domain object representing a veterinarian.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Arjen Poutsma
 */
@Entity
@Table(name = "vets")
public class Vet extends Person {

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "vet_specialties",
      joinColumns = @JoinColumn(name = "vet_id"),
      inverseJoinColumns = @JoinColumn(name = "specialty_id"))
  public Set<Specialty> specialties = new HashSet<>();

  @XmlElement
  public List<Specialty> getSpecialties() {
    List<Specialty> sortedSpecs = new ArrayList<>(specialties);
    sortedSpecs.sort(Comparator.comparing(NamedEntity::getName));
    return Collections.unmodifiableList(sortedSpecs);
  }

  public int getNrOfSpecialties() {
    return specialties.size();
  }

  public void addSpecialty(Specialty specialty) {
    if (specialty.id == null) {
      this.specialties.add(specialty);
    }
  }
}
