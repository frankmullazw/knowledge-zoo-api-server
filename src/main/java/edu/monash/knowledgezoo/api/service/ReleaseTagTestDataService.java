package edu.monash.knowledgezoo.api.service;

import edu.monash.knowledgezoo.api.repository.ApiRepository;
import edu.monash.knowledgezoo.api.repository.ReleaseTagRepository;
import edu.monash.knowledgezoo.api.repository.entity.Api;
import edu.monash.knowledgezoo.api.repository.entity.ReleaseTag;
import edu.monash.knowledgezoo.api.repository.entity.relationship.ReleaseTagApiRelationship;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReleaseTagTestDataService {

    private final ReleaseTagRepository releaseTagRepo;

    private final ApiRepository apiRepo;

    public ReleaseTagTestDataService(ReleaseTagRepository releaseTagRepo, ApiRepository apiRepo) {
        this.releaseTagRepo = releaseTagRepo;
        this.apiRepo = apiRepo;
    }

    @Transactional
    public void generateTestData() {
        releaseTagRepo.deleteAll();
        apiRepo.deleteAll();

        System.out.println("Server: Test data nodes deleted!");

        generateTestReleases();
        System.out.println("Server: generateTestData() Complete");
    }

    private void generateTestReleases() {
        ReleaseTag tag = new ReleaseTag("refs/tags/android-4.2_r1");
        tag.addReleaseApi(new Api("android.app.admin.DevicePolicyManager.createUser(android.content.ComponentName, java.lang.String)")
                , ReleaseTagApiRelationship.State.INTRODUCE);
        releaseTagRepo.save(tag);
    }
}
