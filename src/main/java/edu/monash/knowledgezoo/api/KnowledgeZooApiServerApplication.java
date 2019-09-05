package edu.monash.knowledgezoo.api;

import edu.monash.knowledgezoo.api.service.ApkTestDataService;
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
        context.getBean(ApkTestDataService.class).generateTestData();
//        context.getBean(ApkTestDataService.class).printTestNode();
    }

}
