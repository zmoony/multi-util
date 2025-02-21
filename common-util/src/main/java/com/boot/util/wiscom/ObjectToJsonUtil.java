/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boot.util.wiscom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

/**
 *
 * @author Administrator
 */
@Log4j2
public class ObjectToJsonUtil {
    public static String objectToJson(Object object){
        String json = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            json=mapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            log.error("Object解析成json失败："+ex.getMessage());
        }
        return json;
    }
}
