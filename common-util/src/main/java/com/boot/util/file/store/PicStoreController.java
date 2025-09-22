package com.boot.util.file.store;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PicStoreController
 *
 * @author yuez
 * @since 2025/5/8
 */
@RestController
@RequestMapping("/pic-store")
@AllArgsConstructor
public class PicStoreController {
    private final FileUploadUtil fileUploadUtil;

//    @PostMapping("/upload")
//    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return Result.error("文件不能为空");
//        }
//        String url = fileUploadUtil.uploadImage(file);
//        return Result.success(url);
//    }
}
