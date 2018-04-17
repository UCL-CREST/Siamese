    private static void addFileToTarGz(TarArchiveOutputStream taro, String path, String base) throws IOException {
        File f = new File(path);
        String entryName = base + f.getName();
        FileInputStream goIn = new FileInputStream(f);
        TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);
        taro.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        taro.putArchiveEntry(tarEntry);
        if (f.isFile()) {
            IOUtils.copy(goIn, taro);
            taro.closeArchiveEntry();
        } else {
            taro.closeArchiveEntry();
            File[] children = f.listFiles();
            if (children != null) {
                for (File child : children) {
                    addFileToTarGz(taro, child.getAbsolutePath(), entryName + "/");
                }
            }
        }
        taro.close();
        goIn.close();
    }
