    public void testConvert() throws IOException, ConverterException {
        InputStreamReader reader = new InputStreamReader(new FileInputStream("test" + File.separator + "input" + File.separator + "A0851ohneex.dat"), CharsetUtil.forName("x-PICA"));
        FileWriter writer = new FileWriter("test" + File.separator + "output" + File.separator + "ddbInterToMarcxmlTest.out");
        Converter c = context.getConverter("ddb-intern", "MARC21-xml", "x-PICA", "UTF-8");
        ConversionParameters params = new ConversionParameters();
        params.setSourceCharset("x-PICA");
        params.setTargetCharset("UTF-8");
        params.setAddCollectionHeader(true);
        params.setAddCollectionFooter(true);
        c.convert(reader, writer, params);
    }
