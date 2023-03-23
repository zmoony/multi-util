package com.boot.es.demo.pyIkSuggest;

import com.boot.util.common.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * doc实体类
 *
 * @author yuez
 * @since 2023/3/23
 */
@Data
@NoArgsConstructor
public class HotelDoc {
    private Long id;
    private String name;
    private String address;
    private Integer price;
    private Integer score;
    private String brand;
    private String city;
    private String starName;
    private String business;
    private String location;
    private String pic;
    private Object distance;
    private Boolean isAD;
    private List<String> suggestion;

    public List<String> getSuggestion() {
        if (!StringUtils.isEmpty(this.business)) {
            String[] arr = this.business.split("/");
            this.suggestion = new ArrayList<>();
            this.suggestion.add(this.brand);
            Collections.addAll(this.suggestion,arr);
        }
        return suggestion;
    }
}
