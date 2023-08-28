package com.mdsol.audit.demo.controller;

import com.mdsol.audit.demo.entity.User;
import com.mdsol.audit.demo.service.UserService;
import io.smallrye.common.constraint.NotNull;
import io.smallrye.mutiny.Uni;
import java.util.List;
import java.util.UUID;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class UserController {

  @Inject
  UserService userService;


  @POST
  public Uni<Response> addUser(User user) {
    user.setUuid(UUID.randomUUID().toString());
    return userService.addUser(user).map(addedUser -> Response.accepted(addedUser).build());
  }

  @PUT
  public Uni<Response> updateUser(User user) {
    return userService.updateUser(user).map(addedUser -> Response.accepted(addedUser).build());
  }

  @DELETE
  @Path("/{uuid}")
  public Uni<Response> deleteUser(@PathParam("uuid") String uuid) {
    return userService.deleteUser(uuid).map(count -> Response.accepted(count).build());
  }

  @GET
  public Uni<List<User>> getUsers() {
    return userService.getAllUsers();
  }

  @GET
  @Path("/{uuid}")
  public Uni<User> getUser(@NotNull @PathParam("uuid") String uuid) {
    return userService.getUser(uuid);
  }
}
