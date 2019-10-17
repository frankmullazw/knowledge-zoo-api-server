package edu.monash.knowledgezoo.api.service;

import edu.monash.knowledgezoo.api.repository.PermissionRepository;
import edu.monash.knowledgezoo.api.repository.entity.Permission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Transactional(readOnly = true)
    public long getTotalNumberOfPermissions() {
        return permissionRepository.count();
    }

    @Transactional(readOnly = true)
    public Collection<Permission> getTop10Permissions() {
        return permissionRepository.getTop10Permissions();
    }

    @Transactional(readOnly = true)
    public Permission findByGenericNameLike(String name) {
        return permissionRepository.findFirstByGenericName(name);
    }

    @Transactional(readOnly = true)
    public List<String> getAllPermissionName() { return permissionRepository.getAllPermissionName(); }
}
