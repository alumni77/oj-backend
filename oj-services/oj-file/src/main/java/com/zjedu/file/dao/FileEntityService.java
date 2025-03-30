package com.zjedu.file.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjedu.pojo.entity.common.File;

/**
 * @Author Zhong
 * @Create 2025/3/30 20:16
 * @Version 1.0
 * @Description
 */

public interface FileEntityService extends IService<File>
{
    int updateFileToDeleteByUidAndType(String uid, String type);

}
