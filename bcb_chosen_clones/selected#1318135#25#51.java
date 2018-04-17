    public void testCreateNewXMLFile() throws InvalidNodeTypeDefException, ParseException, Exception {
        JCRNodeSource emptySource = loadTestSource();
        assertEquals(false, emptySource.exists());
        OutputStream sourceOut = emptySource.getOutputStream();
        assertNotNull(sourceOut);
        InputStream contentIn = getClass().getResourceAsStream(CONTENT_FILE);
        try {
            IOUtils.copy(contentIn, sourceOut);
            sourceOut.flush();
        } finally {
            sourceOut.close();
            contentIn.close();
        }
        InputStream expected = getClass().getResourceAsStream(CONTENT_FILE);
        JCRNodeSource persistentSource = loadTestSource();
        assertEquals(true, persistentSource.exists());
        InputStream actual = persistentSource.getInputStream();
        try {
            assertTrue(isXmlEqual(expected, actual));
        } finally {
            expected.close();
            actual.close();
        }
        JCRNodeSource tmpSrc = (JCRNodeSource) resolveSource(BASE_URL + "users/alexander.saar");
        persistentSource.delete();
        tmpSrc.delete();
    }
