package edu.monash.knowledgezoo.api.repository.model;

import edu.monash.knowledgezoo.api.repository.entity.Apk;

import java.util.ArrayList;
import java.util.List;

public class PageFindAPKsByLike {
    public Boolean hasNext = false;
    public List<Apk> apks = new ArrayList<>();
    public Object detail = new Object();
}
