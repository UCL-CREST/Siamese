    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        FileInputStream in = new FileInputStream("test/res/simple.abc");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        byte[] abcBytes = out.toByteArray();
        in.close();
        out.close();
        coder = new ABCCoder(abcBytes);
        abc = new ABCFile();
        abc.decode(coder);
    }
