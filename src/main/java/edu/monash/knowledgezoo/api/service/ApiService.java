package edu.monash.knowledgezoo.api.service;

import edu.monash.knowledgezoo.api.repository.ApiRepository;
import edu.monash.knowledgezoo.api.repository.entity.Api;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class ApiService {

    private final ApiRepository signatureRepository;

    public ApiService(ApiRepository signatureRepository) {
        this.signatureRepository = signatureRepository;
    }

    @Transactional(readOnly = true)
    public Api findByName(String name) {
        return signatureRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Collection<Api> findByNameLike(String name) {
        return signatureRepository.findByNameLike(name);
    }
}
