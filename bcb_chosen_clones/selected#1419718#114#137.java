    public boolean executeUpdate(String strSql, HashMap<Integer, Object> prams) throws SQLException, ClassNotFoundException {
        getConnection();
        boolean flag = false;
        try {
            pstmt = con.prepareStatement(strSql);
            setParamet(pstmt, prams);
            logger.info("###############::执行SQL语句操作(更新数据 有参数):" + strSql);
            if (0 < pstmt.executeUpdate()) {
                close_DB_Object();
                flag = true;
                con.commit();
            }
        } catch (SQLException ex) {
            logger.info("###############Error DBManager Line121::执行SQL语句操作(更新数据 无参数):" + strSql + "失败!");
            flag = false;
            con.rollback();
            throw ex;
        } catch (ClassNotFoundException ex) {
            logger.info("###############Error DBManager Line152::执行SQL语句操作(更新数据 无参数):" + strSql + "失败! 参数设置类型错误!");
            con.rollback();
            throw ex;
        }
        return flag;
    }
