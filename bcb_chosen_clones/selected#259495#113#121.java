    @Test(dependsOnMethods = { "getSize" })
    public void download() throws IOException {
        FileObject typica = fsManager.resolveFile("s3://" + bucketName + "/jonny.zip");
        File localCache = File.createTempFile("vfs.", ".s3-test");
        FileOutputStream out = new FileOutputStream(localCache);
        IOUtils.copy(typica.getContent().getInputStream(), out);
        Assert.assertEquals(localCache.length(), typica.getContent().getSize());
        localCache.delete();
    }
