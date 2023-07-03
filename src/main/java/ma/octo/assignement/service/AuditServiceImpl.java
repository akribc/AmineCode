package ma.octo.assignement.service;

import ma.octo.assignement.domain.Audit;
import ma.octo.assignement.domain.util.EventType;
import ma.octo.assignement.repository.AuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AuditServiceImpl implements AuditService {

    Logger log = LoggerFactory.getLogger(AuditServiceImpl.class);
    private final AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public void auditVirement(String message) {
        audit(EventType.VIREMENT, message);
    }

    @Override
    public void auditVersement(String message) {
        audit(EventType.VERSEMENT, message);
    }

    private void audit(EventType type, String message) {

        log.info("Audit de l'événement {}", type);

        Audit audit = new Audit();
        audit.setEventType(type);
        audit.setMessage(message);
        auditRepository.save(audit);
    }
}
