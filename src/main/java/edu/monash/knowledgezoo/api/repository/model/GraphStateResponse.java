package edu.monash.knowledgezoo.api.repository.model;

import edu.monash.knowledgezoo.api.repository.entity.Api;
import edu.monash.knowledgezoo.api.repository.entity.Permission;

import java.util.ArrayList;
import java.util.Collection;

public class GraphStateResponse {
    public long numApi = 0;
    public long numApk = 0;
    public long numPermission = 0;
    public Collection<Api> top10Apis = new ArrayList<>();
    public Collection<Permission> top10Permissions = new ArrayList<>();
}
