package edu.monash.knowledgezoo.api.utils.onetimescripts;

import edu.monash.knowledgezoo.api.repository.ApiRepository;
import edu.monash.knowledgezoo.api.repository.ReleaseTagRepository;
import edu.monash.knowledgezoo.api.repository.entity.ReleaseTag;
import org.springframework.stereotype.Service;

@Service
public class ReleaseTagRenameService {

    private ReleaseTagRepository tagRepo;

    private ApiRepository apiRepo;


    public ReleaseTagRenameService(ReleaseTagRepository tagRepo, ApiRepository apiRepo) {
        this.tagRepo = tagRepo;
        this.apiRepo = apiRepo;
    }

    /*
        Removes prefix from all tags in DB
     */
    public void renamePrefixedTags() {
        for (ReleaseTag tag : tagRepo.findAll()) {
            if (tag.getName().startsWith(ReleaseTag.TAG_PREFIX)) {
                tag.setName(tag.getName().replaceFirst(ReleaseTag.TAG_PREFIX, ""));
                tagRepo.save(tag);
            }
        }

    }
}
