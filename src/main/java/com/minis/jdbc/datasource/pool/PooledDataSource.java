package com.minis.jdbc.datasource.pool;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * 池数据源
 */
public class PooledDataSource implements DataSource {

    /**
     * 池连接列表
     */
    private List<PooledConnection> connections = null;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private int initialSize = 2;
    private Properties connectionProperties;

    public PooledDataSource() {
    }

    private void initPool() {
        connections = new ArrayList<>(initialSize);
        try {
            for (int i = 0; i < initialSize; i++) {
                Connection connect = DriverManager.getConnection(url, username, password);
                PooledConnection pooledConnection = new PooledConnection(connect, false);
                connections.add(pooledConnection);
            }
            System.out.println("PooledDataSource.initPool() 完成, initialSize = " + initialSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnectionFromDriver(getUsername(), getPassword());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnectionFromDriver(username, password);
    }

    protected Connection getConnectionFromDriver(String username, String password) throws SQLException {
        Properties mergedProps = new Properties();

        Properties connProps = getConnectionProperties();
        if (connProps != null) mergedProps.putAll(connProps);
        if (username != null) mergedProps.setProperty("user", username);
        if (password != null) mergedProps.setProperty("password", password);

        if (connections == null) initPool(); // 如果没有初始化

        PooledConnection pooledConnection = getAvailableConnection();
        // 死等一个有效的连接
        while (pooledConnection == null) {
            pooledConnection = getAvailableConnection();

            if (pooledConnection == null) {
                try {
                    TimeUnit.MILLISECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return pooledConnection;
    }

    /**
     * 获取可用连接
     */
    private PooledConnection getAvailableConnection() throws SQLException {
        for (PooledConnection pooledConnection : connections) {
            if (!pooledConnection.isActive()) {
                pooledConnection.setActive(true);
                return pooledConnection;
            }
        }

        return null;
    }

    protected Connection getConnectionFromDriverManager(String url, Properties props) throws SQLException {
        return DriverManager.getConnection(url, props);
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        try {
            Class.forName(this.driverClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not load JDBC driver class [" + driverClassName + "]", e);
        }
    }

    // ======================================== getter()、setter() ========================================

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public Properties getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    // =============================================== 空方法 ===============================================

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter arg0) throws SQLException {
    }

    @Override
    public void setLoginTimeout(int arg0) throws SQLException {
    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        return null;
    }
}
