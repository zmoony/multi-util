package com.example.text.demoOnLine.java处理xml;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.jdom.JDOMException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 性能很好
 * 大量使用 Java 集合类，开发简便，同时也提供了一些提高性能的代替方法
 * 支持 XPath
 * 需要注意的是，DOM4j会将整个XML文档加载到内存中，因此对于大型XML文件可能会占用大量的内存。
 * 因此在处理大型XML文件时，可以考虑使用基于事件驱动的SAX解析器，它可以逐行读取XML文件并在遇到节点时触发事件，
 * 避免将整个XML文档加载到内存中。
 * @author yuez
 * @since 2023/2/24
 */
public class Dom4jUtil {
    private static final File xmlFile = new File("./text/src/main/java/com/example/text/demoOnLine/java处理xml/hello.xml");
    private static final Map<String, Object> imageSearchedByImageObject = new HashMap<String, Object>(){{
        put("SearchID","321000000000122023022320073810000");
        put("RecordStartNo","1");
        put("MaxNumRecordReturn","500");
        put("PageRecordNum","10");
        put("Threshold","80");
        put("SearchType","1");
        put("QueryString","(passtime)");
        put("ResultImageDeclare","-1");
        put("ResultFeatureDeclare","-1");
        put("Image",new HashMap<String,Object>(){{
            put("Type",1);
            put("FileFormat","JPEG");
            put("Data","SDHUIOHDOSBHDHBCJXKCBJSDKDHFIOAUHFDUIAFHDSJANLKJFHA");
        }});
    }};

    public static void main(String[] args) throws Exception {
        createXml();
    }

    /**
     * 创建 SAXReader 的对象 reader
     * 通过 reader 对象的 read() 方法加载 books.xml 文件，获取 document 对象
     * 通过 document 对象获取根节点 bookstore
     * 通过 element 对象的 elementIterator() 获取迭代器
     * 遍历迭代器，获取根节点中的信息
     * 获取 book 的属性名和属性值
     * 通过 book 对象的 elementIterator() 获取节点元素迭代器
     * 遍历迭代器，获取子节点中的信息
     * 获取节点名和节点值
     */
    private static void ParsingXml() throws MalformedURLException, DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(xmlFile);
        Element rootElement = document.getRootElement();
        Iterator it = rootElement.elementIterator();
        while (it.hasNext()) {
            Element book = (Element) it.next();
            List<Attribute> attributes = book.attributes();
            for (Attribute attr : attributes) {
                System.out.println(attr.getName() + "---" + attr.getValue());
            }
            Iterator itt = book.elementIterator();
            while (itt.hasNext()) {
                Element next = (Element) itt.next();
                System.out.println(next.getName()+"---"+next.getText());
            }

        }
    }

    /**
     * <?xml version="1.0" encoding="UTF-8"?>
     * <ImageSearchedByImage>
     *     <SearchID>321000000000122023022320073810000</SearchID>
     *     <RecordStartNo>1</RecordStartNo>
     *     <MaxNumRecordReturn>500</MaxNumRecordReturn>
     *     <PageRecordNum>10</PageRecordNum>
     *     <Threshold>80</Threshold>
     *     <SearchType>1</SearchType>
     *     <QueryString>(passtime)</QueryString>
     *     <ResultImageDeclare>-1</ResultImageDeclare>
     *     <ResultFeatureDeclare>-1</ResultFeatureDeclare>
     *     < Image>
     *         <Type>1</Type>
     *         <FileFormat>JPEG</FileFormat>
     *         <Data>SDHUIOHDOSBHDHBCJXKCBJSDKDHFIOAUHFDUIAFHDSJANLKJFHA</Data>
     *     </Image>
     * </ImageSearchedByImage>
     */
    private static void createXml(){
        Document document =  DocumentHelper.createDocument();
        Element imageSearchedByImage = document.addElement("ImageSearchedByImage");
        imageSearchedByImage.addElement("SearchID").addText(imageSearchedByImageObject.get("SearchID")+"");
        imageSearchedByImage.addElement("RecordStartNo").addText(imageSearchedByImageObject.get("RecordStartNo")+"");
        imageSearchedByImage.addElement("MaxNumRecordReturn").addText(imageSearchedByImageObject.get("MaxNumRecordReturn")+"");
        imageSearchedByImage.addElement("PageRecordNum").addText(imageSearchedByImageObject.get("PageRecordNum")+"");
        imageSearchedByImage.addElement("Threshold").addText(imageSearchedByImageObject.get("Threshold")+"");
        imageSearchedByImage.addElement("SearchType").addText(imageSearchedByImageObject.get("SearchType")+"");
        imageSearchedByImage.addElement("QueryString").addText(imageSearchedByImageObject.get("QueryString")+"");
        imageSearchedByImage.addElement("ResultImageDeclare").addText(imageSearchedByImageObject.get("ResultImageDeclare")+"");
        imageSearchedByImage.addElement("ResultFeatureDeclare").addText(imageSearchedByImageObject.get("ResultFeatureDeclare")+"");

        Element image = imageSearchedByImage.addElement("Image");
        image.addElement("Type").addText(((Map<String,Object>)imageSearchedByImageObject.get("Image")).get("Type")+"");
        image.addElement("FileFormat").addText(((Map<String,Object>)imageSearchedByImageObject.get("Image")).get("FileFormat")+"");
        image.addElement("Data").addText(((Map<String,Object>)imageSearchedByImageObject.get("Image")).get("Data")+"");

        System.out.println(document.asXML());

        /*OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new FileOutputStream("output.xml"), format);
        writer.write(document);
        writer.close();*/
    }
}
