package edu.monash.knowledgezoo.api.utils;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class DataImporterUtility {

    private final ApplicationContext appContext;
    private SimpleStopWatchTimer stopWatchTimer = new SimpleStopWatchTimer();


    public DataImporterUtility(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    //    @EventListener(ApplicationReadyEvent.class)
    public void importLinuxData() {
        stopWatchTimer.start();
        appContext.getBean(JsonApkDataImporterService.class).parseFolder("/home/ubuntu/JSON", null);
        System.out.printf("Done in :%s", stopWatchTimer.getMainTime().toString());
        stopWatchTimer.stop();
        stopWatchTimer.reset();
    }

    //    @EventListener(ApplicationReadyEvent.class)
    public void configurableDataInput() {
        stopWatchTimer.start();
        // todo: Rename Tags
//        context.getBean(ReleaseTagRenameService.class).renamePrefixedTags();
        //        context.getBean(ApkTestDataService.class).printTestNode();
//        context.getBean(ReleaseTagTestDataService.class).testDataRetrieval();
//        context.getBean(ReleaseTagTestDataService.class).generateTestData();
//        context.getBean(ApkTestDataService.class).removeApkData();
//        appContext.getBean(JsonApkDataImporterService.class).parseFolder("/home/ubuntu/JSON", null);
        System.out.printf("Done in :%s", stopWatchTimer.getMainTime().toString());
        stopWatchTimer.stop();
        stopWatchTimer.reset();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void importWindowsData() {
        stopWatchTimer.start();
        appContext.getBean(JsonApkDataImporterService.class).parseFolder("C:\\Uni\\FIT4003\\Content\\JSON", null);
        stopWatchTimer.stop();
        stopWatchTimer.reset();
    }

}
