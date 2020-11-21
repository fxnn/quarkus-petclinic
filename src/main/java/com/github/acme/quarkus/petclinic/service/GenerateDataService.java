package com.github.acme.quarkus.petclinic.service;

import static java.util.stream.Collectors.toList;

import com.github.acme.quarkus.petclinic.model.Owner;
import com.github.acme.quarkus.petclinic.model.Pet;
import com.github.acme.quarkus.petclinic.model.PetType;
import com.github.acme.quarkus.petclinic.model.Specialty;
import com.github.acme.quarkus.petclinic.model.Vet;
import com.github.acme.quarkus.petclinic.model.Visit;
import com.github.acme.quarkus.petclinic.repository.OwnerRepository;
import com.github.acme.quarkus.petclinic.repository.PetRepository;
import com.github.acme.quarkus.petclinic.repository.PetTypeRepository;
import com.github.acme.quarkus.petclinic.repository.SpecialtyRepository;
import com.github.acme.quarkus.petclinic.repository.VetRepository;
import com.github.acme.quarkus.petclinic.repository.VisitRepository;
import com.github.javafaker.Faker;
import io.quarkus.runtime.StartupEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class GenerateDataService {

  private static final Logger LOG = LoggerFactory.getLogger(GenerateDataService.class);

  final Faker faker = new Faker();

  @Inject VetRepository vetRepository;

  @Inject SpecialtyRepository specialtyRepository;

  @Inject PetTypeRepository petTypeRepository;

  @Inject OwnerRepository ownerRepository;

  @Inject PetRepository petRepository;

  @Inject VisitRepository visitRepository;

  void onStartup(@Observes StartupEvent event) {
    generateData();
  }

  @Transactional
  public void generateData() {
    StopWatch stopWatch = StopWatch.createStarted();

    var specialties =
        Stream.of("radiology", "surgery", "dentistry")
            .map(this::generateSpecialty)
            .collect(toList());
    specialtyRepository.persist(specialties);

    var types =
        Stream.of("cat", "dog", "lizard", "snake", "bird", "hamster")
            .map(this::generateType)
            .collect(toList());
    petTypeRepository.persist(types);

    vetRepository.persist(IntStream.range(0, 1000).mapToObj(idx -> generateVet(specialties)));

    var owners = IntStream.range(0, 10000).mapToObj(idx -> generateOwner()).collect(toList());
    ownerRepository.persist(owners);

    var pets =
        IntStream.range(0, 10000).mapToObj(idx -> generatePet(types, owners)).collect(toList());
    petRepository.persist(pets);

    visitRepository.persist(IntStream.range(0, 1000).mapToObj(idx -> generateVisit(pets)));

    LOG.info("Data generated in {}", stopWatch);
  }

  private Visit generateVisit(List<Pet> pets) {
    var visit = new Visit();
    visit.date =
        LocalDate.ofInstant(
            faker.date().past(365, TimeUnit.DAYS).toInstant(), ZoneId.systemDefault());
    visit.pet = randomElement(pets);
    visit.description = faker.medical().symptoms();
    return visit;
  }

  private Pet generatePet(List<PetType> types, List<Owner> owners) {
    var pet = new Pet();
    pet.name = faker.animal().name();
    pet.type = randomElement(types);
    pet.birthDate =
        LocalDate.ofInstant(faker.date().birthday().toInstant(), ZoneId.systemDefault());
    pet.owner = randomElement(owners);
    return pet;
  }

  private Owner generateOwner() {
    var owner = new Owner();
    owner.firstName = faker.name().firstName();
    owner.lastName = faker.name().lastName();
    owner.address = faker.address().streetAddressNumber();
    owner.city = faker.address().city();
    owner.telephone = faker.phoneNumber().phoneNumber();
    return owner;
  }

  private PetType generateType(String name) {
    var petType = new PetType();
    petType.name = name;
    return petType;
  }

  private Specialty generateSpecialty(String name) {
    var specialty = new Specialty();
    specialty.name = name;
    return specialty;
  }

  private Vet generateVet(List<Specialty> specialties) {
    var vet = new Vet();
    vet.firstName = faker.name().firstName();
    vet.lastName = faker.name().lastName();
    IntStream.range(0, 3).mapToObj(i -> randomElement(specialties)).forEach(vet::addSpecialty);
    return vet;
  }

  private <T> T randomElement(List<T> list) {
    return list.get(ThreadLocalRandom.current().nextInt(list.size()));
  }
}
