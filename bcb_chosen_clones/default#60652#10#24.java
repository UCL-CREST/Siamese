    public static void main(String[] args) throws Throwable {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection con1 = DriverManager.getConnection("jdbc:oracle:thin:@cumberland:1521:csuite", "belair40", "password");
        DatabaseMetaData metaData1 = con1.getMetaData();
        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new MetaDataTestCase(metaData1));
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }
    }
