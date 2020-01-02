package com.metro.nccc.migration.utils.ibatis;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-11-14 下午3:25
 */
@Data
public class ArrayObject<T> {
    T[] data;

    public ArrayObject(T[] data) {
        this.data = data;
    }

    public ArrayObject(List<T> data) {
        this.data = (T[]) data.toArray();
    }

    public ArrayObject(T userId) {
        this.data = (T[]) Arrays.asList(userId).toArray();

    }

    @Override
    public String toString() {
        if (data == null || data.length == 0) {
            return "null";
        }
        return "array"
                + Arrays.toString(data);
    }
}
