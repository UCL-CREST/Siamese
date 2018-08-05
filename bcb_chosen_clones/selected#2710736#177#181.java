    private void saveVersion(ZipOutputStream zipStream) throws IOException {
        zipStream.putNextEntry(new ZipEntry(VERSION_FILENAME));
        String MZmineVersion = MZmineCore.getMZmineVersion();
        zipStream.write(MZmineVersion.getBytes());
    }
