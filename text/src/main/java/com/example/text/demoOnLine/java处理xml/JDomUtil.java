package com.example.text.demoOnLine.java处理xml;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仅使用具体类，而不使用接口
 * API 大量使用了 Collections 类
 * <dependency>
 *             <groupId>org.jdom</groupId>
 *             <artifactId>jdom</artifactId>
 *             <version>1.1</version>
 *         </dependency>
 * @author yuez
 * @since 2023/2/24
 */
public class JDomUtil {
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

    public static void main(String[] args) throws IOException, JDOMException {
        createXml();
    }

    private static void createXml() {
        Document document = new Document();
        Element imageSearchedByImage = new Element("ImageSearchedByImage");
        imageSearchedByImage.addContent(new Element("SearchID").setText(imageSearchedByImageObject.get("SearchID")+""));
        imageSearchedByImage.addContent(new Element("RecordStartNo").setText(imageSearchedByImageObject.get("RecordStartNo")+""));
        imageSearchedByImage.addContent(new Element("MaxNumRecordReturn").setText(imageSearchedByImageObject.get("MaxNumRecordReturn")+""));
        imageSearchedByImage.addContent(new Element("PageRecordNum").setText(imageSearchedByImageObject.get("PageRecordNum")+""));
        imageSearchedByImage.addContent(new Element("Threshold").setText(imageSearchedByImageObject.get("Threshold")+""));
        imageSearchedByImage.addContent(new Element("SearchType").setText(imageSearchedByImageObject.get("SearchType")+""));
        imageSearchedByImage.addContent(new Element("QueryString").setText(imageSearchedByImageObject.get("QueryString")+""));
        imageSearchedByImage.addContent(new Element("ResultImageDeclare").setText(imageSearchedByImageObject.get("ResultImageDeclare")+""));
        imageSearchedByImage.addContent(new Element("ResultFeatureDeclare").setText(imageSearchedByImageObject.get("ResultFeatureDeclare")+""));

        Element image = new Element("Image");
        image.addContent(new Element("Type").setText(((Map<String,Object>)imageSearchedByImageObject.get("Image")).get("Type")+""));
        image.addContent(new Element("FileFormat").setText(((Map<String,Object>)imageSearchedByImageObject.get("Image")).get("FileFormat")+""));
        image.addContent(new Element("Data").setText(((Map<String,Object>)imageSearchedByImageObject.get("Image")).get("Data")+""));
        imageSearchedByImage.addContent(image);

        document.setRootElement(imageSearchedByImage);

        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        String s = xmlOutputter.outputString(document);
        System.out.println(s);


    }

    /**
     * 创建一个 SAXBuilder 的对象
     * 创建一个输入流，将 xml 文件加载到输入流中
     * 通过 saxBuilder 的 build()方法，将输入流加载到 saxBuilder 中
     * 通过 document 对象获取 xml 文件的根节点
     * 获取根节点下的子节点的 List 集合
     */
    private static void ParsingXml() throws IOException, JDOMException {
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = saxBuilder.build(xmlFile);
        Element rootElement = document.getRootElement();
//        System.out.println(rootElement.getName() + "----"+ rootElement.getValue());
        List<Element> bookList = rootElement.getChildren();
        for (Element book : bookList) {
//            System.out.println(book.getName() + "----"+ book.getValue());
            List<Attribute> attributes = book.getAttributes();
            for (Attribute attr : attributes) {
                String attrName = attr.getName();
                String attrValue = attr.getValue();
                System.out.println( attrName + "----" + attrValue);
            }
            List<Element> bookNodeList = book.getChildren();
            for (Element bookNode : bookNodeList) {
                System.out.println(bookNode.getName() + "----"+ bookNode.getValue());
            }
        }
    }


}
