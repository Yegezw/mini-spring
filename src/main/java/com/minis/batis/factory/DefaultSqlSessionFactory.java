package com.minis.batis.factory;

import com.minis.batis.config.MapperNode;
import com.minis.batis.sqlsession.DefaultSqlSession;
import com.minis.batis.sqlsession.SqlSession;
import com.minis.beans.factory.config.annotation.Autowired;
import com.minis.jdbc.core.JdbcTemplate;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * SqlSession 工厂 + MapperNode 仓库
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private String mapperLocations; // mapper 目录
    Map<String, MapperNode> mapperNodeMap = new HashMap<>();

    public DefaultSqlSessionFactory() {
    }

    /**
     * 由 Spring 调用
     */
    public void init() {
        scanLocation(mapperLocations);
        for (Map.Entry<String, MapperNode> entry : mapperNodeMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    /**
     * 扫描 classpath/location/ 目录下所有的 mapper 文件
     */
    private void scanLocation(String location) {
        String mapperLocation = this.getClass().getClassLoader().getResource("").getPath() + location;
        System.out.println("mapper location : " + mapperLocation); // classpath\mapper\

        File dir = new File(mapperLocation);
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) scanLocation(location + "/" + file.getName());
            else buildMapperNodes(location + "/" + file.getName());
        }
    }

    /**
     * 构建 Mapper 节点
     */
    private void buildMapperNodes(String filePath) {
        System.out.println(filePath);

        SAXReader saxReader = new SAXReader();
        URL xmlPath = this.getClass().getClassLoader().getResource(filePath);

        try {
            Document document = saxReader.read(xmlPath);
            Element rootElement = document.getRootElement();

            String namespace = rootElement.attributeValue("namespace");

            Iterator<Element> nodes = rootElement.elementIterator();
            while (nodes.hasNext()) {
                Element node = nodes.next();

                String id = node.attributeValue("id");
                String parameterType = node.attributeValue("parameterType");
                String resultType = node.attributeValue("resultType");
                String sql = node.getText();

                MapperNode mapperNode = new MapperNode();
                mapperNode.namespace = namespace;
                mapperNode.id = id;
                mapperNode.parameterType = parameterType;
                mapperNode.resultType = resultType;
                mapperNode.sql = sql;
                mapperNode.parameter = "";

                mapperNodeMap.put(namespace + "." + id, mapperNode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public SqlSession openSession() {
        SqlSession sqlSession = new DefaultSqlSession();
        sqlSession.setJdbcTemplate(jdbcTemplate);
        sqlSession.setSqlSessionFactory(this);

        return sqlSession;
    }

    @Override
    public MapperNode getMapperNode(String name) {
        return mapperNodeMap.get(name);
    }

    // ======================================== getter()、setter() ========================================

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public Map<String, MapperNode> getMapperNodeMap() {
        return mapperNodeMap;
    }
}
