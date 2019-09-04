package edu.monash.knowledgezoo.api.controller;

import edu.monash.knowledgezoo.api.service.ApkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apk/")
public class ApkController {

    private final ApkService apkService;

    public ApkController(ApkService apkService) {
        this.apkService = apkService;
    }

    @GetMapping("/test")
    public String test() {
        return apkService.findByLabelLike("test").toString();
    }
}
