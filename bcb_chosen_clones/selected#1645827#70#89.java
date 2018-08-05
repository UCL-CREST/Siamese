    public static void unzip2(String strZipFile, String folder) throws IOException, ArchiveException {
        FileUtil.fileExists(strZipFile, true);
        final InputStream is = new FileInputStream(strZipFile);
        ArchiveInputStream in = new ArchiveStreamFactory().createArchiveInputStream("zip", is);
        ZipArchiveEntry entry = null;
        OutputStream out = null;
        while ((entry = (ZipArchiveEntry) in.getNextEntry()) != null) {
            File zipPath = new File(folder);
            File destinationFilePath = new File(zipPath, entry.getName());
            destinationFilePath.getParentFile().mkdirs();
            if (entry.isDirectory()) {
                continue;
            } else {
                out = new FileOutputStream(new File(folder, entry.getName()));
                IOUtils.copy(in, out);
                out.close();
            }
        }
        in.close();
    }
