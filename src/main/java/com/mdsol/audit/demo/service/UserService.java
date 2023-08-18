package com.mdsol.audit.demo.service;

import com.mdsol.audit.demo.entity.User;
import com.mdsol.audit.demo.repository.AuditRepository;
import com.mdsol.audit.demo.repository.UserRepository;
import io.smallrye.mutiny.Uni;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.bson.Document;

@ApplicationScoped
public class UserService {

  @Inject
  UserRepository userRepository;

  @Inject
  AuditRepository auditRepository;


  public Uni<User> addUser(User user) {
    return auditRepository.insert(userRepository.persist(user), user, "Pankaj Patil", "User Added");
  }


  public Uni<User> updateUser(User user) {
    Uni<User> oldUserUni = userRepository.find(new Document("uuid", user.getUuid())).firstResult();
    return oldUserUni.chain(oldUser -> auditRepository.update(userRepository.persist(user), oldUser, user, "Pankaj Patil", "User Updated"));
  }


  public Uni<Long> deleteUser(String uuid) {
    Uni<User> existingUserUni = userRepository.find(new Document("uuid", uuid)).firstResult();
    return existingUserUni.chain(existingUser -> auditRepository.delete(userRepository.delete(new Document("uuid",
      existingUser.getUuid())), existingUser, "Pankaj " +
      "Patil", "User Deleted"));
  }

  public Uni<User> getUser(String uuid) {
    return userRepository.find(new Document("uuid", uuid)).firstResult();
  }

  public Uni<List<User>> getAllUsers() {
    return userRepository.streamAll().collect().asList();
  }
}
