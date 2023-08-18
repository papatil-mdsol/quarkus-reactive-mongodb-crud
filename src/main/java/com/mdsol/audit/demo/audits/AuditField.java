package com.mdsol.audit.demo.audits;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuditField implements Serializable {
  private String fieldName;
  private String oldValue;
  private String newValue;

  public AuditField() {
  }

  public AuditField(String fieldName, String oldValue, String newValue) {
    this.fieldName = fieldName;
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  @Override
  public String toString() {
    return "{" +
      "'fieldName':'" + fieldName + '\'' +
      ", 'oldValue':'" + oldValue + '\'' +
      ", 'newValue':'" + newValue + '\'' +
      '}';
  }
}
