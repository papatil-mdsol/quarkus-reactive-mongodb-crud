package com.mdsol.audit.demo.controller;

import com.mdsol.audit.demo.entity.User;
import com.mdsol.audit.demo.service.UserService;
import io.smallrye.common.constraint.NotNull;
import io.smallrye.mutiny.Uni;
import java.util.List;
import java.util.UUID;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
