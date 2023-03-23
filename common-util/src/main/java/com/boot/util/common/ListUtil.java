package com.boot.util.common;

import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuez
 * @title: ListUtil
 * @projectName muti-scaffold
 * @description: list--集合操作
 * @date 2022/5/12 10:13
 */
@Log4j2
public class ListUtil {

    /**
     * 分隔list
     * @param list 目标组
     * @param group 需要分成的组数
     * @return
     */
    public static List<List<?>> divisionList(List<?> list, int group) {
        List<List<?>> newList = new ArrayList<>();
        try {
            int totalLength = list.size();
            int groupSize = (totalLength / group) + 1;
            for (int i = 0; i < Math.min(group, totalLength); i++) {
                int fromIndex = Math.min(i * groupSize, totalLength);
                int endIndex = (i + 1) * groupSize < totalLength ? (i + 1) * groupSize : totalLength;
                newList.add(list.subList(fromIndex, endIndex));
            }
            return newList;
        } catch (Exception e) {
            log.error("分割数据异常：{}", e);
            newList.add(list);
            return newList;
        }
    }
}
