    public void test() throws Exception {
        StringDocument doc = new StringDocument("Test", "UTF-8");
        doc.open();
        try {
            assertEquals("UTF-8", doc.getCharacterEncoding());
            assertEquals("Test", doc.getText());
            InputStream input = doc.getInputStream();
            StringWriter writer = new StringWriter();
            try {
                IOUtils.copy(input, writer, "UTF-8");
            } finally {
                writer.close();
            }
            assertEquals("Test", writer.toString());
        } finally {
            doc.close();
        }
    }
