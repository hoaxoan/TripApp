package com.winter.dreamhub.api.model;

import java.util.Comparator;
import java.util.List;

/**
 * Classes related to sorting {@link BaseModel}s.
 */
public class BaseModelSorting {

    /**
     * A comparator that compares {@link BaseModel}s based on their {@code weight} attribute.
     */
    public static class PlaidItemComparator implements Comparator<BaseModel> {

        @Override
        public int compare(BaseModel lhs, BaseModel rhs) {
            return Float.compare(lhs.weight, rhs.weight);
        }
    }

    /**
     * Interface for weighing a group of {@link BaseModel}s
     */
    public interface PlaidItemGroupWeigher<T extends BaseModel> {
        void weigh(List<T> items);
    }

    /**
     * Applies a weight to a group of {@link BaseModel}s according to their natural order.
     */
    public static class NaturalOrderWeigher implements PlaidItemGroupWeigher<BaseModel> {

        @Override
        public void weigh(List<BaseModel> items) {
            final float step = 1f / (float) items.size();
            for (int i = 0; i < items.size(); i++) {
                BaseModel item = items.get(i);
                item.weight = item.page + ((float) i) * step;
            }
        }
    }
}
