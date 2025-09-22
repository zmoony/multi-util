package com.boot.util.tree;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 树状数据结构处理工具 默认不支持循环引用
 *
 * @author Lgd
 * @since 2023-10-10 10:05:40
 */
public class TreeUtil {

    private <T>  List<T> streamToTree(List<T> list, String parentId,Function<T, String> parentIdGetter) {
        return list.stream()
                // 过滤父节点
                .filter(parent -> parentIdGetter.apply(parent).equals(parentId))
                // 递归设置子节点
               /* .map(item -> {
                    item.setChildren(streamToTree(list,item.getDeptId()));
                    return item;
                })*/
                .collect(Collectors.toList());
    }

    /**
     * 线性集合 -> 多节点树状数据集合
     *
     * @param sourceList         线性集合
     * @param idGetter           id的getter方法引用
     * @param parentIdGetter     parentId的getter方法引用
     * @param childrenListSetter 子列表setter方法
     * @param <A>                树状类
     * @param <B>                id,parentId的数据类型
     * @return 多节点树状数据类型
     */
    public static <A, B> List<A> list2TreeList(List<A> sourceList, Function<A, B> idGetter, Function<A, B> parentIdGetter, BiConsumer<A, List<A>> childrenListSetter) {
        /**
         * 1. 找出所有parentId不在集合中的元素,作为根节点集合 rootList
         * 2. 遍历rootList,寻找他们的子元素,将子元素设置到 children 里
         *      2.1 如果这个children为空,那么 return
         *      2.2 如果这个children不为空,那么遍历里面这个 list ,重复上述操作
         */
        sourceList = sourceList.stream().filter(e -> Objects.nonNull(idGetter.apply(e))).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(sourceList)) {
            return null;
        }

        final List<B> idList = sourceList.parallelStream().filter(Objects::nonNull).map(idGetter).collect(Collectors.toList());

        List<A> rootList = sourceList.stream().filter(Objects::nonNull).filter(e -> {
            B parentId = parentIdGetter.apply(e);
            if (idList.contains(parentId)) {
                //父节点在集合中,那么就不能做根节点
                return false;
            }
            return true;
        }).collect(Collectors.toList());

        //出现了多个循环引用的情况,会直接返回
        if (CollectionUtils.isEmpty(rootList)) {
            return null;
        }

        //移除掉root 元素,提高效率
        sourceList.removeAll(rootList);

        if (CollectionUtils.isEmpty(sourceList)) {
            return rootList;
        }

        //构建树
        mkTree(rootList, sourceList, idGetter, parentIdGetter, childrenListSetter);

        return rootList;
    }

    /**
     * 多节点树状数据集合 -> 线性集合
     *
     * @param treeList           多节点树状
     * @param childrenListGetter childrenList 的 getter 方法
     * @param <A>                树状类
     * @return
     */
    public static <A> List<A> treeList2List(List<A> treeList, Function<A, List<A>> childrenListGetter) {
        final List<A> list = Lists.newArrayList();
        /**
         * 遍历rootList
         * 查询每个root是否有子children
         *      如果没有,那么就塞入目标集合
         *      如果有,那么就继续遍历,遍历到没有的就塞
         */
        undoTree2List(treeList, childrenListGetter, list);
        return list;
    }

    private static <A> void undoTree2List(List<A> treeList, Function<A, List<A>> childrenListGetter, List<A> targetList) {
        for (A father : treeList) {
            List<A> childrenList = childrenListGetter.apply(father);
            if (CollectionUtils.isNotEmpty(childrenList)) {
                undoTree2List(childrenList, childrenListGetter, targetList);
                childrenList.clear();
            }
            targetList.add(father);
        }
    }

    /**
     * 单节点树状数据 -> 线性集合
     *
     * @param tree 单根节点树型数据
     * @param <A>
     * @return
     */
    public static <A> List<A> tree2List(A tree, Function<A, List<A>> childrenListGetter) {
        return treeList2List(Lists.newArrayList(tree), childrenListGetter);
    }

    private static <A, B> void mkTree(List<A> fatherList, List<A> sourceList, Function<A, B> idGetter, Function<A, B> parentIdGetter, BiConsumer<A, List<A>> childrenListSetter) {
        if (sourceList.isEmpty()) {
            return;
        }
        for (A father : fatherList) {
            final B fatherId = idGetter.apply(father);
            List<A> sonList = sourceList.stream().filter(e -> fatherId.equals(parentIdGetter.apply(e))).collect(Collectors.toList());
            sourceList.removeAll(sonList);
            if (CollectionUtils.isNotEmpty(sonList)) {
                mkTree(sonList, sourceList, idGetter, parentIdGetter, childrenListSetter);
            }
            if (CollectionUtils.isEmpty(sonList)) {
                // 将所有为空，自定义为null
                sonList = null;
            }
            childrenListSetter.accept(father, sonList);
        }
    }


}
