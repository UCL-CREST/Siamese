    public static void main(String[] args) {
        FTPClient client = new FTPClient();
        try {
            File l_file = new File("C:/temp/testLoribel.html");
            String l_url = "http://www.loribel.com/index.html";
            GB_HttpTools.loadUrlToFile(l_url, l_file, ENCODING.ISO_8859_1);
            System.out.println("Try to connect...");
            client.connect("ftp://ftp.phpnet.org");
            System.out.println("Connected to server");
            System.out.println("Try to connect...");
            boolean b = client.login("fff", "ddd");
            System.out.println("Login: " + b);
            String[] l_names = client.listNames();
            GB_DebugTools.debugArray(GB_FtpDemo2.class, "names", l_names);
            b = client.makeDirectory("test02/toto");
            System.out.println("Mkdir: " + b);
            String l_remote = "test02/test.xml";
            InputStream l_local = new StringInputStream("Test111111111111111");
            b = client.storeFile(l_remote, l_local);
            System.out.println("Copy file: " + b);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
