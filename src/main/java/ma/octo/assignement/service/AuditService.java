package ma.octo.assignement.service;

public interface AuditService {
    void auditVirement(String message);
    void auditVersement(String message);
}
