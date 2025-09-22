package com.boot.util.file.store;

/**
 * FileUploadUtil
 *
 * @author yuez
 * @since 2025/5/8
 */

import com.example.backend.config.BusinessException;
import io.micrometer.common.util.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.UUID;

@Log4j2
@Component
public class FileUploadUtil {
    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.access.path}")
    private String accessPath;

    public String uploadImage(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + suffix;
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            File targetDir = new File(uploadPath + "/" + datePath);
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            file.transferTo(new File(targetDir, fileName));
            return accessPath + "/" + datePath + "/" + fileName;
        }catch (Exception e){
            log.error("上传图片失败",e);
            throw new BusinessException("文件上传失败");
        }

    }

    public boolean deleteFile(String fileUrl) {
        try {
            if (StringUtils.isBlank(fileUrl)) {
                return false;
            }
            String filePath = fileUrl.substring(fileUrl.indexOf(accessPath) + accessPath.length());
            File file = new File(uploadPath + filePath);
            if (file.exists()) {
                return file.delete();
            }
            return true;
        }catch (Exception e){
            log.error("删除图片失败：{}",fileUrl,e);
            return false;
        }
    }

    public void batchDeleteImages(Collection<String> fileUrls) {
        if (CollectionUtils.isEmpty(fileUrls)) {
            return;
        }
        for (String fileUrl : fileUrls) {
            try {
                deleteFile(fileUrl);
            } catch (Exception e) {
                log.error("批量删除文件失败: {}", fileUrl, e);
            }
        }
    }

}
