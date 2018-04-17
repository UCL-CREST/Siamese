    public void testCopyFolderContents() throws IOException {
        log.info("Running: testCopyFolderContents()");
        IOUtils.copyFolderContents(srcFolderName, destFolderName);
        Assert.assertTrue(destFile1.exists() && destFile1.isFile());
        Assert.assertTrue(destFile2.exists() && destFile2.isFile());
        Assert.assertTrue(destFile3.exists() && destFile3.isFile());
    }
