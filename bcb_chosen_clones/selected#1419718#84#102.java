    public boolean executeUpdate(String strSql) throws SQLException {
        getConnection();
        boolean flag = false;
        stmt = con.createStatement();
        logger.info("###############::执行SQL语句操作(更新数据 无参数):" + strSql);
        try {
            if (0 < stmt.executeUpdate(strSql)) {
                close_DB_Object();
                flag = true;
                con.commit();
            }
        } catch (SQLException ex) {
            logger.info("###############Error DBManager Line126::执行SQL语句操作(更新数据 无参数):" + strSql + "失败!");
            flag = false;
            con.rollback();
            throw ex;
        }
        return flag;
    }
