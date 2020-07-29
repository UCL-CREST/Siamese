    @Test
    public void shouldDownloadFileUsingPublicLink() throws Exception {
        String bucketName = "test-" + UUID.randomUUID();
        Service service = new WebClientService(credentials);
        service.createBucket(bucketName);
        File file = folder.newFile("foo.txt");
        FileUtils.writeStringToFile(file, UUID.randomUUID().toString());
        service.createObject(bucketName, file.getName(), file, new NullProgressListener());
        String publicUrl = service.getPublicUrl(bucketName, file.getName(), new DateTime().plusDays(5));
        File saved = folder.newFile("saved.txt");
        InputStream input = new URL(publicUrl).openConnection().getInputStream();
        FileOutputStream output = new FileOutputStream(saved);
        IOUtils.copy(input, output);
        output.close();
        assertThat("Corrupted download", Files.computeMD5(saved), equalTo(Files.computeMD5(file)));
        service.deleteObject(bucketName, file.getName());
        service.deleteBucket(bucketName);
    }
