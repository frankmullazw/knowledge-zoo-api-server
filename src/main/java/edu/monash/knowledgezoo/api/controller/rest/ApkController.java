package edu.monash.knowledgezoo.api.controller.rest;

import edu.monash.knowledgezoo.api.repository.entity.Apk;
import edu.monash.knowledgezoo.api.service.ApkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/apk/")
public class ApkController {

    private final ApkService apkService;

    public ApkController(ApkService apkService) {
        this.apkService = apkService;
    }

    @GetMapping("/test")
    public Collection<Apk> test() {
//        return new Gson().toJson(apkService.findByNameLike("Facebook"));
        return apkService.findByNameLike("Facebook");
    }
}
