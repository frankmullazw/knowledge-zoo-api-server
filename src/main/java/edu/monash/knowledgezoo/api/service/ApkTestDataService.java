package edu.monash.knowledgezoo.api.service;

import edu.monash.knowledgezoo.api.repository.*;
import edu.monash.knowledgezoo.api.repository.entity.Apk;
import edu.monash.knowledgezoo.api.repository.entity.FingerprintCertificate;
import edu.monash.knowledgezoo.api.repository.entity.OwnerCertificate;
import edu.monash.knowledgezoo.api.repository.entity.Permission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApkTestDataService {


    private final ApkRepository apkRepo;

    private final PermissionRepository permissionRepo;

    private final SDKVersionRepository sdkVersionRepo;

    private final OwnerCertificateRepository ownerRepo;

    private final FingerprintCertificateRepository fingerprintReppo;

    public ApkTestDataService(ApkRepository apkRepo, PermissionRepository permissionRepo, SDKVersionRepository sdkVersionRepo, OwnerCertificateRepository ownerRepo, FingerprintCertificateRepository fingerprintReppo) {
        this.apkRepo = apkRepo;
        this.permissionRepo = permissionRepo;
        this.sdkVersionRepo = sdkVersionRepo;
        this.ownerRepo = ownerRepo;
        this.fingerprintReppo = fingerprintReppo;
    }

    @Transactional
    public void generateTestData() {
        apkRepo.deleteAll();
        permissionRepo.deleteAll();
        sdkVersionRepo.deleteAll();
        ownerRepo.deleteAll();
        fingerprintReppo.deleteAll();
        System.out.println("Server: Test data nodes deleted!");

        generateTestApks();
        System.out.println("Server: generateTestData() Complete");
    }

    private void generateTestApks() {
        Apk apk = new Apk();
        apk.setName("Facebook");
        apk.setSha256("3d690add7242d62ad559d3aefc8613015a8f6ed4aa8bc5e2962b857ab21834e1");
        apk.setSize(31489381L);
        apk.setFingerprintCertificate(
                new FingerprintCertificate("E3:F9:E1:E0:CF:99:D0:E5:6A:05:5B:A6:5E:24:1B:33:99:F7:CE:A5:24:32:6B:0C:DD:6E:C1:32:7E:D0:FD:C1"));
        apk.setOwnerCertificate(OwnerCertificate.generateFromFullCertificate
                        ("Common Name: Facebook Corporation, Organizational Unit: Facebook, Organization: Facebook Mobile, Locality: Palo Alto, State/Province: CA, Country: US"));
        apk.addPermission(new Permission("android.permission.WRITE_SYNC_SETTINGS"));
        apk.addPermission(new Permission("com.facebook.home.permission.WRITE_BADGES"));
        apk.addPermission(new Permission("android.permission.CALL_PHONE"));
        apk.addPermission(new Permission("com.facebook.katana.permission.RECEIVE_ADM_MESSAGE"));
        apk.addPermission(new Permission("android.permission.ACCESS_NETWORK_STATE"));
        apkRepo.save(apk);
    }

    public void printTestNode() {
        for (Apk apk : apkRepo.findAll()) {
            System.out.println(apk);
        }
    }
}
