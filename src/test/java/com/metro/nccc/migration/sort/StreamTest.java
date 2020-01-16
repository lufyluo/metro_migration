package com.metro.nccc.migration.sort;

import com.metro.nccc.migration.utils.maplain.Ancestors;
import com.metro.nccc.migration.utils.maplain.MaplainUtil;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author lufy
 * @Description ...
 * @Date 20-1-9 下午8:25
 */
public class StreamTest {
    @Test
    public void sortTest(){
        List<Ancestors> list = build();
        list = list.stream().sorted(Ancestors::compareTo).collect(Collectors.toList());
        System.out.println(Arrays.toString(list.toArray()));
    }

    private List<Ancestors> build1(){
        List<Ancestors> list = new ArrayList<>();
        for (Long i =0L;i<=4;i++){
            Ancestors ancestors =  new Ancestors();
            ancestors.setId(i);
            ancestors.setParentId(i+1);
            list.add(ancestors);
        }
        List<Ancestors> result = new ArrayList<>();
        result.add(list.get(4));
        result.add(list.get(3));
        result.add(list.get(2));
        result.add(list.get(1));
        return result;
    }

    private List<Ancestors> build(){
        List<Ancestors> list = new ArrayList<>();
        for (Long i =0L;i<=4;i++){
            Ancestors ancestors =  new Ancestors();
            ancestors.setId(i);
            ancestors.setParentId(i+1);
            list.add(ancestors);
        }
        List<Ancestors> result = new ArrayList<>();
        result.add(list.get(1));
        result.add(list.get(3));
        result.add(list.get(0));
        result.add(list.get(2));
        return result;
    }

    @Test
    public void flatTest(){
        List<Ancestors> flatData = build();
        List<Ancestors> list = MaplainUtil.toHierarchy(flatData);
        List<Ancestors> ids = MaplainUtil.flat(list).stream().collect(Collectors.toList());
        //list = list.stream().map(Ancestors::getChildren).flatMap(flat->flat.stream()).collect(Collectors.toList());
        //list = list.stream().map(Ancestors::getChildren).flatMap(flat->Arrays.stream((Ancestors[])flat.toArray())).collect(Collectors.toList());
        System.out.println(Arrays.toString(list.toArray()));
    }

    @Test
    public void stringFlatTest(){
        String[] words = new String[]{"Hello","World"};
        List<String> a = Arrays.stream(words)
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());
        System.out.println(ArrayUtils.toString(a));
    }

    @Test
    public void refTest(){
        List<Ancestors> list = build();
        System.out.println("begin: " + list.size());
        refChange(list);
        System.out.println("end : " + list.size());
    }
    private void refChange( List<Ancestors> list){
        list = new ArrayList<>();
        list.add(new Ancestors());
        System.out.println("in: " + list.size());
    }
}
