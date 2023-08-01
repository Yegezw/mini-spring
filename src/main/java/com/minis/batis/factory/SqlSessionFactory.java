package com.minis.batis.factory;

import com.minis.batis.config.MapperNode;
import com.minis.batis.sqlsession.SqlSession;

public interface SqlSessionFactory {

    SqlSession openSession();

    MapperNode getMapperNode(String name);
}
