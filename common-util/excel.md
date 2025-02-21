## controller
```java
    /**
     * 导入
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public RetrunJson importExcel(@RequestPart(value = "file") MultipartFile file) {
        RetrunJson retrunJson=new RetrunJson();
        deptService.importExcel(file,retrunJson);
        return retrunJson;
    }

    /**
     * 模板下载
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void exportExcel(HttpServletResponse response) {
        deptService.exportExcel(response);
    }

```

## service
```java
/**
     * 批量导入部门
     * @param file
     * @param retrunJson
     */
    public void importExcel(MultipartFile file, RetrunJson retrunJson) {
        if(file==null || file.isEmpty() ){
            retrunJson.setMessage("文件未上传");
            return;
        }
        if(file.getOriginalFilename().endsWith(".xls") || file.getOriginalFilename().endsWith(".xlsx") || file.getOriginalFilename().endsWith(".csv")){

        }else{
            retrunJson.setMessage("文件格式错误");
            return;
        }
        retrunJson.setMessage("导入失败");
        PostgresqlDao postgresqlDao=new PostgresqlDao();
        try(InputStream inputStream = file.getInputStream();Workbook workbook = WorkbookFactory.create(inputStream);) {
            Sheet sheetAt = workbook.getSheetAt(0);
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 1; i <= sheetAt.getLastRowNum(); i++) {
                Row row = sheetAt.getRow(i);
                if(row==null){
                    continue;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("dept_id", System.currentTimeMillis()+(int)(Math.random()*10));
                map.put("dept_name", row.getCell(0).getStringCellValue());
                map.put("short_name", row.getCell(1).getStringCellValue());
                list.add(map);
            }
            if(postgresqlDao.doInsert(list,GlobalObject.properties_business.getProperty("postgresql.schema.sys")+".user_dept")){
                retrunJson.setMessage("导入成功");
                retrunJson.setStatus("success");
            }
        }catch (Exception e){
            log.error("导入部门失败",e);
            retrunJson.setMessage("导入部门失败");
        }
    }

    /**
     * 导出excel模板，包含两列 单位名称，单位简称
     * @param response
     */
    public void exportExcel(HttpServletResponse response) {
        RetrunJson retrunJson = new RetrunJson();
        try (Workbook workbook = new XSSFWorkbook(); OutputStream outputStream = response.getOutputStream()){
            Map<String, String> map = new HashMap<>();
            map.put("0", "单位名称");
            map.put("1", "单位简称");
            String title = "部门信息模板";
            Sheet sheet = workbook.createSheet(title);
            CellStyle style1 = workbook.createCellStyle();
            style1.setAlignment(HorizontalAlignment.CENTER);
            style1.setBorderBottom(BorderStyle.THICK);
            style1.setBorderLeft(BorderStyle.THICK);
            style1.setBorderRight(BorderStyle.THICK);
            style1.setBorderTop(BorderStyle.THICK);
            Font font1 = workbook.createFont();
            font1.setBold(true);
            style1.setFont(font1);
            sheet.setDefaultColumnWidth(35);

            Row row = sheet.createRow(0);
            for (int i = 0; i < map.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellStyle(style1);
                cell.setCellValue(map.get(i + ""));
            }
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(title+".xlsx", "UTF-8"));
            workbook.write(outputStream);
            outputStream.flush();
        }catch (Exception e){
            log.error("导出部门模板失败",e);
            retrunJson.setStatus("error");
            retrunJson.setMessage("导出部门模板失败");
            try {
                String jsonStr = objectMapper.writeValueAsString(retrunJson);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getOutputStream().write(jsonStr.getBytes());
                response.getOutputStream().close();
            } catch (Exception ex) {
                log.error("获取输出流失败",ex);
            }
        }

    }

```

## 前端

#### 下载模板
```js
const onTemplateDownTxt = () => {
    request.post("/buyingzhe/dept/export", {}, {responseType: 'blob'}).then(response => {
        if (response.status === 200) {
            let disposition = response.headers["content-disposition"]
            const regex = /filename[^;=\n]*=((['"]).*?\2|[^;=\n]*)/;
            const match = disposition.match(regex);
            let filename = '部门导入模板.xlsx';
            if (match && match[1]) {
                filename = decodeURIComponent(match[1].replace(/['"]/g, ''));
            }
            let blob = new Blob([response.data], {type: 'application/vnd.ms-excel'});
            let a = document.createElement('a');
            let url = URL.createObjectURL(blob);
            a.href = url
            a.download = filename;
            a.click();
            document.body.removeChild(a);
            URL.revokeObjectURL(url);
        }
    })
}
```

#### 上传文件
```vue
<template>
        <el-upload
                :data="uploadData"
                class="upload-demo"
                :action="url_file_upload"
                :headers="headers"
                :auto-upload="true"
                accept=".xlsx"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :on-error="errorMessage"
            >
                <el-button style=" margin-left: 0.625vw;" type="primary" :icon="Upload" plain >批量导入</el-button>
            </el-upload>
</template>

<script>
const uploadData = {
    create_user: useUserStore().getUser.id,
    create_dept: useUserStore().getUser.dept_id
}
const url_file_upload = useUserStore().getDefaultBaseUrl + "/buyingzhe/dept/import";
const headers = {
    'Authorization': useUserStore().getToken
}
const imageUrl = ref('')
const handleAvatarSuccess: UploadProps['onSuccess'] = (
    response,
    uploadFile
) => {
    if (response.status === "success") {
        ElMessage.success(response.message)
        queryList();
    }
}


const errorMessage = (response) => {
    return ElMessage({
        message: "上传失败",
        type: "error",
        offset: 60
    });
};

</script>
```

