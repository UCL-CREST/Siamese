    private static boolean addFileToZip(ZipOutputStream zipWriter, File zipArchive, File file, final byte[] readBuffer, String currentPath) throws IOException {
        if (zipArchive.getAbsoluteFile().equals(file.getAbsoluteFile())) {
            return true;
        }
        InputStream fileInputStream = null;
        try {
            if (!file.canRead()) {
                return false;
            }
            fileInputStream = Channels.newInputStream(new FileInputStream(file).getChannel());
            ZipEntry fileEntry = new ZipEntry(currentPath + file.getName());
            zipWriter.putNextEntry(fileEntry);
            int readBytes;
            while ((readBytes = fileInputStream.read(readBuffer)) > 0) {
                zipWriter.write(readBuffer, 0, readBytes);
                zipWriter.flush();
            }
            zipWriter.closeEntry();
            zipWriter.flush();
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }
        return true;
    }
