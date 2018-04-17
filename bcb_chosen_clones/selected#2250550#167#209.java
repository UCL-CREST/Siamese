    public void testStorageStringWriter() throws Exception {
        TranslationResponseInMemory r = new TranslationResponseInMemory(2048, "UTF-8");
        {
            Writer w = r.getWriter();
            w.write("This is an example");
            w.write(" and another one.");
            w.flush();
            assertEquals("This is an example and another one.", r.getText());
        }
        {
            InputStream input = r.getInputStream();
            StringWriter writer = new StringWriter();
            try {
                IOUtils.copy(input, writer, "UTF-8");
            } finally {
                input.close();
                writer.close();
            }
            assertEquals("This is an example and another one.", writer.toString());
        }
        try {
            r.getOutputStream();
            fail("Is not allowed as you already called getWriter().");
        } catch (IOException e) {
        }
        {
            Writer output = r.getWriter();
            output.write(" and another line");
            output.write(" and write some more");
            assertEquals("This is an example and another one. and another line and write some more", r.getText());
        }
        {
            r.addText(" and some more.");
            assertEquals("This is an example and another one. and another line and write some more and some more.", r.getText());
        }
        r.setEndState(ResponseStateOk.getInstance());
        assertEquals(ResponseStateOk.getInstance(), r.getEndState());
        try {
            r.getWriter();
            fail("Previous line should throw IOException as result closed.");
        } catch (IOException e) {
        }
    }
