    private File makeMockWar() {
        try {
            File war = File.createTempFile("test", ".war");
            war.deleteOnExit();
            ZipOutputStream os = new ZipOutputStream(new FileOutputStream(war));
            ZipEntry entry = new ZipEntry("file1");
            os.putNextEntry(entry);
            os.write(new byte[] { 0, 1, 2 });
            entry = new ZipEntry("dir/file2");
            os.putNextEntry(entry);
            os.write(new byte[] { 0, 1, 2, 3 });
            os.flush();
            os.close();
            return war;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
