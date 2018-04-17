    public void testGetOldVersion() throws ServiceException, IOException, SAXException, ParserConfigurationException {
        JCRNodeSource emptySource = loadTestSource();
        for (int i = 0; i < 3; i++) {
            OutputStream sourceOut = emptySource.getOutputStream();
            InputStream contentIn = getClass().getResourceAsStream(CONTENT_FILE);
            try {
                IOUtils.copy(contentIn, sourceOut);
                sourceOut.flush();
            } finally {
                sourceOut.close();
                contentIn.close();
            }
        }
        String testSourceUri = BASE_URL + "users/lars.trieloff?revision=1.1";
        JCRNodeSource secondSource = (JCRNodeSource) resolveSource(testSourceUri);
        System.out.println("Read again at:" + secondSource.getSourceRevision());
        InputStream expected = emptySource.getInputStream();
        InputStream actual = secondSource.getInputStream();
        assertTrue(isXmlEqual(expected, actual));
    }
