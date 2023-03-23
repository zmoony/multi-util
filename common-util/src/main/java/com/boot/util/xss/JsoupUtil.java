package com.boot.util.xss;

import org.aspectj.weaver.ast.Var;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

/**
 * Xss过滤工具
 *
 * @author yuez
 * @version 1.0.0
 * @className JsoupUtil
 * @date 2021/6/1 9:00
 **/
public class JsoupUtil {
    private static final Whitelist whitelist = Whitelist.basicWithImages();
    /**
    * 配置过滤化参数，不对代码进行格式化
    */
    private static final Document.OutputSettings outputSettings  = new Document.OutputSettings();

    static {
        whitelist.addAttributes(":all","style");
    }

    public static String clean(String content){
        return Jsoup.clean(content,"",whitelist,outputSettings);
    }
}
