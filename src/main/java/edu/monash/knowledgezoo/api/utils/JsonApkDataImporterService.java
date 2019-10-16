package edu.monash.knowledgezoo.api.utils;

import edu.monash.knowledgezoo.api.repository.*;
import edu.monash.knowledgezoo.api.repository.entity.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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

    public Set<Apk> parseFolder(String path) {
        Set<File> files = getAllFileNames(path);
        Set<Apk> apks = new HashSet<>();
        for (File file : files)
            if (file.length() > 0) {
                try {
                    Apk apk = parseFile(file);
                    if (apk != null)
                        apks.add(apk);
                    // Clean up and add data
                } catch (IOException | ParseException e) {
                    System.out.println("Something went wrong importing: " + file.getName());
                    e.printStackTrace();
                }
            }
        // Save the APKs
        return apks;
    }

    @Transactional
    public Apk parseFile(File file) throws IOException, ParseException {
        Apk apk = new Apk();
        // typecasting obj to JSONObject
        JSONObject jo = (JSONObject) new JSONParser().parse(new FileReader(file));

        // getting firstName and lastName
        String name = (String) jo.get("name");
        System.out.println("Processing Apk: " + name);
        String sha256 = (String) jo.get("SHA256");
        String versionCode = (String) jo.get("versionCode");
        String versionName = (String) jo.get("versionName");
        Long size = (Long) jo.get("size");
        String minSDKVersion = (String) jo.get("minSDKVersion");
        String jsonCertificateFingerprint = (String) jo.get("certificate(fingerprint)");
        String jsonCertificateOwner = (String) jo.get("certificate(owner)");

        // start checking
        // todo: Check if all the values are consistent
        Apk retrievedApk = apkRepo.findBySha256(sha256);
        if (retrievedApk != null) {
            System.out.println(String.format("Apk already found: name: %s, v%s and  (Uploaded): name: %s, v%s", retrievedApk.getName(),
                    retrievedApk.getVersionName(), name, versionName));
            return retrievedApk;
        } else {

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

            // Check and Assign Certificates
            System.out.println("\t\tSaving Certificates");
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

            // Reused to save memory
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

            // Assigning Activities
            jsonArray = (JSONArray) jo.get("activity");
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


            // Assigning Apis
            jsonArray = (JSONArray) jo.get("API");
            System.out.printf("\t\tProcessing %d Apis...\n", jsonArray.size());
            System.out.print("\t\tSaved Apis: ");
            int count = 1;
            for (JSONObject apiObj : new ArrayList<JSONObject>(jsonArray)) {
                for (String apiKey : (Set<String>) apiObj.keySet()) {
                    // Check existance
                    // If exists add APi
                    // Else create a new one
                    Api api = apiRepo.findByName(apiKey);
                    if (api == null) {
                        api = new Api(apiKey);
                        apiRepo.save(api);
                    } else
                        System.out.print("(API Found) - ");

//                    System.out.println("API: " + apiKey);
                    JSONArray apiPackages = (JSONArray) apiObj.get(apiKey);
                    Iterator<String> jsonPackages = (Iterator<String>) apiPackages.iterator();
                    while (jsonPackages.hasNext()) {
                        String currentPackageName = jsonPackages.next().replace('/', '.').trim();
                        if (currentPackageName.length() > 0) {
                            List<ApiPackage> packageList = packageRepo.findByName(currentPackageName);
                            System.out.printf("(Processing %d Packages) - ", packageList.size());
                            try {
                                ApiPackage apiPackage = packageRepo.findByNameEquals(currentPackageName);
                                if (apiPackage == null) {
                                    apiPackage = new ApiPackage(currentPackageName);
                                    packageRepo.save(apiPackage);
                                }
                                api.addPackage(apiPackage);
                            } catch (IncorrectResultSizeDataAccessException e) {
                                System.out.println("Error!!");
                                System.out.printf("APK: %s, api: %s, package: %s\n", apk.getName(), api.getName(), currentPackageName);
                                System.out.println(e.getActualSize());
                                System.out.println("Packages Found");
                                for (ApiPackage apiPackage : packageList)
                                    System.out.printf("Package: id: %d, name: %s\n", apiPackage.getId(), apiPackage.getName());
                                System.exit(-1);
                            }
                        } else
                            System.out.printf("\t\t\t--Empty Package for: %s, API: %s\n", apk.getName(), api.getName());
                    }
                    apiRepo.save(api);
                    System.out.printf("%d: %s, ", count, api.getName());
                    System.out.println();
                    apk.addApi(api);
                }
            }
            // Save Apk
            System.out.println("\n\t\tSaving Apk...");
            apkRepo.save(apk);
            System.out.println("Saved Apk: " + apk.getName());
            return apk;
        }
    }
}
