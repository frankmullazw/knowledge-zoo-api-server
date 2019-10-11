package edu.monash.knowledgezoo.api;

import edu.monash.knowledgezoo.api.utils.JsonApkDataImporterService;
import edu.monash.knowledgezoo.api.utils.SimpleStopWatchTimer;
import edu.monash.knowledgezoo.api.utils.onetimescripts.ReleaseTagRenameService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories("edu.monash.knowledgezoo.api.repository")
public class KnowledgeZooApiServerApplication {

    public static void main(String[] args) {
//        SpringApplication.run(KnowledgeZooApiServerApplication.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(KnowledgeZooApiServerApplication.class, args);
        SimpleStopWatchTimer stopWatchTimer = new SimpleStopWatchTimer();
        stopWatchTimer.start();
//        context.getBean(ApkTestDataService.class).generateTestData();
//        context.getBean(ReleaseTagTestDataService.class).generateTestData();
//        context.getBean(ApkTestDataService.class).removeApkData();
//        System.out.println("Old Apps removed");
//        context.getBean(JsonApkDataImporterService.class).parseFolder(
//                "C:\\Uni\\FIT4003\\KnowledgeZooProjects\\knowledge-zoo-api-server\\extractData\\androguard-master\\Information\\JSON"
//        );

        // todo: Rename Tags
//        context.getBean(ReleaseTagRenameService.class).renamePrefixedTags();
        context.getBean(JsonApkDataImporterService.class).parseFolder(
                "C:\\Uni\\FIT4003\\Content\\testing_apps"
        );
        System.out.printf("Done in :%s", stopWatchTimer.getMainTime().toString());
        stopWatchTimer.stop();
//        context.getBean(ApkTestDataService.class).printTestNode();
    }

}
