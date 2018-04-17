    public void testReadHelloWorldTxt() throws Exception {
        final InputStream helloWorldIS = this.getClass().getClassLoader().getResourceAsStream(BASE_DIR + "/HelloWorld.txt");
        FileUtils.forceMkdir(new File(this.testDir.getAbsolutePath() + "/org/settings4j/contentresolver"));
        final String helloWorldPath = this.testDir.getAbsolutePath() + "/org/settings4j/contentresolver/HelloWorld.txt";
        final FileOutputStream fileOutputStream = new FileOutputStream(new File(helloWorldPath));
        IOUtils.copy(helloWorldIS, fileOutputStream);
        IOUtils.closeQuietly(helloWorldIS);
        IOUtils.closeQuietly(fileOutputStream);
        LOG.info("helloWorldPath: " + helloWorldPath);
        final FSContentResolver contentResolver = new FSContentResolver();
        contentResolver.setRootFolderPath(this.testDir.getAbsolutePath());
        byte[] content = contentResolver.getContent("org/settings4j/contentresolver/HelloWorld.txt");
        assertNotNull(content);
        assertEquals("Hello World", new String(content, "UTF-8"));
        content = contentResolver.getContent("file:org/settings4j/contentresolver/HelloWorld.txt");
        assertNotNull(content);
        assertEquals("Hello World", new String(content, "UTF-8"));
        content = contentResolver.getContent("file:/org/settings4j/contentresolver/HelloWorld.txt");
        assertNotNull(content);
        assertEquals("Hello World", new String(content, "UTF-8"));
        content = contentResolver.getContent("file:laksjdhalksdhfa");
        assertNull(content);
        content = contentResolver.getContent("/org/settings4j/contentresolver/HelloWorld.txt");
        assertNotNull(content);
        assertEquals("Hello World", new String(content, "UTF-8"));
    }
