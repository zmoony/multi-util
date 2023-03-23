package com.example.text.demoOnLine.java处理xml;

import org.dom4j.DocumentHelper;
import org.dom4j.io.DocumentSource;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Document Object Model
 * 允许应用程序对数据和结构做出更改
 * 访问是双向的，可以在任何时候再树中上、下导航获取、操作任意部分的数据
 *
 * 解析XML文档的需要加载整个文档来构造层次结构，消耗内存资源大。
 *
 * 遍历能力强，常应用于XML文档需要频繁改变的服务中。
 * @author yuez
 * @since 2023/2/24
 */
public class DomUtil {
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
//        ParsingXml();
        createXml();
    }


    /**
     * 创建一个 DocumentBuilderFactory 对象
     * 创建一个 DocumentBuilder 对象
     * 通过 DocumentBuilder 的 parse() 方法加载 XML 到当前工程目录下
     * 通过 getElementsByTagName() 方法获取所有 XML 所有节点的集合
     * 遍历所有节点
     * 通过 item() 方法获取某个节点的属性
     * 通过 getNodeName() 和 getNodeValue() 方法获取属性名和属性值
     * 通过 getChildNodes() 方法获取子节点，并遍历所有子节点
     * 通过 getNodeName() 和 getTextContent() 方法获取子节点名称和子节点值
     *
     */
    private static void ParsingXml(){
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
            Document document = db.parse(xmlFile);
            NodeList book = document.getElementsByTagName("book");
            int bookCnt = book.getLength();
            System.err.println("一共获取到" + bookCnt +"本书");

            for (int i = 0; i < bookCnt; i++) {
                Node bookNode = book.item(i);
                NamedNodeMap attrs = bookNode.getAttributes();
                for (int j = 0; j < attrs.getLength(); j++) {
                    Node attr = attrs.item(j);
                    System.err.println(attr.getNodeName()+"---"+attr.getNodeValue());//id
                }
                NodeList childNodes = bookNode.getChildNodes();
                for (int k = 0; k < childNodes.getLength(); k++) {
                    if(childNodes.item(k).getNodeType() == Node.ELEMENT_NODE){
                        System.out.println(childNodes.item(k).getNodeName()+"---" + childNodes.item(k).getTextContent());
                    }/*else{
                        System.out.println(childNodes.item(k).getNodeType());
                    }*/
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void createXml() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element rootElement = document.createElement("ImageSearchedByImage");
        Element searchID = document.createElement("SearchID");
        searchID.appendChild(document.createTextNode(imageSearchedByImageObject.get("SearchID") + ""));
        rootElement.appendChild(searchID);

        document.appendChild(rootElement);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");

        StringWriter sw = new StringWriter();
        transformer.transform(new javax.xml.transform.dom.DOMSource(document),new StreamResult(sw));
        String xml = sw.getBuffer().toString();
        System.out.println(xml);
    }
}
