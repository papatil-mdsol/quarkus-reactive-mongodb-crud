package com.mdsol.audit.demo.audits.persistance;

import com.mdsol.audit.demo.audits.AuditField;
import com.mdsol.audit.demo.audits.AuditType;
import com.mdsol.audit.demo.audits.DiffCalculator;
import com.mdsol.audit.demo.audits.WhichChanged;
import com.mdsol.audit.demo.entity.Audit;
import com.mdsol.audit.demo.markers.Auditable;
import com.mdsol.audit.demo.repository.AuditRepository;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import org.jboss.logging.Logger;

@CreateAudit
@Interceptor
@ApplicationScoped
@Priority(Interceptor.Priority.APPLICATION)
public class CreateAuditInterceptor {
  @Inject
  AuditRepository auditRepository;

  @Inject
  Logger logger;

  @AroundInvoke
  public Object audit(InvocationContext ctx) throws Exception {
    logger.info("Entering: " + ctx.getMethod().getName());


    Auditable auditable = getAuditable(ctx.getParameters());
    List<AuditField> auditFields = null;
    try {
      auditFields = DiffCalculator.diff(null, auditable);
    } catch (Exception e) {
      logger.error("Exception : " + e);
    }


    Audit audit = new Audit(UUID.randomUUID().toString(), auditable.getClass().getPackageName(), "Pankaj Patil",
      auditable.getClass().getSimpleName(), new WhichChanged(AuditType.CREATE, auditFields), "New User Added Using Annotation",
      new Date().toString());

    auditRepository.persist(audit).subscribe().asCompletionStage();


    logger.info("Auditable : " + auditable);
    try {
      Object result = ctx.proceed();
      logger.info("Exiting: " + ctx.getMethod().getName());


      return result;
    } catch (Exception e) {
      logger.error("Exception in method: " + ctx.getMethod().getName());
      throw e;
    }
  }

  private Auditable getAuditable(Object[] parameters) {
    for (Object param : parameters) {
      if (param instanceof Auditable) {
        return (Auditable) param;
      }
    }
    return null;
  }
}

