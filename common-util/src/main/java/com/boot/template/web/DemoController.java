package com.boot.template.web;

import com.boot.template.web.vo.UserCreateRequestVO;
import com.boot.template.web.vo.UserInfoResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * DemoController
 *
 * @author yuez
 * @since 2024/2/6
 */
@RestController
@RequestMapping("user")
@Validated
@Slf4j
@SuppressWarnings({"all"})
public class DemoController {
//    private IUserService userService;
    /**
     * 创建用户
     *
     * @param requestVO
     * @return
     */
    @PostMapping("create")
    public ResultResponse<Void> createUser(@Validated @RequestBody UserCreateRequestVO requestVO) {
        return ResultUtil.success(null);
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @GetMapping("info")
    public ResultResponse<UserInfoResponseVO> getUser(@NotBlank(message = "请选择用户") String userId) {
//        final UserInfoResponseVO responseVO = userService.getUserInfoById(userId);
        return ResultUtil.success(null);
    }

//    @Autowired
//    public void setUserService(IUserService userService) {
//        this.userService = userService;
//    }

}
