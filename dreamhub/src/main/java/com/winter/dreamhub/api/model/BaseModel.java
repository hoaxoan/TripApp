package com.winter.dreamhub.api.model;

/**
 * Created by hoaxoan on 6/2/2016.
 */
public abstract class BaseModel {
    public final long id;
    public final String title;
    public String url; // can't be final as some APIs use different serialized names
    public String dataSource;
    public int page;
    public float weight; // used for sorting
    public int colspan;

    public BaseModel(long id,
                     String title,
                     String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    @Override
    public String toString() {
        return title;
    }

    /**
     * Equals check based on the id field
     */
    @Override
    public boolean equals(Object o) {
        return (o.getClass() == getClass() && ((BaseModel) o).id == id);
    }
}
