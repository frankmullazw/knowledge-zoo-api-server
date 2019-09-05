package edu.monash.knowledgezoo.api.service;

import edu.monash.knowledgezoo.api.repository.ApkRepository;
import edu.monash.knowledgezoo.api.repository.entity.Apk;
import edu.monash.knowledgezoo.api.repository.entity.Permission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApkTestDataService {


    private final ApkRepository apkRepository;

    public ApkTestDataService(ApkRepository apkRepository) {
        this.apkRepository = apkRepository;
    }

    @Transactional
    public void generateTestData() {
        deleteApks();
        generateApks();
        System.out.println("Server: generateTestData() Complete");
    }

    private void generateApks() {
        Apk apk = new Apk();
        apk.setName("Facebook");
        apk.setSha256("3d690add7242d62ad559d3aefc8613015a8f6ed4aa8bc5e2962b857ab21834e1");
        apk.setSize(31489381L);
        apk.addPermission(new Permission("android.permission.WRITE_SYNC_SETTINGS"));
        apk.addPermission(new Permission("com.facebook.home.permission.WRITE_BADGES"));
        apkRepository.save(apk);
    }

    private void printTest() {

    }

    private void deleteApks() {
        apkRepository.deleteAll();
    }

    public void printTestNode() {
        for (Apk apk : apkRepository.findAll()) {
            System.out.println(apk);
        }
    }
}
