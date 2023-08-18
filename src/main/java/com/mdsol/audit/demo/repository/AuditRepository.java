package com.mdsol.audit.demo.repository;

import com.mdsol.audit.demo.audits.AuditField;
import com.mdsol.audit.demo.audits.AuditType;
import com.mdsol.audit.demo.audits.DiffCalculator;
import com.mdsol.audit.demo.audits.WhichChanged;
import com.mdsol.audit.demo.audits.persistance.CreateAudit;
import com.mdsol.audit.demo.audits.persistance.DeleteAudit;
import com.mdsol.audit.demo.audits.persistance.UpdateAudit;
import com.mdsol.audit.demo.entity.Audit;
import com.mdsol.audit.demo.markers.Auditable;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Uni;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AuditRepository implements ReactivePanacheMongoRepository<Audit> {

  @Inject
  Logger logger;


  @CreateAudit
  public <T extends Auditable> Uni<T> insert(Uni<T> method, T auditable, String userName, String reason) {

    List<AuditField> auditFields = null;
    try {
      auditFields = DiffCalculator.diff(null, auditable);
    } catch (Exception e) {
      logger.error(e);
    }
    Audit audit = new Audit(UUID.randomUUID().toString(), auditable.getClass().getPackageName(), userName,
      auditable.getClass().getSimpleName(), new WhichChanged(AuditType.CREATE, auditFields), reason, new Date().toString());
    return persist(audit).chain(auditRecord -> method);
  }

  @DeleteAudit
  public <T extends Auditable> Uni<Long> delete(Uni<Long> method, T auditable, String userName, String reason) {

    List<AuditField> auditFields = null;
    try {
      auditFields = DiffCalculator.diff(auditable, null);
    } catch (Exception e) {
      logger.error(e);
    }
    Audit audit = new Audit(UUID.randomUUID().toString(), auditable.getClass().getPackageName(), userName,
      auditable.getClass().getSimpleName(), new WhichChanged(AuditType.DELETE, auditFields), reason, new Date().toString());
    return persist(audit).chain(auditRecord -> method);
  }

  @UpdateAudit
  public <T extends Auditable> Uni<T> update(Uni<T> method, T auditableOld, T auditableNew, String userName, String reason) {

    List<AuditField> auditFields = null;
    try {
      auditFields = DiffCalculator.diff(auditableOld, auditableNew);
    } catch (Exception e) {
      logger.error(e);
    }
    Audit audit = new Audit(UUID.randomUUID().toString(), auditableNew.getClass().getPackageName(), userName,
      auditableNew.getClass().getSimpleName(), new WhichChanged(AuditType.UPDATE, auditFields), reason, new Date().toString());
    return persist(audit).chain(auditRecord -> method);
  }
}
