package com.boot.template.web;

import com.boot.template.web.exception.ServiceException;
import com.boot.template.web.vo.UserCreateRequestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * DemoService
 *
 * @author yuez
 * @since 2024/2/6
 */
@Service
@Slf4j
public class DemoService {
    /**
     * 创建用户
     *
     * @param requestVO 请求参数
     */
    public void createUser(UserCreateRequestVO requestVO) {
//        final UserDO userDO = userManager.selectUserByName(requestVO.getUserName());
        if (requestVO != null) {
            throw new ServiceException(StatusEnum.PARAM_INVALID, "用户名已存在");
        }
    }


}
