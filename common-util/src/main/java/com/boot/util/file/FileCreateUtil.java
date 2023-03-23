/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boot.util.file;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Administrator
 */
@Log4j2
public class FileCreateUtil {
    public static   void createDir(String path){
        File file=new File(path);
        if(!file.exists()){
            file.mkdir();
        }
    }
    
    public static boolean write(String filepath,String content){
        File file = new File(filepath);
        try {
            FileUtils.writeStringToFile(file, content,"UTF-8");
            return true;
        } catch (IOException ex) {
            log.error(ex.getMessage());
            return false; 
        }
        
        
    }
    public static String read(String filepath){
        File file = new File(filepath);
        String content = null;
        
        try {
            if(!file.exists()){
                file.createNewFile();
            }
           content=FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException ex) {
           log.error(ex.getMessage());
        }
        
        return content;
    }
    
    public static void readLogByJDK(List<String> result,int number){
        int count=0; //读取行数统计
        try{
            String path=new File(".").getCanonicalPath()+ File.separator +"log"+File.separator+"psmp.log";
            String lineSeparator=System.getProperty("line.separator");
            try(RandomAccessFile rf=new RandomAccessFile(path,"r")){
                long length = rf.length(); //文件长度
                if(length>0){
                    //初始化游标
                    long pos = length - 1;
                    while (pos > 0){
                        pos--;
                        rf.seek(pos);
                        if (rf.readByte() == '\n'){ //如果读到
                            
                            String line = new String(rf.readLine().getBytes("ISO-8859-1"));
                            log.info(line);
                            result.add(line);
                            count++;
                            if(count==number){ //需要读取的行数够了
                                break;
                            }
                        }
                        
                    }
                    if (pos == 0){
                        rf.seek(0);
                        result.add(rf.readLine());
                    }
                }
            }
            Collections.reverse(result);//反转
            
            
        }catch(Exception ex){
            log.error(ex.getMessage());
        }
    
    }

    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
       boolean flag = false;
       File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            flag=file.delete();
        }
        return flag;
    }

    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                //flag =files[i].delete();
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag){
                    log.error("删除失败："+files[i].getAbsolutePath());
                    break;
                }
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }





    
}
