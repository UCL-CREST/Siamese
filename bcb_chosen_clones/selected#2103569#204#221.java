    private void writeZipEntry(ZipOutputStream zipOut, String entryName, Content content) throws IOException {
        checkCurrentThreadIsntInterrupted();
        byte[] buffer = new byte[8192];
        InputStream contentIn = null;
        try {
            zipOut.putNextEntry(new ZipEntry(entryName));
            contentIn = content.openStream();
            int size;
            while ((size = contentIn.read(buffer)) != -1) {
                zipOut.write(buffer, 0, size);
            }
            zipOut.closeEntry();
        } finally {
            if (contentIn != null) {
                contentIn.close();
            }
        }
    }
