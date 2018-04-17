    public int executeUpdateJT(String sql, Object[][] paramsList) {
        Connection connection = null;
        connection = this.getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < paramsList.length; i++) {
                if (connection != null && !connection.isClosed()) {
                    InputStream is = null;
                    if (paramsList[i].length > 0) {
                        for (int j = 0; j < paramsList[i].length; j++) {
                            Object obj = paramsList[i][j];
                            if (obj.getClass().equals(Class.forName("java.io.File"))) {
                                File file = (File) obj;
                                is = new FileInputStream(file);
                                preparedStatement.setBinaryStream(j + 1, is, (int) file.length());
                            } else if (obj.getClass().equals(Class.forName("java.util.Date"))) {
                                java.text.SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                preparedStatement.setString(j + 1, sdf.format((Date) obj));
                            } else {
                                preparedStatement.setObject(j + 1, obj);
                            }
                        }
                    }
                    preparedStatement.executeUpdate();
                    if (is != null) {
                        is.close();
                    }
                    ;
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
                connection.close();
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
