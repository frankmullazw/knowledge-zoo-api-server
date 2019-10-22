package edu.monash.knowledgezoo.api.utils;

import edu.monash.knowledgezoo.api.repository.ApiRepository;
import edu.monash.knowledgezoo.api.repository.entity.Api;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@Component
public class DataImporterUtility implements DisposableBean, Runnable {

    private final ApplicationContext appContext;
    private final ApiRepository apiRepo;
    private SimpleStopWatchTimer stopWatchTimer = new SimpleStopWatchTimer();
    private volatile boolean isImportDone;
    private Thread thread;

    public DataImporterUtility(ApplicationContext appContext, ApiRepository apiRepo) {
        this.appContext = appContext;
        this.apiRepo = apiRepo;
        this.thread = new Thread(this);
    }

    @Override
    public void run() {
        if (!this.isImportDone) {
            System.out.println("DataImport Started");
//            this.configurableTests();
//            this.importLinuxData();
            this.importWindowsData();
            this.isImportDone = true;
            System.out.printf("Done in :%s", stopWatchTimer.getMainTime().toString());
            stopWatchTimer.stop();
            stopWatchTimer.reset();
            this.thread.interrupt();
        }
    }

    @Override
    public void destroy() {
        this.isImportDone = true;
    }


//    @EventListener(ApplicationReadyEvent.class)
    public void runThreadOnReady() {
        stopWatchTimer.start();
        this.thread.start();
    }


    public void importLinuxData() {
        appContext.getBean(JsonApkDataImporterService.class).parseFolder("/home/ubuntu/JSON", null);
    }

    public void configurableDataInput() {
        // todo: Rename Tags
//        context.getBean(ReleaseTagRenameService.class).renamePrefixedTags();
        //        context.getBean(ApkTestDataService.class).printTestNode();
//        context.getBean(ReleaseTagTestDataService.class).testDataRetrieval();
//        context.getBean(ReleaseTagTestDataService.class).generateTestData();
//        context.getBean(ApkTestDataService.class).removeApkData();
//        appContext.getBean(JsonApkDataImporterService.class).parseFolder("/home/ubuntu/JSON", null);
    }

    public void importWindowsData() {
        appContext.getBean(JsonApkDataImporterService.class).parseFolder("C:\\Uni\\FIT4003\\Content\\JSON", null);
    }

//    @EventListener(ApplicationReadyEvent.class)
    public void configurableTests() {
        stopWatchTimer.start();
        String[] apis = {"android.support.v4.app.ActionBarDrawerToggle.SlideDrawable.invalidateSelf()", "android.support.v4.app.ActionBarDrawerToggle.SlideDrawable.scheduleSelf(long,java.lang.Runnable)", "android.support.v4.app.ActionBarDrawerToggle.SlideDrawable.unscheduleSelf(java.lang.Runnable)", "android.support.v4.app.DialogFragment.getFragmentManager()", "android.support.v4.app.DialogFragment.getView()", "android.support.v4.app.DialogFragment.getActivity()", "android.support.v4.app.FragmentActivity.getText(long)", "android.support.v4.app.FragmentActivity.getLayoutInflater()"};
//        String[] apis = {"android.support.v4.app.ActionBarDrawerToggle.SlideDrawable.invalidateSelf()"};
        Set<String> apiNames = new HashSet<>(Arrays.asList(apis));
        Set<Api> retrieved = apiRepo.findByNameIsIn(apiNames);
        System.out.printf("StartApi: %d, retrieved: %d\n", apiNames.size(), retrieved.size());
        System.out.printf("Done in :%s", stopWatchTimer.getMainTime().toString());
        stopWatchTimer.stop();
    }

}
