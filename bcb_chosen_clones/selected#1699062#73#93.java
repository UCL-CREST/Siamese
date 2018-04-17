    private static void recurseFiles(File root, File file, TarArchiveOutputStream taos, boolean absolute) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file2 : files) {
                recurseFiles(root, file2, taos, absolute);
            }
        } else if ((!file.getName().endsWith(".tar")) && (!file.getName().endsWith(".TAR"))) {
            String filename = null;
            if (absolute) {
                filename = file.getAbsolutePath().substring(root.getAbsolutePath().length());
            } else {
                filename = file.getName();
            }
            TarArchiveEntry tae = new TarArchiveEntry(filename);
            tae.setSize(file.length());
            taos.putArchiveEntry(tae);
            FileInputStream fis = new FileInputStream(file);
            IOUtils.copy(fis, taos);
            taos.closeArchiveEntry();
        }
    }
