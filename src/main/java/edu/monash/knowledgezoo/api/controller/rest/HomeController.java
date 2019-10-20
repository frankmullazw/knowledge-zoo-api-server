package edu.monash.knowledgezoo.api.controller.rest;

import edu.monash.knowledgezoo.api.repository.model.GraphStateResponse;
import edu.monash.knowledgezoo.api.service.ApiService;
import edu.monash.knowledgezoo.api.service.ApkService;
import edu.monash.knowledgezoo.api.service.PermissionService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/")
@CrossOrigin(origins = "*")
public class HomeController {
    private final ApkService apkService;
    private final ApiService apiService;
    private final PermissionService permissionService;

    public HomeController(ApkService apkService, ApiService apiService, PermissionService permissionService) {
        this.apkService = apkService;
        this.apiService = apiService;
        this.permissionService = permissionService;
    }

    @GetMapping("/graphState")
    public GraphStateResponse graphState() {
        try {
            GraphStateResponse res = new GraphStateResponse();
            res.numApk = apkService.getTotalNumberOfApks();
            res.numApi = apiService.getTotalNumberOfApis();
            res.numPermission = permissionService.getTotalNumberOfPermissions();
            res.top10Apis = apiService.getTop10Apis();
            res.top10Permissions = permissionService.getTop10Permissions();
            return res;
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/allSuggestion")
    public List<String> getAllSuggestion() {
        try {
            List<String> res = new ArrayList<>();
            res.addAll(apiService.getAllApiName());
            res.addAll(permissionService.getAllPermissionName());
            return res;
        } catch (Exception e) {
            throw e;
        }
    }
}
