    public void test() throws Exception {
        TranslationResponseStorage r = new TranslationResponseStorage(mockStorage, "UTF-8");
        MockResponseStateObserver observer = new MockResponseStateObserver();
        r.addStateObserver(observer);
        assertEquals("UTF-8", r.getCharacterEncoding());
        assertEquals(-1L, r.getLastModified());
        assertEquals(0, r.getTranslationCount());
        r.setTranslationCount(10);
        assertEquals(10, r.getTranslationCount());
        assertNotNull(r.getHeadersStorage());
        assertNull(r.getHeaders());
        r.setLastModified(100000L);
        assertEquals(100000L, r.getLastModified());
        assertFalse(r.getHeaders().isEmpty());
        {
            Set<ResponseHeader> set = new TreeSet<ResponseHeader>();
            set.add(new ResponseHeaderImpl("Last-Modified", new String[] { DateUtil.formatDate(new Date(200000L)) }));
            r.addHeaders(set);
        }
        assertEquals(1, r.getHeaders().size());
        assertEquals(200000L, r.getLastModified());
        {
            Set<ResponseHeader> set = new TreeSet<ResponseHeader>();
            set.add(new ResponseHeaderImpl("Last-Modified", new String[] { DateUtil.formatDate(new Date(310000L)) }));
            set.add(new ResponseHeaderImpl("User-Agent", new String[] { "Pinoccio" }));
            r.addHeaders(set);
        }
        assertEquals(2, r.getHeaders().size());
        int ii = 0;
        for (ResponseHeader h : r.getHeaders()) {
            ii++;
            if (ii == 1) {
                assertEquals("Last-Modified", h.getName());
                assertEquals(Arrays.toString(new String[] { DateUtil.formatDate(new Date(310000L)) }), Arrays.toString(h.getValues()));
            } else if (ii == 2) {
                assertEquals("User-Agent", h.getName());
                assertEquals(Arrays.toString(new String[] { "Pinoccio" }), Arrays.toString(h.getValues()));
            }
        }
        r.addText("This is an example");
        r.addText(" and another one.");
        assertEquals("This is an example and another one.", mockStorage.getText());
        InputStream input = r.getInputStream();
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(input, writer, "UTF-8");
        } finally {
            input.close();
            writer.close();
        }
        assertEquals("This is an example and another one.", writer.toString());
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
        assertFalse(r.hasEnded());
        assertNull(r.getEndState());
        assertEquals(0L, observer.getHits());
        r.setEndState(ResponseStateOk.getInstance());
        assertEquals(ResponseStateOk.getInstance(), r.getEndState());
        assertTrue(r.hasEnded());
        assertEquals(1L, observer.getHits());
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
