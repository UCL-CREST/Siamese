    private void addZipEntry(ZipOutputStream zipout, String filename, String content) throws IOException {
        zipout.putNextEntry(new ZipEntry(filename));
        StringReader strin = new StringReader(content);
        int read;
        while ((read = strin.read()) != -1) {
            zipout.write(read);
        }
        zipout.closeEntry();
        zipout.flush();
    }
