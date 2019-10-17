package edu.monash.knowledgezoo.api.service;

import edu.monash.knowledgezoo.api.repository.ApiRepository;
import edu.monash.knowledgezoo.api.repository.entity.Api;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.security.acl.AllPermissionsImpl;

import java.util.Collection;

@Service
public class ApiService {

    private final ApiRepository apiRepository;

    public ApiService(ApiRepository signatureRepository) {
        this.apiRepository = signatureRepository;
    }

    @Transactional(readOnly = true)
    public Api findByName(String name) {
        return apiRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Api findByNameLike(String name) {
        return apiRepository.findByNameLike(name);
    }

    @Transactional(readOnly = true)
    public long getTotalNumberOfApis() {
        return apiRepository.count();
    }

    @Transactional(readOnly = true)
    public Collection<Api> getTop10Apis() {
        return apiRepository.getTop10Apis();
    }
}
