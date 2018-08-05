    public void testConvert() throws ConverterException, IOException {
        File picaRecordFile = new File("data/p119_test.dat");
        FileInputStream in = null;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            in = new FileInputStream(picaRecordFile);
            int i;
            while ((i = in.read()) != -1) {
                buffer.write(i);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        PicaPlusToOaiDcConverter converter = new PicaPlusToOaiDcConverter();
        byte[] convertedRecords = converter.convert(buffer.toByteArray(), "x-PICA", "UTF-8");
        logger.info("convertToList() - " + new String(convertedRecords, "UTF-8"));
    }
