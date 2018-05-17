package com.winter.dreamhub.api.service.model;

import com.winter.dreamhub.api.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 12/4/2016.
 */

public class EntitySnippets extends BaseModel {

    private List<EntitySnippet> entities;
    private int totalRecord;
    private int pageSize;

    public EntitySnippets(long id, String title, String url) {
        super(id, title, url);
        entities = new ArrayList<EntitySnippet>();
    }

    public List<EntitySnippet> getEntitySnippets() {
        return entities;
    }

    public void getEntitySnippets(List<EntitySnippet> entities) {
        this.entities = entities;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
