package com.mdsol.audit.demo.entity;

import com.mdsol.audit.demo.markers.Auditable;
import io.quarkus.mongodb.panache.common.MongoEntity;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@MongoEntity(collection = "users", database = "users-management")
public class User implements Serializable, Auditable {

  public String uuid;
  private String firstName;
  private String lastName;
  private String emailId;
  public String department;

  public User() {
  }

  public User(String uuid, String firstName,
    String lastName, String emailId, String department) {
    this.uuid = uuid;
    this.firstName = firstName;
    this.lastName = lastName;
    this.emailId = emailId;
    this.department = department;
  }
}
