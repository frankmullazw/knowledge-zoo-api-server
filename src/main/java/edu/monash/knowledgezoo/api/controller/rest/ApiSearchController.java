package edu.monash.knowledgezoo.api.controller.rest;

import edu.monash.knowledgezoo.api.repository.entity.Api;
import edu.monash.knowledgezoo.api.service.ApiService;
import edu.monash.knowledgezoo.api.service.ApkService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("api/search/")
public class ApiSearchController {
    private final ApkService apkService;

    private final ApiService signatureService;

    public ApiSearchController(ApkService apkService, ApiService signatureService) {
        this.apkService = apkService;
        this.signatureService = signatureService;
    }

    private Collection<Api> searchApi(@RequestParam(name = "apName") String apName) {
        return signatureService.findByNameLike(apName);
    }

}
