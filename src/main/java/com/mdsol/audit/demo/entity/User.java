package com.mdsol.audit.demo.entity;

import com.mdsol.audit.demo.markers.Auditable;
import io.quarkus.mongodb.panache.common.MongoEntity;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@MongoEntity(collection = "users", database = "users-management")
public class User implements Serializable, Auditable {
  public String uuid;
  private String firstName;
  private String lastName;
  private String emailId;
  public String department;
}
