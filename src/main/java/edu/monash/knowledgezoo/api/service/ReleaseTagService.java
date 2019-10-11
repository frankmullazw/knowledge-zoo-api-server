package edu.monash.knowledgezoo.api.service;

import edu.monash.knowledgezoo.api.repository.ReleaseTagRepository;
import edu.monash.knowledgezoo.api.repository.entity.ReleaseTag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class ReleaseTagService {

    private final ReleaseTagRepository releaseTagRepo;

    public ReleaseTagService(ReleaseTagRepository releaseTagRepo) {
        this.releaseTagRepo = releaseTagRepo;
    }

    @Transactional(readOnly = true)
    public ReleaseTag findByName(String name) {
        return releaseTagRepo.findByName(name);
    }

    @Transactional(readOnly = true)
    public Iterable<ReleaseTag> findAll() {
        return releaseTagRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Collection<ReleaseTag> findByNameLike(String name) {
        return releaseTagRepo.findByNameLike(name);
    }
}
