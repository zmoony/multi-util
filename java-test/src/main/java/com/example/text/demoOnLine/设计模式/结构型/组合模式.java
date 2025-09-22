package com.example.text.demoOnLine.设计模式.结构型;

import java.util.ArrayList;
import java.util.List;

/**
 * 组合模式
 * 结构型设计模式
 * 它允许将对象组合成树形结构，以表示“部分-整体”的层次关系。组合模式可以让客户端使用统一的方式处理单个对象和组合对象，从而简化了客户端的代码
 * (类似递归   对象里包含一个自己的list)
 *
 * @author yuez
 * @since 2023/4/24
 */
public class 组合模式 {
    public static void main(String[] args) {
        FileSystem root = new FileSystem("C:", false);
        FileSystem windowsFolder = new FileSystem("Windows", false);
        FileSystem programFilesFolder = new FileSystem("Program Files", false);
        FileSystem notepadFile = new FileSystem("notepad.exe", true);
        FileSystem javaFolder = new FileSystem("Java", false);
        FileSystem eclipseFolder = new FileSystem("Eclipse", false);
        FileSystem eclipseExeFile = new FileSystem("eclipse.exe", true);

        root.add(windowsFolder);
        root.add(programFilesFolder);
        programFilesFolder.add(javaFolder);
        javaFolder.add(eclipseFolder);
        eclipseFolder.add(eclipseExeFile);
        windowsFolder.add(notepadFile);

        root.display(0);
    }
}

class FileSystem {
    private String name;
    private boolean isFile;
    private List<FileSystem> children = new ArrayList<>();

    public FileSystem(String name, boolean isFile) {
        this.name = name;
        this.isFile = isFile;
    }

    public void add(FileSystem fileSystem) {
        children.add(fileSystem);
    }

    public void remove(FileSystem fileSystem) {
        children.remove(fileSystem);
    }

    public void display(int depth) {
        System.out.println("-".repeat(depth) + name);
        for (FileSystem fileSystem : children) {
            fileSystem.display(depth + 2);
        }
    }




}




