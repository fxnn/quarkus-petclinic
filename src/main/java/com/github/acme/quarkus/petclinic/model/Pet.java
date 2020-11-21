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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
@Entity
@Table(name = "pets", indexes = @Index(name = "pets_name", columnList = "name"))
public class Pet extends NamedEntity {

  @Column(name = "birth_date")
  public LocalDate birthDate;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "type_id", nullable = false)
  public PetType type;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "owner_id", nullable = false)
  public Owner owner;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.EAGER)
  public Set<Visit> visits = new LinkedHashSet<>();

  public List<Visit> getVisits() {
    List<Visit> sortedVisits = new ArrayList<>(visits);
    sortedVisits.sort(Comparator.comparing(Visit::getDate));
    return Collections.unmodifiableList(sortedVisits);
  }

  public void addVisit(Visit visit) {
    this.visits.add(visit);
    visit.pet = this;
  }
}
