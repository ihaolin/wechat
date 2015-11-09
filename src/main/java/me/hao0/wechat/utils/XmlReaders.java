package me.hao0.wechat.utils;

import me.hao0.wechat.exception.XmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 9/11/15
 */
public class XmlReaders {

    private static DocumentBuilder builder;

    static {
        try {
            builder =  DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new XmlException("init xml failed");
        }
    }

    private Document document;

    private XmlReaders(){}

    public static XmlReaders create(String xml){
        return create(new ByteArrayInputStream(xml.getBytes()));
    }

    public static XmlReaders create(InputStream inputStream){
        XmlReaders readers = new XmlReaders();
        try {
            readers.document = builder.parse(inputStream);
        } catch (Exception e) {
            throw new XmlException("XmlReaders create fail", e);
        }
        return readers;
    }

    public Node getNode(String tagName){
        NodeList nodes = document.getElementsByTagName(tagName);
        if (nodes.getLength() <= 0){
            return null;
        }
        return nodes.item(0);
    }

    public NodeList getNodes(String tagName){
        NodeList nodes = document.getElementsByTagName(tagName);
        if (nodes.getLength() <= 0){
            return null;
        }
        return nodes;
    }

    /**
     * 获取某个节点的文本内容，若有多个该节点，只会返回第一个
     * @param tagName 标签名
     * @return 文本内容，或NULL
     */
    public String getNodeStr(String tagName){
        Node node = getNode(tagName);
        return node == null ? null : node.getTextContent();
    }

    /**
     * 获取某个节点的Integer，若有多个该节点，只会返回第一个
     * @param tagName 标签名
     * @return Integer值，或NULL
     */
    public Integer getNodeInt(String tagName){
        String nodeContent = getNodeStr(tagName);
        return nodeContent == null ? null : Integer.valueOf(nodeContent);
    }

    /**
     * 获取某个节点的Long值，若有多个该节点，只会返回第一个
     * @param tagName 标签名
     * @return Long值，或NULL
     */
    public Long getNodeLong(String tagName){
        String nodeContent = getNodeStr(tagName);
        return nodeContent == null ? null : Long.valueOf(nodeContent);
    }

    /**
     * 获取某个节点的Float，若有多个该节点，只会返回第一个
     * @param tagName 标签名
     * @return Float值，或NULL
     */
    public Float getNodeFloat(String tagName){
        String nodeContent = getNodeStr(tagName);
        return nodeContent == null ? null : Float.valueOf(nodeContent);
    }
}
