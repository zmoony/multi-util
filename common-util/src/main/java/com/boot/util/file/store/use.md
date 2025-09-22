## 更新

```java
if (victim != null && victim.getId() != null) {
            // 如果更新了头像，删除旧头像
            Victim oldVictim = this.getById(victim.getId());
            if (oldVictim != null && StringUtils.isNotBlank(oldVictim.getHeadImage())
                    && !oldVictim.getHeadImage().equals(victim.getHeadImage())) {
                fileUploadUtil.deleteFile(oldVictim.getHeadImage());
            }
            updateById(victim);
        } else {
            throw new BusinessException("受害人信息不完整或未指定ID");
        }
```

## 删除
```java
 boolean remove = remove(new LambdaQueryWrapper<Victim>().in(Victim::getId, idList));
        if(remove){
            List<String> imageUrls = this.listByIds(idList).stream()
                    .map(Victim::getHeadImage)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList());
            fileUploadUtil.batchDeleteImages(imageUrls);
        }
```
