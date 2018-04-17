    public static void TestDBStore() throws PDException, Exception {
        StoreDDBB StDB = new StoreDDBB("jdbc:derby://localhost:1527/Prodoc", "Prodoc", "Prodoc", "org.apache.derby.jdbc.ClientDriver;STBLOB");
        System.out.println("Driver[" + StDB.getDriver() + "] Tabla  [" + StDB.getTable() + "]");
        StDB.Connect();
        FileInputStream in = new FileInputStream("/tmp/readme.htm");
        StDB.Insert("12345678-1", "1.0", in);
        int TAMBUFF = 1024 * 64;
        byte Buffer[] = new byte[TAMBUFF];
        InputStream Bytes;
        Bytes = StDB.Retrieve("12345678-1", "1.0");
        FileOutputStream fo = new FileOutputStream("/tmp/12345679.htm");
        int readed = Bytes.read(Buffer);
        while (readed != -1) {
            fo.write(Buffer, 0, readed);
            readed = Bytes.read(Buffer);
        }
        Bytes.close();
        fo.close();
        StDB.Delete("12345678-1", "1.0");
        StDB.Disconnect();
    }
