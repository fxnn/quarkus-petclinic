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

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Simple JavaBean domain object representing a visit.
 *
 * @author Ken Krebs
 * @author Dave Syer
 */
@Entity
@Table(name = "visits", indexes = @Index(name = "visits_pet_id", columnList = "pet_id"))
public class Visit extends PanacheEntity {

  @Column(name = "visit_date")
  public LocalDate date;

  @NotEmpty
  @Column(name = "description")
  public String description;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "pet_id", nullable = false)
  public Pet pet;

  /** Creates a new instance of Visit for the current date */
  public Visit() {
    this.date = LocalDate.now();
  }

  public LocalDate getDate() {
    return date;
  }
}
