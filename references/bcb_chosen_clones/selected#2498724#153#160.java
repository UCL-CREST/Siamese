    @Override
    public Connection getConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, NoSuchMethodException, InvocationTargetException {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart());
        Class.forName(driver).getConstructor().newInstance();
        final Connection result = DriverManager.getConnection(url, user, password);
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit(result));
        return result;
    }
