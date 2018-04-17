    public void testAddingEntries() throws Exception {
        DiskCache c = new DiskCache();
        {
            c.setRoot(rootFolder.getAbsolutePath());
            c.setHtmlExtension("htm");
            c.setPropertiesExtension("txt");
            assertEquals("htm", c.getHtmlExtension());
            assertEquals("txt", c.getPropertiesExtension());
            assertEquals(rootFolder.getAbsolutePath(), c.getRoot());
        }
        String key1 = "cat1/key1";
        String key2 = "cat1/key2";
        try {
            try {
                {
                    c.removeCacheEntry(key1, null);
                    CacheItem i = c.getOrCreateCacheEntry(key1);
                    assertNull(i.getEncoding());
                    assertEquals(-1L, i.getLastModified());
                    assertEquals(-1, i.getTranslationCount());
                    assertFalse(i.isCached());
                    assertNull(i.getHeaders());
                    i.setLastModified(300005L);
                    i.setTranslationCount(10);
                    i.setEncoding("ISO-8859-7");
                    i.setHeader(new ResponseHeaderImpl("Test2", new String[] { "Value3", "Value4" }));
                    i.setHeader(new ResponseHeaderImpl("Test1", new String[] { "Value1", "Value2" }));
                    byte[] greekTextBytes = new byte[] { -57, -20, -27, -15, -34, -13, -23, -31, 32, -48, -17, -21, -23, -12, -23, -22, -34, 32, -59, -10, -25, -20, -27, -15, -33, -28, -31, 32, -60, -23, -31, -19, -35, -20, -27, -12, -31, -23, 32, -22, -31, -24, -25, -20, -27, -15, -23, -19, -36, 32, -60, -39, -47, -59, -63, -51, 32, -13, -12, -17, 32, -28, -33, -22, -12, -11, -17, 32, -13, -11, -29, -22, -17, -23, -19, -7, -19, -23, -2, -19, 32, -12, -25, -14, 32, -56, -27, -13, -13, -31, -21, -17, -19, -33, -22, -25, -14 };
                    String greekText = new String(greekTextBytes, "ISO-8859-7");
                    {
                        InputStream input = new ByteArrayInputStream(greekTextBytes);
                        try {
                            i.setContentAsStream(input);
                        } finally {
                            input.close();
                        }
                    }
                    assertEquals("ISO-8859-7", i.getEncoding());
                    assertEquals(300005L, i.getLastModified());
                    assertEquals(10, i.getTranslationCount());
                    assertFalse(i.isCached());
                    i.updateAfterAllContentUpdated(null, null);
                    {
                        assertEquals(3, i.getHeaders().size());
                        int ii = 0;
                        for (ResponseHeader h : i.getHeaders()) {
                            ii++;
                            if (ii == 1) {
                                assertEquals("Content-Length", h.getName());
                                assertEquals("[97]", Arrays.toString(h.getValues()));
                            } else if (ii == 2) {
                                assertEquals("Test1", h.getName());
                                assertEquals("[Value1, Value2]", Arrays.toString(h.getValues()));
                            } else if (ii == 3) {
                                assertEquals("Test2", h.getName());
                                assertEquals("[Value3, Value4]", Arrays.toString(h.getValues()));
                            }
                        }
                    }
                    c.storeInCache(key1, i);
                    assertEquals("ISO-8859-7", i.getEncoding());
                    assertEquals(300005L, i.getLastModified());
                    assertEquals(10, i.getTranslationCount());
                    assertTrue(i.isCached());
                    {
                        InputStream input = i.getContentAsStream();
                        StringWriter w = new StringWriter();
                        IOUtils.copy(input, w, "ISO-8859-7");
                        IOUtils.closeQuietly(input);
                        IOUtils.closeQuietly(w);
                        assertEquals(greekText, w.toString());
                    }
                }
                {
                    c.removeCacheEntry(key2, null);
                    CacheItem i = c.getOrCreateCacheEntry(key2);
                    assertNull(i.getEncoding());
                    assertEquals(-1L, i.getLastModified());
                    assertEquals(-1, i.getTranslationCount());
                    assertFalse(i.isCached());
                    assertNull(i.getHeaders());
                    i.setLastModified(350000L);
                    i.setTranslationCount(11);
                    i.setEncoding("ISO-8859-1");
                    i.setHeader(new ResponseHeaderImpl("Test3", new String[] { "Value3", "Value4" }));
                    i.setHeader(new ResponseHeaderImpl("Test4", new String[] { "Value1" }));
                    String englishText = "Hello this is another example";
                    {
                        InputStream input = new ByteArrayInputStream(englishText.getBytes("ISO-8859-1"));
                        try {
                            i.setContentAsStream(input);
                        } finally {
                            input.close();
                        }
                    }
                    assertEquals("ISO-8859-1", i.getEncoding());
                    assertEquals(350000L, i.getLastModified());
                    assertEquals(11, i.getTranslationCount());
                    assertFalse(i.isCached());
                    i.updateAfterAllContentUpdated(null, null);
                    {
                        assertEquals(3, i.getHeaders().size());
                        int ii = 0;
                        for (ResponseHeader h : i.getHeaders()) {
                            ii++;
                            if (ii == 1) {
                                assertEquals("Content-Length", h.getName());
                                assertEquals("[29]", Arrays.toString(h.getValues()));
                            } else if (ii == 2) {
                                assertEquals("Test3", h.getName());
                                assertEquals("[Value3, Value4]", Arrays.toString(h.getValues()));
                            } else if (ii == 3) {
                                assertEquals("Test4", h.getName());
                                assertEquals("[Value1]", Arrays.toString(h.getValues()));
                            }
                        }
                    }
                    c.storeInCache(key2, i);
                    assertEquals("ISO-8859-1", i.getEncoding());
                    assertEquals(350000L, i.getLastModified());
                    assertEquals(11, i.getTranslationCount());
                    assertTrue(i.isCached());
                    {
                        InputStream input = i.getContentAsStream();
                        StringWriter w = new StringWriter();
                        IOUtils.copy(input, w, "ISO-8859-1");
                        IOUtils.closeQuietly(input);
                        IOUtils.closeQuietly(w);
                        assertEquals(englishText, w.toString());
                    }
                }
                {
                    CacheItem i = c.getOrCreateCacheEntry(key1);
                    assertEquals("ISO-8859-7", i.getEncoding());
                    assertEquals(300005L, i.getLastModified());
                    assertEquals(10, i.getTranslationCount());
                    assertTrue(i.isCached());
                    {
                        assertEquals(3, i.getHeaders().size());
                        int ii = 0;
                        for (ResponseHeader h : i.getHeaders()) {
                            ii++;
                            if (ii == 1) {
                                assertEquals("Content-Length", h.getName());
                                assertEquals("[97]", Arrays.toString(h.getValues()));
                            } else if (ii == 2) {
                                assertEquals("Test1", h.getName());
                                assertEquals("[Value1, Value2]", Arrays.toString(h.getValues()));
                            } else if (ii == 3) {
                                assertEquals("Test2", h.getName());
                                assertEquals("[Value3, Value4]", Arrays.toString(h.getValues()));
                            }
                        }
                    }
                    byte[] greekTextBytes = new byte[] { -57, -20, -27, -15, -34, -13, -23, -31, 32, -48, -17, -21, -23, -12, -23, -22, -34, 32, -59, -10, -25, -20, -27, -15, -33, -28, -31, 32, -60, -23, -31, -19, -35, -20, -27, -12, -31, -23, 32, -22, -31, -24, -25, -20, -27, -15, -23, -19, -36, 32, -60, -39, -47, -59, -63, -51, 32, -13, -12, -17, 32, -28, -33, -22, -12, -11, -17, 32, -13, -11, -29, -22, -17, -23, -19, -7, -19, -23, -2, -19, 32, -12, -25, -14, 32, -56, -27, -13, -13, -31, -21, -17, -19, -33, -22, -25, -14 };
                    String greekText = new String(greekTextBytes, "ISO-8859-7");
                    {
                        InputStream input = i.getContentAsStream();
                        StringWriter w = new StringWriter();
                        IOUtils.copy(input, w, "ISO-8859-7");
                        IOUtils.closeQuietly(input);
                        IOUtils.closeQuietly(w);
                        assertEquals(greekText, w.toString());
                    }
                }
                {
                    c.removeCacheEntry(key1, null);
                    CacheItem i = c.getOrCreateCacheEntry(key1);
                    assertNull(i.getEncoding());
                    assertEquals(-1L, i.getLastModified());
                    assertEquals(-1, i.getTranslationCount());
                    assertFalse(i.isCached());
                    assertNull(i.getHeaders());
                }
            } finally {
                c.removeCacheEntry(key1, null);
            }
        } finally {
            c.removeCacheEntry(key2, null);
        }
    }
