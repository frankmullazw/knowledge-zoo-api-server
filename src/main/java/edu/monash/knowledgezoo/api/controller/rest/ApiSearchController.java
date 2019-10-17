package edu.monash.knowledgezoo.api.controller.rest;

import edu.monash.knowledgezoo.api.repository.model.PageFindAPKsByLike;
import edu.monash.knowledgezoo.api.repository.model.SearchByKeywordForm;
import edu.monash.knowledgezoo.api.repository.model.SearchResponse;
import edu.monash.knowledgezoo.api.service.ApkService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("api/search/")
@CrossOrigin(origins = "*")
public class ApiSearchController {
    private final ApkService apkService;

    public ApiSearchController(ApkService apkService) {
        this.apkService = apkService;
    }

    @PostMapping("/byKeyword")
    public SearchResponse searchByKeyword(@RequestBody SearchByKeywordForm form) {
        try {
            SearchResponse res = new SearchResponse();
            PageFindAPKsByLike result = apkService.findByPermissionLike(form.keyword, form.pageIdx);
            res.type = "Permission";
            if (result.apks.size() == 0) {
                res.type = "API";
                result = apkService.findByApiNameLike(form.keyword, form.pageIdx);
            }
            res.apks = result.apks;
            res.hasNext = result.hasNext;
            //res.detail = result.detail;
            return res;
        } catch (Exception e) {
            throw e;
        }
    }

}
