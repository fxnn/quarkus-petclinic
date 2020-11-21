package com.github.acme.quarkus.petclinic.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.not;

import com.github.acme.quarkus.petclinic.model.Vet;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Map;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class VetResourceTest {

  @Test
  public void findByName__aVetFromList__success() {
    Map<String, Object> vet = given().get("/vets").path("[0]");

    given()
        .pathParam("name", vet.get("lastName"))
        .when()
        .get("/vets/by-name/{name}")
        .then()
        .statusCode(200)
        .body("firstName", is(vet.get("firstName")))
        .body("lastName", is(vet.get("lastName")));
  }

  @Test
  public void list__regularQuery__returnsVets() {
    given().when().get("/vets").then().statusCode(200).body(is(not(emptyCollectionOf(Vet.class))));
  }
}
