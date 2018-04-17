    private static void addFile(File file, TarArchiveOutputStream taos) throws IOException {
        String filename = null;
        filename = file.getName();
        TarArchiveEntry tae = new TarArchiveEntry(filename);
        tae.setSize(file.length());
        taos.putArchiveEntry(tae);
        FileInputStream fis = new FileInputStream(file);
        IOUtils.copy(fis, taos);
        taos.closeArchiveEntry();
    }
