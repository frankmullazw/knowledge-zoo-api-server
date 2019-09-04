package edu.monash.knowledgezoo.api.service;

import edu.monash.knowledgezoo.api.repository.ApkRepository;
import edu.monash.knowledgezoo.api.repository.entity.Apk;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class ApkTestDataService {


    private final ApkRepository apkRepository;

    public ApkTestDataService(ApkRepository apkRepository) {
        this.apkRepository = apkRepository;
    }

    @Transactional
    public void generateTestData() {
        generateApks();
        System.out.println("Server: APK file should be made");
    }

    private void generateApks() {
        Apk apk = new Apk("29:F3:4E:5F:27:F2:11:B4:24:BC:5B:F9:D6:71:62:C0:EA:FB:A2:DA:35:AF:35:C1:64:16:FC:44:62:76:BA:26",
                "test app",
                1234567L);
        apkRepository.save(apk);
    }

    private void printTest() {

    }

    public void printTestNode() {
        Collection<Apk> apks = apkRepository.findByNameLike("test");
        for (Apk apk : apks) {
            System.out.println(apk);
        }

    }
}
