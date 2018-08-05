    public void testStorageByteArray() throws Exception {
        TranslationResponseInMemory r = new TranslationResponseInMemory(2048, "UTF-8");
        {
            OutputStream output = r.getOutputStream();
            output.write("This is an example".getBytes("UTF-8"));
            output.write(" and another one.".getBytes("UTF-8"));
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
        {
            OutputStream output = r.getOutputStream();
            output.write(" and another line".getBytes("UTF-8"));
            assertEquals("This is an example and another one. and another line", r.getText());
        }
        {
            Writer output = r.getWriter();
            output.write(" and write some more");
            assertEquals("This is an example and another one. and another line and write some more", r.getText());
        }
        {
            r.addText(" and even more.");
            assertEquals("This is an example and another one. and another line and write some more and even more.", r.getText());
        }
        assertFalse(r.hasEnded());
        r.setEndState(ResponseStateOk.getInstance());
        assertTrue(r.hasEnded());
        try {
            r.getOutputStream();
            fail("Previous line should throw IOException as result closed.");
        } catch (IOException e) {
        }
        try {
            r.getWriter();
            fail("Previous line should throw IOException as result closed.");
        } catch (IOException e) {
        }
    }
