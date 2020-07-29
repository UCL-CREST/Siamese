    private static boolean addZipEntry(ZipOutputStream zipOutputStream, File file, String entryName) throws IOException {
        boolean addZipEntry = false;
        if ((zipOutputStream != null) && (file != null) && (file.isFile())) {
            BufferedInputStream anInStream = null;
            try {
                FileInputStream aFileStream = new FileInputStream(file);
                anInStream = new BufferedInputStream(aFileStream);
                String anEntryName = entryName;
                if (anEntryName == null) {
                    anEntryName = file.getName();
                }
                ZipEntry aZipEntry = new ZipEntry(anEntryName);
                zipOutputStream.putNextEntry(aZipEntry);
                int aBytesRead = -1;
                byte[] aBuffer = new byte[DEFAULT_BUFFER_SIZE];
                while ((aBytesRead = anInStream.read(aBuffer, 0, aBuffer.length)) != -1) {
                    zipOutputStream.write(aBuffer, 0, aBytesRead);
                }
                addZipEntry = true;
            } catch (Exception anException) {
                LOG.warn(null, anException);
            } finally {
                if (anInStream != null) {
                    anInStream.close();
                }
                zipOutputStream.closeEntry();
            }
        }
        return addZipEntry;
    }
