    public void testRevcounter() throws ServiceException, IOException {
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
            System.out.println(emptySource.getLatestSourceRevision());
        }
        String testSourceUri = BASE_URL + "users/lars.trieloff?revision=1.1";
        JCRNodeSource secondSource = (JCRNodeSource) resolveSource(testSourceUri);
        System.out.println("Created at: " + secondSource.getSourceRevision());
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
            System.out.println(emptySource.getLatestSourceRevision());
        }
        System.out.println("Read again at:" + secondSource.getSourceRevision());
        assertNotNull(emptySource.getSourceRevision());
    }
