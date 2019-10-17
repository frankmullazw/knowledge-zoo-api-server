package edu.monash.knowledgezoo.api.repository.model;

import edu.monash.knowledgezoo.api.repository.entity.Apk;

import java.util.Collection;

public class SearchResponse {
    public Collection<Apk> apks;
    public String type;
    public Object detail;
    public Boolean hasNext;
}
