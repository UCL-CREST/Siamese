    public void test() throws Exception {
        File temp = File.createTempFile("test", ".test");
        temp.deleteOnExit();
        StorageFile s = new StorageFile(temp, "UTF-8");
        s.addText("Test");
        s.getOutputStream().write("ing is important".getBytes("UTF-8"));
        s.getWriter().write(" but overrated");
        assertEquals("Testing is important but overrated", s.getText());
        s.close(ResponseStateOk.getInstance());
        assertEquals("Testing is important but overrated", s.getText());
        InputStream input = s.getInputStream();
        StringWriter writer = new StringWriter();
        IOUtils.copy(input, writer, "UTF-8");
        assertEquals("Testing is important but overrated", writer.toString());
        try {
            s.getOutputStream();
            fail("Should thow an IOException as it is closed.");
        } catch (IOException e) {
        }
    }
