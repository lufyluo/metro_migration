package com.metro.nccc.migration.utils.maplain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Random;

/**
 * @Author lufy
 * @Description ...
 * @Date 19-10-25 上午9:59
 */
@Getter
@Setter
public class Ancestors<T extends Ancestors> implements Comparable<T>{
    protected Long id;
    protected Long parentId;
    protected List<T> children;

    @Override
    public int compareTo(T o) {
        Random random=new Random();
        if(o.getId().intValue()==this.getParentId().intValue()){
            return 1;
        }
        return -1;
    }
}
