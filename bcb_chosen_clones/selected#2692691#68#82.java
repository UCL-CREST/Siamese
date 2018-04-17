    public static void appendFile(String namePrefix, File baseDir, File file, ZipOutputStream zipOut) throws IOException {
        Assert.Arg.notNull(baseDir, "baseDir");
        Assert.Arg.notNull(file, "file");
        Assert.Arg.notNull(zipOut, "zipOut");
        if (namePrefix == null) namePrefix = "";
        String path = FileSystemUtils.getRelativePath(baseDir, file);
        ZipEntry zipEntry = new ZipEntry(namePrefix + path);
        zipOut.putNextEntry(zipEntry);
        InputStream fileInput = FileUtils.openInputStream(file);
        try {
            org.apache.commons.io.IOUtils.copyLarge(fileInput, zipOut);
        } finally {
            fileInput.close();
        }
    }
