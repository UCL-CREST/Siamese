    @Test
    public void testBinaryFileFromFileSystem() throws Exception {
        final String testfileName = "tiny-jpg.img";
        final File testfile = getTestFile(testfileName);
        final byte[] directBytes = IOUtils.toByteArray(new FileInputStream(testfile));
        final String directStr = hexRepresentation(directBytes);
        final WebClient client = getWebClient();
        final Page testpage = client.getPage(testfile.toURI().toURL());
        final byte[] webclientBytes = IOUtils.toByteArray(testpage.getWebResponse().getContentAsStream());
        final String webclientStr = hexRepresentation(webclientBytes);
        assertEquals(directStr, webclientStr);
    }
