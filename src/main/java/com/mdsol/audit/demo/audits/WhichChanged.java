package com.mdsol.audit.demo.audits;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true, fluent = false)
public class WhichChanged implements Serializable {
  AuditType auditType;
  List<AuditField> auditFields;

  public WhichChanged() {
  }

  public WhichChanged(AuditType auditType, List<AuditField> auditFields) {
    this.auditType = auditType;
    this.auditFields = auditFields;
  }
}
