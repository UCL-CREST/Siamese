    public int executeUpdateJT(String sqlList[], Object[][] paramsList) {
        Connection connection = null;
        connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        PreparedStatement preparedStatement = null;
        try {
            for (int i = 0; i < sqlList.length; i++) {
                System.out.println(sqlList[i]);
                if (connection != null && !connection.isClosed()) {
                    preparedStatement = connection.prepareStatement(sqlList[i]);
                    InputStream is = null;
                    int size = paramsList[i].length;
                    int curr = 0;
                    if (paramsList[i].length > 0) {
                        for (int j = 0; j < size; j++) {
                            Object obj = paramsList[i][j];
                            if (obj != null) {
                                curr++;
                                if (obj.getClass().equals(Class.forName("java.io.File"))) {
                                    File file = (File) obj;
                                    is = new FileInputStream(file);
                                    preparedStatement.setBinaryStream(curr, is, (int) file.length());
                                } else if (obj.getClass().equals(Class.forName("java.util.Date"))) {
                                    java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    preparedStatement.setString(curr, sdf.format((Date) obj));
                                } else {
                                    preparedStatement.setObject(curr, obj);
                                }
                            }
                        }
                    }
                    preparedStatement.executeUpdate();
                    if (is != null) {
                        is.close();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("发生错误，数据回滚！");
            e.printStackTrace();
            try {
                connection.rollback();
                return 0;
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        try {
            connection.commit();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (SQLException e) {
                log.error("未能正确关闭数据库连接！", e);
                System.out.println("未能正确关闭数据库连接！");
                e.printStackTrace();
            }
        }
        return -1;
    }
