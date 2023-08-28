package com.mdsol.audit.demo.entity;

import com.mdsol.audit.demo.audits.WhichChanged;
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
@MongoEntity(collection = "audits", database = "users-management")
public class Audit implements Serializable {
  String uuid;
  String where;
  String who;
  String what;
  WhichChanged whichChanged;
  String why;
  String when;
}
