package edu.monash.knowledgezoo.api.service;

import edu.monash.knowledgezoo.api.repository.ReleaseTagRepository;
import edu.monash.knowledgezoo.api.repository.entity.ReleaseTag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class ReleaseTagService {

    private final ReleaseTagRepository releaseTagRepository;

    public ReleaseTagService(ReleaseTagRepository releaseTagRepository) {
        this.releaseTagRepository = releaseTagRepository;
    }

    @Transactional(readOnly = true)
    public ReleaseTag findByName(String name) {
        return releaseTagRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Collection<ReleaseTag> findByNameLike(String name) {
        return releaseTagRepository.findByNameLike(name);
    }
}
