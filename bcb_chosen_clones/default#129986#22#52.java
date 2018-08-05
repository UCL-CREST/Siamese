    @Test
    public void SpringSelectTest() throws SQLException, ClassNotFoundException {
        PropertyConfigurator.configure("log4j.properties");
        logger.debug("**************************************************");
        logger.debug("Started Logging");
        logger.debug("Trying to create table");
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:D:/300 Workspace/JampangWorkspace/jampang-base/src/test/resources/jampang.db");
        Statement statement = connection.createStatement();
        statement.executeUpdate("drop table if exists jmp_com_users");
        statement.executeUpdate("CREATE TABLE jmp_com_users (user_id string, user_code string, user_email string, user_region string, user_rank string, create_by string, create_date datetime, update_by string, update_date datetime, status string)");
        statement.close();
        connection.close();
        logger.debug("Trying to insert data");
        connection = DriverManager.getConnection("jdbc:sqlite:D:/300 Workspace/JampangWorkspace/jampang-base/src/test/resources/jampang.db");
        statement = connection.createStatement();
        statement.executeUpdate("insert into jmp_com_users (user_id, user_code, user_email, user_region, user_rank, create_by, create_date, update_by, update_date, status) values ('0001', 'dbakhtiar', 'dbakhtiar@jampang.org', 'SG', 'ADMIN', 'INIT', '13-MAY-2010', null, null, 'A')");
        statement.close();
        connection.close();
        logger.debug("Trying to read data");
        connection = DriverManager.getConnection("jdbc:sqlite:D:/300 Workspace/JampangWorkspace/jampang-base/src/test/resources/jampang.db");
        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from jmp_com_users");
        if (resultSet.next()) {
            String email = resultSet.getString("user_email");
            Assert.assertEquals("dbakhtiar@jampang.org", email);
        }
        statement.close();
        connection.close();
        logger.debug("Ended Logging");
    }
