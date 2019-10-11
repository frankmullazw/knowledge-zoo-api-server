package edu.monash.knowledgezoo.api.controller.rest;

import edu.monash.knowledgezoo.api.repository.entity.ReleaseTag;
import edu.monash.knowledgezoo.api.service.ReleaseTagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/tag/")
public class ReleaseTagController {

    private final ReleaseTagService tagService;

    public ReleaseTagController(ReleaseTagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/test")
    public Iterable<ReleaseTag> test() {
//        return new Gson().toJson(apkService.findByNameLike("Facebook"));
        System.out.println("Here");
        return tagService.findAll();
//        return tagService.findByNameLike("refs");
    }
}
