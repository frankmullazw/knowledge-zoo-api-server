package edu.monash.knowledgezoo.api.service;

import edu.monash.knowledgezoo.api.repository.ApkRepository;
import edu.monash.knowledgezoo.api.repository.entity.Apk;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class ApkService {

    private final ApkRepository apkRepository;

    public ApkService(ApkRepository apkRepository) {
        this.apkRepository = apkRepository;
    }

    @Transactional(readOnly = true)
    public Apk findBySha256name(String sha256name) {
        return apkRepository.findBySha256(sha256name);
    }

    @Transactional(readOnly = true)
    public Collection<Apk> findByNameLike(String name) {
        return apkRepository.findByNameLike(name);
    }
}
