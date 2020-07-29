    @Before
    public void BeforeTheTest() throws Exception {
        URL url = ProfileParserTest.class.getClassLoader().getResource("ca/uhn/hl7v2/conf/parser/tests/example_ack.xml");
        URLConnection conn = url.openConnection();
        InputStream instream = conn.getInputStream();
        if (instream == null) throw new Exception("can't find the xml file");
        BufferedReader in = new BufferedReader(new InputStreamReader(instream));
        int tmp = 0;
        StringBuffer buf = new StringBuffer();
        while ((tmp = in.read()) != -1) {
            buf.append((char) tmp);
        }
        profileString = buf.toString();
    }
