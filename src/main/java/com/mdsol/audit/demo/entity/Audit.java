package com.mdsol.audit.demo.entity;

import com.mdsol.audit.demo.audits.WhichChanged;
import io.quarkus.mongodb.panache.common.MongoEntity;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@MongoEntity(collection = "audits", database = "users-management")
public class Audit implements Serializable {

  String uuid;
  String where;
  String who;
  String what;
  WhichChanged whichChanged;
  String why;
  String when;

  public Audit() {
  }

  public Audit(String uuid, String where, String who, String what, WhichChanged whichChanged, String why, String when) {
    this.uuid = uuid;
    this.where = where;
    this.who = who;
    this.what = what;
    this.whichChanged = whichChanged;
    this.why = why;
    this.when = when;
  }

}
