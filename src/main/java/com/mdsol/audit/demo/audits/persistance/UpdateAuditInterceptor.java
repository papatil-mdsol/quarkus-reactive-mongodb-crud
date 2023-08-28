package com.mdsol.audit.demo.audits.persistance;

import com.mdsol.audit.demo.audits.AuditField;
import com.mdsol.audit.demo.audits.AuditType;
import com.mdsol.audit.demo.audits.DiffCalculator;
import com.mdsol.audit.demo.audits.WhichChanged;
import com.mdsol.audit.demo.entity.Audit;
import com.mdsol.audit.demo.markers.Auditable;
import com.mdsol.audit.demo.repository.AuditRepository;
import io.smallrye.mutiny.tuples.Tuple2;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.jboss.logging.Logger;

@UpdateAudit
@Interceptor
@ApplicationScoped
@Priority(Interceptor.Priority.APPLICATION)
public class UpdateAuditInterceptor {
  @Inject
  AuditRepository auditRepository;

  @Inject
  Logger logger;

  @AroundInvoke
  public Object update(InvocationContext ctx) throws Exception {
    logger.info("Entering: " + ctx.getMethod().getName());

    Tuple2 auditables = getAuditables(ctx.getParameters());
    List<AuditField> auditFields = null;
    try {
      auditFields = DiffCalculator.diff(auditables.getItem1(), auditables.getItem2());
    } catch (Exception e) {
      logger.error("Exception : " + e);
    }


    Audit audit = new Audit(UUID.randomUUID().toString(), auditables.getItem1().getClass().getPackageName(), "Pankaj Patil",
      auditables.getItem1().getClass().getSimpleName(), new WhichChanged(AuditType.UPDATE, auditFields), "User Updated Using Annotation",
      new Date().toString());

    auditRepository.persist(audit).subscribe().asCompletionStage();


    logger.info("Auditables : " + auditables);
    try {
      Object result = ctx.proceed();
      logger.info("Exiting: " + ctx.getMethod().getName());


      return result;
    } catch (Exception e) {
      logger.error("Exception in method: " + ctx.getMethod().getName());
      throw e;
    }
  }

  private Tuple2<Auditable, Auditable> getAuditables(Object[] parameters) {
    Auditable original = (Auditable) parameters[1];
    Auditable updated = (Auditable) parameters[2];
    return Tuple2.of(original, updated);
  }
}

