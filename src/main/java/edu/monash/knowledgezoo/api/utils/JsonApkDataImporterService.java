package edu.monash.knowledgezoo.api.utils;

import edu.monash.knowledgezoo.api.repository.*;
import edu.monash.knowledgezoo.api.repository.entity.*;
import edu.monash.knowledgezoo.api.repository.entity.relationship.ApiPackageRelationship;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
public class JsonApkDataImporterService {

    private ApkRepository apkRepo;
    private PermissionRepository permissionRepo;
    private ApiRepository apiRepo;
    private SDKVersionRepository versionRepo;
    private FingerprintCertificateRepository fingerprintCertificateRepo;
    private OwnerCertificateRepository ownerCertificateRepo;
    private ActivityRepository activityRepo;
    private ApiPackageRepository packageRepo;
    private SDKVersionRepository sdkVersionRepo;

    public JsonApkDataImporterService(ApkRepository apkRepo, PermissionRepository permissionRepo, ApiRepository apiRepo, SDKVersionRepository versionRepo, FingerprintCertificateRepository fingerprintCertificateRepo, OwnerCertificateRepository ownerCertificateRepo, ActivityRepository activityRepo, ApiPackageRepository packageRepo, SDKVersionRepository sdkVersionRepo) {
        this.apkRepo = apkRepo;
        this.permissionRepo = permissionRepo;
        this.apiRepo = apiRepo;
        this.versionRepo = versionRepo;
        this.fingerprintCertificateRepo = fingerprintCertificateRepo;
        this.ownerCertificateRepo = ownerCertificateRepo;
        this.activityRepo = activityRepo;
        this.packageRepo = packageRepo;
        this.sdkVersionRepo = sdkVersionRepo;
    }

    private Set<File> getAllFileNames(String path) {
        // Return all the file names in the given path with extensions .txt and .json
        File folder = new File(path);
        Set<File> files = new HashSet<>();
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null)
            for (File file : listOfFiles) {
                if (file.isFile())
                    if (file.getName().endsWith(".txt") || file.getName().endsWith(".json"))
                        files.add(file);
            }
        return files;
    }

    public Set<Apk> parseFolder(String path, Integer limit) {
        Set<File> files = getAllFileNames(path);
        Set<Apk> apks = new HashSet<>();
        int count = 0;
        for (File file : files)
            if (file.length() > 0) {
                try {
                    Apk apk = parseApkFile(file);
                    if (apk != null)
                        apks.add(apk);
                    // Clean up and add data
                    count++;
                    System.out.printf("Processed App File No: %d of %d\n", count, files.size());
                    if (limit != null && count >= limit)
                        return apks;
                } catch (IOException | ParseException e) {
                    System.out.println("Something went wrong importing: " + file.getName());
                    e.printStackTrace();
                }
            }
        // Save the APKs
        return apks;
    }

    @Transactional
    public Apk parseApkFile(File file) throws IOException, ParseException {
        JSONObject jo = (JSONObject) new JSONParser().parse(new FileReader(file));
        if (jo != null) {
            Apk apk = processBaseApkData(jo);
            if (apk.getId() != null) {
                return null;
                // This apk already exists
                // todo: Perform any checks for this apk
            } else {
                // Apk doesnt exist in DB process metadata
                processApkCertificates(apk, jo);
                processApkActivities(apk, jo);
                processApkPermissions(apk, jo);
                processApkApisandPackages(apk, jo);

                // Save Apk
                System.out.println("\n\t\tSaving Apk...");
                apkRepo.save(apk);
                System.out.println("Saved Apk: " + apk.getName());
                return apk;
            }
        }
        return null;
    }


    private Apk processBaseApkData(JSONObject jo) {
        // typecasting obj to JSONObject
        String sha256 = (String) jo.get(Apk.SHA256_PROPERTY_NAME);
        String name = (String) jo.get(Apk.NAME_PROPERTY_NAME);
        String versionName = (String) jo.get(Apk.VERSION_NAME_PROPERTY_NAME);
        String packageName = (String) jo.get(Apk.PACKAGE_NAME_PROPERTY_NAME);

        // Check the sha256 to see if we have the application so we can quickly skip the rest
        Apk apk = apkRepo.findBySha256(sha256);
        if (apk != null) {
            System.out.println(String.format("Apk already found: name: %s, v%s and  (Uploaded): name: %s, v%s", apk.getName(),
                    apk.getVersionName(), name, versionName));
            return apk;
        }
        apk = new Apk();

        // getting firstName and lastName
        System.out.println("Processing Apk: " + name);


        String versionCode = (String) jo.get(Apk.VERSION_CODE_PROPERTY_NAME);
        Long size = (Long) jo.get(Apk.SIZE_PROPERTY_NAME);
        String minSDKVersion = (String) jo.get(Apk.MINIMUM_SDK_PROPERTY_NAME);

        // Process base properties
        System.out.println("\t\tSaving base properties...");
        apk.setName(name);
        apk.setSha256(sha256);
        try {
            apk.setVersionCode(Integer.parseInt(versionCode));
        } catch (ClassCastException e) {
            System.out.println("Casting to Integer failed for : " + versionCode);
        }
        apk.setVersionName(versionName);
        apk.setSize(size);
        apk.setPackageName(packageName);

        // Check and Assign SDK
        try {
            int sdkNumber = Integer.parseInt(minSDKVersion);
            if (sdkNumber > 0) {
                SDKVersion sdkVersion = versionRepo.findByApiLevel(sdkNumber);
                if (sdkVersion == null) {
                    sdkVersion = new SDKVersion(sdkNumber);
                    sdkVersionRepo.save(sdkVersion);
                }
            } else
                System.out.println(String.format("Invalid SDK version %d for app: %s", sdkNumber, name));
        } catch (ClassCastException e) {
            System.out.println("Casting to Integer failed for minimum SDK: " + minSDKVersion);
        }
        return apk;
    }

    private void processApkCertificates(Apk apk, JSONObject jo) {
        // Check and Assign Certificates
        System.out.println("\t\tSaving Certificates");
        String jsonCertificateFingerprint = (String) jo.get(Apk.FINGERPRINT_CERTIFICATE_PROPERTY_NAME);
        String jsonCertificateOwner = (String) jo.get(Apk.OWNER_CERTIFICATE_PROPERTY_NAME);

        FingerprintCertificate certificate = fingerprintCertificateRepo.findByName(jsonCertificateFingerprint);
        if (certificate == null) {
            certificate = new FingerprintCertificate(jsonCertificateFingerprint);
            fingerprintCertificateRepo.save(certificate);
        }
        apk.setFingerprintCertificate(certificate);


        OwnerCertificate ownerCertificate = ownerCertificateRepo.findByFullCertificate(jsonCertificateOwner);
        if (ownerCertificate == null) {
            ownerCertificate = OwnerCertificate.generateFromFullCertificate(jsonCertificateOwner);
            ownerCertificateRepo.save(ownerCertificate);
        }
        apk.setOwnerCertificate(ownerCertificate);

    }

    private void processApkActivities(Apk apk, JSONObject jo) {
        // Assigning Activities
        JSONArray jsonArray = (JSONArray) jo.get("activity");
        System.out.printf("\t\tProcessing %d Activities...\n", jsonArray.size());
        for (String activityName : new ArrayList<String>(jsonArray)) {
            // Check existance
            // If exists add Permission
            // Else create a new one
            Activity activity = activityRepo.findByName(activityName);
            if (activity == null) {
                activity = new Activity(activityName);
                activityRepo.save(activity);
            }
            apk.addActivity(activity);
        }
    }

    private void processApkPermissions(Apk apk, JSONObject jo) {
        // Saving Permissions
        JSONArray jsonArray = (JSONArray) jo.get("permission");
        System.out.printf("\t\tProcessing %d Permissions...\n", jsonArray.size());
        for (String permissionName : new ArrayList<String>(jsonArray)) {
            // Check existance
            // If exists add Permission
            // Else create a new one
            Permission permission = permissionRepo.findByName(permissionName);
            if (permission == null) {
                permission = new Permission(permissionName);
                permissionRepo.save(permission);
            }
            apk.addPermission(permission);
        }
    }

    private void processApkApisandPackages(Apk apk, JSONObject jo) {
        // Assigning Apis
        JSONArray jsonApiArray = (JSONArray) jo.get("API");
        System.out.printf("\t\tProcessing %d Apis...\n", jsonApiArray.size());
        int count = 1;
        for (JSONObject apiObj : new ArrayList<JSONObject>(jsonApiArray)) {
            for (String apiKey : (Set<String>) apiObj.keySet()) {
                // Check existance
                // If exists add APi
                // Else create a new one
                ApiPackageRelationship apiPackageRelationship = new ApiPackageRelationship();
                Api api = apiRepo.findByName(apiKey);
                if (api == null) {
                    api = new Api(apiKey);
                    apiRepo.save(api);
                } else
                    System.out.print("\t\t(API Found) - ");
                System.out.printf("\t\tAPI[%d of %d]: %s ", count++, jsonApiArray.size(), api.getName());
                apiPackageRelationship.setApi(api);

//                    System.out.println("API: " + apiKey);
                JSONArray apiPackages = (JSONArray) apiObj.get(apiKey);
                Iterator<String> jsonPackages = (Iterator<String>) apiPackages.iterator();
                Set<ApiPackage> packages = new HashSet<>();
                System.out.printf("(Processing %d Packages) - \n", apiPackages.size());
                while (jsonPackages.hasNext()) {
                    String currentPackageName = jsonPackages.next().replace('/', '.').trim();
                    if (currentPackageName.length() > 0) {
                        ApiPackage apiPackage = packageRepo.findByNameEquals(currentPackageName);
                        if (apiPackage == null) {
                            apiPackage = new ApiPackage(currentPackageName);
//                            System.out.printf("Saving Package: %s ", apiPackage.getName());
                            packageRepo.save(apiPackage);
                        } else {
//                            System.out.printf("Existing Package: %s ", apiPackage.getName());
                            ;
                        }
                        // todo: Determine whether we want to save the package apis or not
//                        apiPackage.addApi(api);
//                        packageRepo.save(apiPackage);
                        packages.add(apiPackage);
                    } else
                        System.out.printf("\t\t\t--Empty Package for: %s, API: %s\n", apk.getName(), api.getName());
                }
                apk.addApiandPackages(api, packages);
            }
        }
    }

}
