    public javax.sql.DataSource getClientDataSource(String database, String user, String password) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class nsDataSource = Class.forName(jdbcDataSource);
        DataSource ds = (DataSource) nsDataSource.newInstance();
        Class[] methodParams = new Class[] { String.class };
        Method dbname = nsDataSource.getMethod("setDatabaseName", methodParams);
        Object[] args = new Object[] { database };
        dbname.invoke(ds, args);
        if (user != null) {
            Method setuser = nsDataSource.getMethod("setUser", methodParams);
            args = new Object[] { user };
            setuser.invoke(ds, args);
        }
        if (password != null) {
            Method setpw = nsDataSource.getMethod("setPassword", methodParams);
            args = new Object[] { password };
            setpw.invoke(ds, args);
        }
        Method servername = nsDataSource.getMethod("setServerName", methodParams);
        args = new Object[] { "localhost" };
        servername.invoke(ds, args);
        methodParams = new Class[] { int.class };
        Method portnumber = nsDataSource.getMethod("setPortNumber", methodParams);
        args = new Object[] { new Integer(1527) };
        portnumber.invoke(ds, args);
        return ds;
    }
