package com.boot.util.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;

/**
 * FontFindAndDown
 *
 * @author yuez
 * @since 2024/11/14
 */
public class PathWalkUtil {
    static String filePath = "D:\\project\\code\\前端\\字体-WEB\\chinese-free-web-font-storage\\packages";
    static String savePath = "C:\\Users\\yuez\\Desktop\\font";

    public static void main(String[] args) {
        Path path = Paths.get(filePath);
        bfsWalk(path, 8);
    }

    public static void dfsWalk(Path root,int maxDepth){
        try(Stream<Path> stream = Files.walk(root,4)){
            //知道ttf结尾的，保存在另一个文件夹
            stream.filter(p -> p.toString().endsWith(".ttf"))
                    .forEach(p -> {
                        try {
                            Files.copy(p, Paths.get(savePath, p.getFileName().toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 深度检索
     * @param root
     * @param maxDepth
     */
    public static void dfsWalk2(Path root,int maxDepth){
        try ( Stream<Path> list = Files.list(root);){
            list.forEach(p -> {
                //判断是否是文件夹
                if (Files.isDirectory(p)){
                    //递归调用
                    dfsWalk2(p,maxDepth);
                }else{
                    if (p.toString().endsWith(".ttf")){
                        System.out.println(p.getFileName());
                    }
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void bfsWalk(Path root,int maxDepth){
        Queue<PathEntry> queue = new LinkedList();
        queue.add(new PathEntry(root,0));
        while (!queue.isEmpty()){
            PathEntry poll = queue.poll();
            Path path = poll.path;
            int depth = poll.depth;
            if (depth > maxDepth){
                continue;
            }
            if (Files.isDirectory(path)){
                try (Stream<Path> list = Files.list(path)){
                    list.forEach(p -> {
                        if (Files.isDirectory(p)){
                            queue.add(new PathEntry(p,depth+1));
                        }else{
                            System.out.println(p.getFileName().toString());
//                            if (p.toString().endsWith(".ttf")){
//                                System.out.println(p.getFileName().toString());
//                            }
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                System.out.println(path.getFileName().toString());
            }
        }
    }

    public static void bfsWalk2(Path root,int maxDepth){
        Queue<PathEntry> queue = new LinkedList();
        queue.add(new PathEntry(root,0));
        while (!queue.isEmpty()){
            PathEntry entry = queue.poll();
            try {
                Files.list(entry.path).forEach(p -> {
                    if (Files.isDirectory(p)){
                        queue.add(new PathEntry(p,entry.depth+1));
                    }else{
                        if (p.toString().endsWith(".ttf")){
                            System.out.println(p.getFileName());
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        }



}
class PathEntry {
    Path path;
    int depth;

    PathEntry(Path path, int depth) {
        this.path = path;
        this.depth = depth;
    }
}
