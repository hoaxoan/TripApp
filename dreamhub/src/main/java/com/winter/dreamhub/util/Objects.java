package com.winter.dreamhub.util;

import java.util.Arrays;

/**
 * Created by hoaxoan on 6/4/2016.
 */
public final class Objects {
    public static boolean equal(Object obj1, Object obj2) {
        return (obj1 == obj2) || ((obj1 != null) && (obj1.equals(obj2)));
    }

    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }
}
