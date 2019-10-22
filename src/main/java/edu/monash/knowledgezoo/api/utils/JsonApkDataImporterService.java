package edu.monash.knowledgezoo.api.utils;

import edu.monash.knowledgezoo.api.repository.*;
import edu.monash.knowledgezoo.api.repository.entity.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
    private SimpleStopWatchTimer stopWatchTimer = new SimpleStopWatchTimer();


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

    Set<String> parseFolder(String path, Integer limit) {
        Set<File> files = getAllFileNames(path);
        Set<String> apkNames = new HashSet<>();
        int count = 0;
        for (File file : files)
            if (file.length() > 0) {
                try {
                    Apk apk = parseApkFile(file);
                    if (apk != null)
                        apkNames.add(apk.getName());
                    // Clean up and add data
                    count++;
                    System.out.printf("Processed App File No: %d of %d\n", count, files.size());
                    if (limit != null && count >= limit)
                        return apkNames;
                } catch (IOException | ParseException e) {
                    System.out.println("Something went wrong importing: " + file.getName());
                    e.printStackTrace();
                }
            }
        // Save the APKs
        return apkNames;
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
                stopWatchTimer.start();
                processApkCertificates(apk, jo);
                processApkActivities(apk, jo);
                processApkPermissions(apk, jo);
                processApkApisandPackages(apk, jo);

                // Save Apk
                System.out.println("\t\tSaving Apk...");
                apkRepo.save(apk);
                System.out.printf("Saved Apk: %s, Time: %s\n\n", apk.getName(), stopWatchTimer.getMainTime().toString());
                stopWatchTimer.stop();
                stopWatchTimer.reset();
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

        // Package Name had a colon in the imports so a quick check for both
        if (packageName == null || packageName.length() < 1)
            packageName = (String) jo.get(Apk.PACKAGE_NAME_PROPERTY_NAME_WITH_COLON);

        // Check the sha256 to see if we have the application so we can quickly skip the rest
        Apk apk = apkRepo.findBySha256(sha256);
        if (apk != null) {
            System.out.println(String.format("Apk already found: name: %s, v%s and  (Uploaded): name: %s, v%s", apk.getName(),
                    apk.getVersionName(), name, versionName));
            if (apk.getPackageName() == null || apk.getPackageName().length() < 1)
                apk.setPackageName(packageName);
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
        } catch (ClassCastException | NumberFormatException e) {
            System.out.printf("\t\tCasting to Integer failed for minimum SDK: %s, App: %s", minSDKVersion, name);
        }
        return apk;
    }

    private void processApkCertificates(Apk apk, JSONObject jo) {
        // Check and Assign Certificates
        System.out.println("\t\tSaving Certificates");
        String jsonCertificateFingerprint = (String) jo.get(Apk.FINGERPRINT_CERTIFICATE_PROPERTY_NAME);
        String jsonCertificateOwner = (String) jo.get(Apk.OWNER_CERTIFICATE_PROPERTY_NAME);
//        System.out.printf("\t\t%s\n", jsonCertificateOwner);

        // Process Fingerprint Certificate
        FingerprintCertificate certificate = fingerprintCertificateRepo.findByName(jsonCertificateFingerprint);
        if (certificate == null) {
            certificate = new FingerprintCertificate(jsonCertificateFingerprint);
            fingerprintCertificateRepo.save(certificate);
        }
        apk.setFingerprintCertificate(certificate);

        // Process Owner certificate
        OwnerCertificate ownerCertificate = ownerCertificateRepo.findByFullCertificate(jsonCertificateOwner);
        if (ownerCertificate == null) {
            ownerCertificate = OwnerCertificate.generateFromFullCertificate(jsonCertificateOwner);
            ownerCertificateRepo.save(ownerCertificate);
        }
        apk.setOwnerCertificate(ownerCertificate);
    }

    private void processApkActivities(Apk apk, JSONObject jo) {
        // Assigning Activities
        Set<String> activityNames = new HashSet<String>((JSONArray) jo.get("activity"));
        System.out.printf("\t\tProcessing %d Activities...\n", activityNames.size());
        Set<Activity> retrievedActivities = activityRepo.findByNameIsIn(activityNames);
        // Process Saved/Retrieved Activities
        for (Activity retrievedActivity : retrievedActivities) {
            apk.addActivity(retrievedActivity);
            activityNames.remove(retrievedActivity.getName());
        }

        Set<Activity> activities = new HashSet<>(activityNames.size());
        for (String activityName : activityNames) {
            activities.add(new Activity(activityName));
        }
        activityRepo.saveAll(activities);
        apk.getActivities().addAll(activities);
    }

    private void processApkPermissions(Apk apk, JSONObject jo) {
        // Saving Permissions
        Set<String> permissionNames = new HashSet<>((JSONArray) jo.get("permission"));
        System.out.printf("\t\tProcessing %d Permissions...\n", permissionNames.size());
        Set<Permission> retrievedPermissions = permissionRepo.findByNameIsIn(permissionNames);
        // Process Saved/Retrieved Permissions
        for (Permission retrievedPermission : retrievedPermissions) {
            apk.addPermission(retrievedPermission);
            permissionNames.remove(retrievedPermission.getName());
        }

        Set<Permission> permissions = new HashSet<>(permissionNames.size());
        for (String permissionName : permissionNames) {
            permissions.add(new Permission(permissionName));
        }
        permissionRepo.saveAll(permissions);
        apk.getPermissions().addAll(permissions);
    }

    private void processApkApisandPackages(Apk apk, JSONObject jo) {
        // Assigning Apis
        JSONArray jsonApiArray = (JSONArray) jo.get("API");
        System.out.printf("\t\tProcessing %d Apis...\n", jsonApiArray.size());
        Set<String> apiNames = new HashSet<>();
        Set<String> packageNames = new HashSet<>();
        HashMap<String, Set<String>> apiPackageNames = new HashMap<>();

        for (JSONObject apiObj : new ArrayList<JSONObject>(jsonApiArray)) {
            Set<String> apiKeys = (Set<String>) apiObj.keySet();
            for (String apiKey : apiKeys) {
                apiNames.add(apiKey);
                ArrayList<String> apiPackages = new ArrayList<String>((JSONArray) apiObj.get(apiKey));
                Set<String> currentApiPackageNames = new HashSet<>();
//                System.out.printf("Packages Size: %d\n", apiPackages.size());
                Iterator<String> iter = apiPackages.iterator();
                while (iter.hasNext()) {
                    String currentPackageName = iter.next().replace('/', '.').trim();
                    if (currentPackageName.length() > 0) {
                        packageNames.add(currentPackageName);
                        currentApiPackageNames.add(currentPackageName);
                    } else {
                        System.out.printf("\t\t\t--Empty Package for: %s, API: %s\n", apk.getName(), apiKey);
                    }
                }
                apiPackageNames.put(apiKey, currentApiPackageNames);
            }
        }
        HashMap<String, ApiPackage> packageMap = new HashMap<>();
        Set<Api> retrievedApis = apiRepo.findByNameIsIn(apiNames);
//        System.out.printf("Api: %d, retrieved: %d\n", apiNames.size(), retrievedApis.size());
        System.out.printf("\t\tProcessing %d Packages with Apis...\n", packageNames.size());

        // Process Retrieved Packages
        Set<ApiPackage> retrievedPackages = packageRepo.findByNameIsIn(packageNames);
//        System.out.printf("Packages: %d, retrieved: %d\n", packageNames.size(), retrievedPackages.size());

        // Remove Existing Packages From List
        for (ApiPackage retrievedPackage : retrievedPackages) {
            packageMap.put(retrievedPackage.getName(), retrievedPackage);
            packageNames.remove(retrievedPackage.getName());
        }

        // Process New Packages
        // Create Package Nodes for unsaved packages
        for (String packageName : packageNames) {
            packageMap.put(packageName, new ApiPackage(packageName));
        }

        // Process Retrieved Apis and Relationship
        packageRepo.saveAll(packageMap.values());
        for (Api retrievedApi : retrievedApis) {
            Set<String> currentPackageNames = apiPackageNames.get(retrievedApi.getName());
            apk.addApiandPackageNames(retrievedApi, currentPackageNames);
            for (String currentPackageName : currentPackageNames) {
                packageMap.get(currentPackageName).addApi(retrievedApi);
            }
            apiNames.remove(retrievedApi.getName());
            apiPackageNames.remove(retrievedApi.getName());
        }

        // Process New Apis
        Set<Api> apis = new HashSet<>();
        for (String apiName : apiNames) {
            apis.add(new Api(apiName));
        }

        apiRepo.saveAll(apis);
        // Process New Api Relationship
        for (Api api : apis) {
            Set<String> currentPackageNames = apiPackageNames.get(api.getName());
            apk.addApiandPackageNames(api, currentPackageNames);
            for (String currentPackageName : currentPackageNames) {
                packageMap.get(currentPackageName).addApi(api);
            }
            apiPackageNames.remove(api.getName());
            apiNames.remove(api.getName());
        }
        packageRepo.saveAll(packageMap.values());
    }
}
