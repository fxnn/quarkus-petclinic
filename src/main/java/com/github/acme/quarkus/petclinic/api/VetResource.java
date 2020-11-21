package com.github.acme.quarkus.petclinic.api;

import com.github.acme.quarkus.petclinic.model.Vet;
import com.github.acme.quarkus.petclinic.repository.VetRepository;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/vets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VetResource {

  @Inject VetRepository vetRepository;

  @GET
  public List<Vet> list() {
    return vetRepository.listAllVets();
  }

  @GET
  @Path("/by-name/{name}")
  public Vet findByName(@PathParam("name") String name) {
    return vetRepository.findByName(name);
  }
}
