package com.boot.es.demo.pyIkSuggest;

import com.boot.es.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 拼音自动补齐
 *
 * @author yuez
 * @since 2023/3/23
 */
@RestController
@RequestMapping("hotel")
public class HotelController {
    private HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/list")
    public R hotelList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "1") Integer size,
            @RequestParam(value = "key" , defaultValue = "") String key
    ) throws IOException {
        List<Map<String, Object>> list = hotelService.list(page, size, key);
        return R.ok().data(list);
    }

    /**
     * 自动补齐推荐词
     * @param key 输入词
     * @return
     * @throws IOException
     */
    @GetMapping("/suggestion")
    public List<String> getSuggestion(@RequestParam("key") String key) throws IOException {
        return hotelService.getSuggestion(key);
    }


}
