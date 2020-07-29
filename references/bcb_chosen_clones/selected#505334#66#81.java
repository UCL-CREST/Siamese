    public void testReadNormal() throws Exception {
        archiveFileManager.executeWith(new TemporaryFileExecutor() {

            public void execute(File temporaryFile) throws Exception {
                ZipArchive archive = new ZipArchive(temporaryFile.getPath());
                InputStream input = archive.getInputFrom(ARCHIVE_FILE_1);
                if (input != null) {
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    IOUtils.copyAndClose(input, output);
                    assertEquals(ARCHIVE_FILE_1 + " contents not correct", ARCHIVE_FILE_1_CONTENT, output.toString());
                } else {
                    fail("cannot open " + ARCHIVE_FILE_1);
                }
            }
        });
    }
