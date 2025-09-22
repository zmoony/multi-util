package com.example.boot3.jdk17;

/**
 * 从 JDK 13开始，也像 Python 那样，支持三引号字符串了，所以再有上面的 JSON 字符串的时候，就可以直接这样声明了。
 *
 * @author yuez
 * @since 2023/2/15
 */
public class TextBlockMain {
    public static void main(String[] args) {
        String json = """
                {
                    "name":"测试文本块",
                    "time":20230215
                }
                """;
        System.out.println(json);
    }
}
