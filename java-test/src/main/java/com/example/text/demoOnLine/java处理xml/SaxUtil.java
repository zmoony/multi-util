package com.example.text.demoOnLine.java处理xml;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;

/**
 * 不需要等待所有的数据被处理，解析就可以开始
 * 只在读取数据时检查数据，不需要保存在内存中
 * 可以在某一个条件满足时停止解析，不必要解析整个文档
 * 效率和性能较高，能解析大于系统内存的文档
 *
 * 解析逻辑复杂，需要应用层自己负责逻辑处理，文档越复杂程序越复杂
 * 单向导航，无法定位文档层次，很难同时同时访问同一文档的不同部分数据，不支持 XPath
 * @author yuez
 * @since 2023/2/24
 */
public class SaxUtil {
    private static final File xmlFile = new File("./text/src/main/java/com/example/text/demoOnLine/java处理xml/hello.xml");

    public static void main(String[] args) {
        ParsingXml();
    }

    /**
     * 获取一个 SAXParserFactory 的实例
     * 通过 factory() 获取 SAXParser 实例
     * 创建一个 handler() 对象
     * 通过 parser 的 parse() 方法来解析 XML
     */
    private static void ParsingXml(){
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            SAXParserSelfHandler handler = new SAXParserSelfHandler();
            parser.parse(xmlFile,handler);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
class SAXParserSelfHandler extends DefaultHandler {
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        System.out.println("开始元素："+qName+"--"+localName +"---"+uri+"---"+attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println("结束元素："+qName+"--"+localName +"---"+uri);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String str = new String(ch, start, length);
        System.out.println("charscters:"+str);
    }
}
