package com.boot.websocket.controller;

import com.boot.websocket.vo.Params;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * MutilPartController
 *
 * @author yuez
 * @since 2024/11/4
 */
@RestController
@RequestMapping(value = "/mutil")
@CrossOrigin
public class MutilPartController {
    private static final String UPLOAD_DIR = "uploads";
/*
    此方法无效，
    需要将file与参数分开。参数可以单独设置，也可以放在一个对象里，使用@ModelAttribute注解。

    @PostMapping(value = "/test")
    public String test(@RequestPart Params mutilPart) {
        System.out.println(mutilPart.getName());
        System.out.println(mutilPart.getFile().getSize());
        return "test";
    }
*/

    /**
     * 正常form传参，适用于复杂参数
     * @param file
     * @param mutilPart
     * @return
     */
    @PostMapping(value = "/test2")
    public String test2(@RequestPart MultipartFile file,  @ModelAttribute Params mutilPart) {
        System.out.println(mutilPart.getName());
        System.out.println(file.getSize());
        return "test";
    }

    /**
     * 适用于简单参数
     * @param file
     * @param name
     * @return
     */
    @PostMapping(value = "/test3")
    public String test3(@RequestPart MultipartFile file, @RequestPart String name) {
        System.out.println(name);
        System.out.println(file.getSize());
        return "test";
    }

    /**大文件上传
     * 下面为你详细介绍使用 Java 后端和 Vue 前端实现大文件上传的方案，该方案的核心思路是将大文件分割成多个小文件块分别上传，最后在后端将这些小文件块合并成完整的文件。
     * ***/
    @PostMapping(value = "/bigfile/chunk")
    public String bigfileChunk(@RequestPart MultipartFile file,
                          @RequestParam("hashHex") String hashHex,
                          @RequestParam("index") String index
                          ) {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filename = file.getOriginalFilename();
        try {
            String chunkFilename = filename + ".part" + hashHex +"_"+index;
            file.transferTo(Path.of(UPLOAD_DIR, chunkFilename));
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping(value = "/bigfile/merge")
    public String bigfileMerge(@RequestParam("hashHex") String hashHex,
                               @RequestParam("chunkCount") long chunkCount,
                               @RequestParam("filename") String filename) {
        //查找UPLOAD_DIR目录下.part文件
        File file = new File(UPLOAD_DIR+"/"+filename);
        try (FileOutputStream fos = new FileOutputStream(file, true)){
            for (int i = 0; i < chunkCount; i++) {
                String chunkFilename = filename + ".part" + hashHex +"_"+i;
                Path tempFile =Path.of(UPLOAD_DIR, chunkFilename);
                if (!tempFile.toFile().exists()) {
                    return "error";
                }
                byte[] chunkBytes = Files.readAllBytes(tempFile);
                fos.write(chunkBytes);
                Files.delete(tempFile);
            }
        }catch (Exception e){
            e.printStackTrace();
            return e.toString();
        }
        return "success";
    }

}

