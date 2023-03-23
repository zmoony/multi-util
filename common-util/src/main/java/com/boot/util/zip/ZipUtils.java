package com.boot.util.zip;

import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author yuez
 * @title: ZipUtils
 * @projectName PSMP3
 * @description: zip工具类
 * @date 2022/1/24 13:43
 */
@Log4j2
public class ZipUtils {

    /**
     * 解压文件
     *
     * @param file      压缩文件
     * @param targetDir 解压文件输出目录
     */
    public static void unzip(Path file, Path targetDir) {
        try {
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }
            ZipFile zipFile = new ZipFile(file.toFile());
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(file))) {
                ZipEntry zipEntry = null;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    String entryName = zipEntry.getName();
                    Path entryFile = targetDir.resolve(entryName);
                    if (zipEntry.isDirectory()) {
                        if (!Files.isDirectory(entryFile)) {
                            Files.createDirectories(entryFile);
                        }
                    } else {
                        try (InputStream inputStream = zipFile.getInputStream(zipEntry)) {
                            try (OutputStream outputStream = Files.newOutputStream(entryFile, StandardOpenOption.CREATE_NEW)) {
                                final byte[] bytes = new byte[4096];
                                int len = 0;
                                while ((len = inputStream.read()) != -1) {
                                    outputStream.write(bytes, 0, len);
                                }
                                outputStream.flush();
                            }
                        }
                    }
                }
            } finally {
                zipFile.close();
            }
        } catch (IOException e) {
            log.error("解压文件错误：{}", e);
        }
    }

    /**
     * 压缩指定文件
     * @param files   目标文件
     * @param zipFile 生成的压缩文件
     */
    public static void zip(Path[] files, Path zipFile) {
        try (OutputStream outputStream=Files.newOutputStream(zipFile, StandardOpenOption.CREATE_NEW);
             ZipOutputStream zipOutputStream= new ZipOutputStream(outputStream)){
            for (Path file : files) {
                if (Files.isDirectory(file)) {
                    continue;
                }
                try (InputStream inputStream = Files.newInputStream(file)){
                    final ZipEntry zipEntry = new ZipEntry(file.getFileName().toString());
                    zipOutputStream.putNextEntry(zipEntry);
                    int len=0;
                    final byte[] bytes = new byte[1024 * 10];
                    while ((len = inputStream.read(bytes))!=-1){
                        zipOutputStream.write(bytes,0,len);
                    }
                }
            }
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            log.error("压缩文件失败：{}", e);
        }
    }

    /**
     * 压缩指定目录 [耗时教快]
     * @param folder 源目录
     * @param zipFile 目标文件
     */
    public static void zip(Path folder, Path zipFile){
        if (!Files.isDirectory(folder)) {
            throw new IllegalArgumentException(folder.toString() + "不是合法的文件夹");
        }
        try(OutputStream outputStream = Files.newOutputStream(zipFile, StandardOpenOption.CREATE_NEW);
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)){
            LinkedList<String> path = new LinkedList<>();
            Files.walkFileTree(folder, new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (!dir.endsWith(folder)) {
                        String folder = dir.getFileName().toString();
                        path.add(folder);
                        ZipEntry zipEntry = new ZipEntry(path.stream().collect(Collectors.joining("/", "", "/")));
                        try {
                            zipOutputStream.putNextEntry(zipEntry);
                            zipOutputStream.flush();
                        } catch (IOException e){
                            throw new RuntimeException(e);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try (final InputStream inputStream = Files.newInputStream(file)){
                        String fileName = path.size() >0
                                ?path.stream().collect(Collectors.joining("/","",""))+"/"+file.getFileName().toString()
                                :file.getFileName().toString();
                        final ZipEntry zipEntry = new ZipEntry(fileName);
                        zipOutputStream.putNextEntry(zipEntry);
                        int len = 0;
                        final byte[] bytes = new byte[1024 * 10];
                        while ((len = inputStream.read(bytes)) != -1) {
                            zipOutputStream.write(bytes,0,len);
                        }
                        zipOutputStream.flush();
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (!path.isEmpty()) {
                        path.removeLast();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            zipOutputStream.closeEntry();
        }catch (Exception e){
            log.error("压缩文件失败：{}",e);
        }
    }

    /**
     * 解压
     *
     * @param file
     * @return
     */
    public static boolean unzip(File file) {
        ZipFile zipFile = null;
        try {
            if (!file.exists()) {
                log.error("源文件不存在");
                return false;
            }
            String name = file.getName().replace(".zip", "");
            String path = file.getPath();
            String savePath = path.substring(0, path.lastIndexOf(".")) + File.separator;
            new File(savePath).mkdirs();
            zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                String fileName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    new File(savePath, fileName).mkdirs();
                } else {
                    File target = new File(savePath, fileName);
                    if (!target.getParentFile().exists()) {
                        target.getParentFile().mkdirs();
                    }
                    target.createNewFile();
                    InputStream inputStream = zipFile.getInputStream(zipEntry);
                    FileOutputStream fileOutputStream = new FileOutputStream(target);
                    byte[] bytes = new byte[1024];
                    int len;
                    while ((len = inputStream.read(bytes)) != -1) {
                        fileOutputStream.write(bytes, 0, len);
                    }
                    fileOutputStream.close();
                    inputStream.close();
                }

            }
        } catch (Exception e) {
            log.error("解压失败：{}", e);
        }
        return false;
    }

    /**
     * 将fileToZip文件夹及其子目录文件递归压缩到zip文件中
     *
     * @param fileTozip 递归当前处理对象，可能是文件夹，也可能是文件
     * @param fileName  fileToZip文件或文件夹名称
     * @param zos       压缩文件输出流
     * @bug 压缩大文件时间较慢，且会造成文件损坏
     */
    public static boolean zip(File fileTozip, String fileName, ZipOutputStream zos) {
        try {
            if (fileTozip.isHidden()) {
                return false;
            }
            if (fileTozip.isDirectory()) {
                if (fileName.endsWith("/")) {
                    zos.putNextEntry(new ZipEntry(fileName));
                    zos.closeEntry();
                } else {
                    zos.putNextEntry(new ZipEntry(fileName + "/"));
                    zos.closeEntry();
                }
                File[] children = fileTozip.listFiles();
                for (File childFile : children) {
                    zip(childFile, fileName + "/" + childFile.getName(), zos);
                }
                return true;
            }
            FileInputStream fis = new FileInputStream(fileTozip);
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);
            byte[] bytes = new byte[2048];
            int len;
            while ((len = fis.read()) != -1) {
                zos.write(bytes, 0, len);
            }
            fis.close();
        } catch (Exception e) {
            log.error("压缩文件失败：{}", e);
        }
        return false;
    }


    public static void main(String[] args) throws FileNotFoundException {
        String name = "D:\\project\\code\\wiscom\\20190525\\PSMP3\\ftp\\upload\\psmp_BigData7";
        String nameZip = "D:\\project\\code\\wiscom\\20190525\\PSMP3\\ftp\\upload\\psmp_BigData7_2.zip";
//        unzip(new File(name));
       zip(Paths.get(name),Paths.get(nameZip));
    }
}
