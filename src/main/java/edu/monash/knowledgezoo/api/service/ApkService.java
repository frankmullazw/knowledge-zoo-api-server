package edu.monash.knowledgezoo.api.service;

import edu.monash.knowledgezoo.api.repository.ApkRepository;
import edu.monash.knowledgezoo.api.repository.entity.Api;
import edu.monash.knowledgezoo.api.repository.entity.Apk;
import edu.monash.knowledgezoo.api.repository.entity.Permission;
import edu.monash.knowledgezoo.api.repository.model.PageFindAPKsByLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ApkService {

    private final ApkRepository apkRepository;
    private final ReleaseTagService releaseTagService;
    private final PermissionService permissionService;
    private final ApiService apiService;
    private final Integer size = 10;

    public ApkService(ApkRepository apkRepository, ReleaseTagService releaseTagService, PermissionService permissionService, ApiService apiService) {
        this.apkRepository = apkRepository;
        this.releaseTagService = releaseTagService;
        this.permissionService = permissionService;
        this.apiService = apiService;
    }

    @Transactional(readOnly = true)
    public Apk findBySha256name(String sha256name) {
        return apkRepository.findBySha256(sha256name);
    }

    @Transactional(readOnly = true)
    public Collection<Apk> findByNameLike(String name) {
        return apkRepository.findByNameLike(name);
    }

    @Transactional(readOnly = true)
    public long getTotalNumberOfApks() {
        return apkRepository.count();
    }

    @Transactional(readOnly = true)
    public PageFindAPKsByLike findByPermissionLike(String keyword, Integer pageIdx) {
        PageFindAPKsByLike res = new PageFindAPKsByLike();
        Permission permission = permissionService.findByGenericNameLike(keyword);
        if (permission == null) {
            return res;
        }
        Pageable page = PageRequest.of(pageIdx, size);
        Page<Apk> pagination = apkRepository.findByPermissionGenericName(permission.getGenericName(), page);
        List<Apk> apks = new ArrayList<>();
        for (Apk a : pagination.getContent()) {
            apks.add(apkRepository.findBySha256(a.getSha256()));
        }
        res.apks = apks;
        res.hasNext = pagination.hasNext();
        res.detail = permission;
        return res;
    }

    @Transactional(readOnly = true)
    public PageFindAPKsByLike findByApiNameLike(String keyword, Integer pageIdx) {
        PageFindAPKsByLike res = new PageFindAPKsByLike();
        Api api = apiService.findByNameLike(keyword);
        api.introduceTag = releaseTagService.findIntroduceTagOfApiByName(api.getName());
        api.removeTag = releaseTagService.findRemoveTagOfApiByName(api.getName());
        if (api == null) {
            return res;
        }
        Pageable page = PageRequest.of(pageIdx, size);
        Page<Apk> pagination = apkRepository.findByApiName(api.getName(), page);
        List<Apk> apks = new ArrayList<>();
        for (Apk a : pagination.getContent()) {
            apks.add(apkRepository.findBySha256(a.getSha256()));
        }
        res.apks = apks;
        res.hasNext = pagination.hasNext();
        res.detail = api;
        return res;
    }
}
