package com.winter.dreamhub.api.service.model;

import com.winter.dreamhub.api.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 10/7/2017.
 */

public class Guidelines extends BaseModel {

    private String name;
    private String description;
    private long categoryId;
    private String categoryName;

    private List<Guideline> guidelines;

    public Guidelines(long id, String title, String url) {
        super(id, title, url);
        guidelines = new ArrayList<Guideline>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Guideline> getGuidelines() {
        return guidelines;
    }

    public void setGuidelines(List<Guideline> guidelines) {
        this.guidelines = guidelines;
    }

}
