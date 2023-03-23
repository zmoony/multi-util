/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boot.util.file;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 *
 * @author 13900
 */
@Controller
@RequestMapping("/data/file/local/upload")
@Log4j2
public class FileLocalUploadControl {
    @RequestMapping("multi")
    @ResponseBody
    public ResponseCommonBean dataFileLocalUploadMulti(String path, HttpServletRequest request) {
        ResponseCommonBean responseCommonBean = new ResponseCommonBean();
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = multipartRequest.getFiles("files");
            StringBuilder filenames=new StringBuilder();
            try{
                String newpath=new File(".").getCanonicalPath()+File.separator+path+File.separator;
                for(MultipartFile file:files){
                    String filePath=newpath+file.getOriginalFilename();
                    filenames.append(filePath).append(",");
                    File newfile=new File(filePath);
                    file.transferTo(newfile);
                    
                }
                filenames.deleteCharAt(filenames.length()-1); //删除最后逗号
                responseCommonBean.setFlag(true);
                responseCommonBean.setDescrible(filenames.toString());
            }catch(Exception ex){
                log.error("",ex);
                responseCommonBean.setDescrible("上传失败："+ex.getMessage());
            }
            //dataImageIntegrate.upload(responseCommonBean,file);
        }
        return responseCommonBean;
    }
}

@Data
class ResponseCommonBean<T> {
    private boolean flag;
    private String describle;
    private List<T> stdOuts;
    private Map<String,Object> content;
    private long total;
}