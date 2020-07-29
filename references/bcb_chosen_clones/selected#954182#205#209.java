    private void copyFile(ZipEntry entry, ZipOutputStream zipout, InputStream in) throws IOException {
        zipout.putNextEntry(entry);
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) zipout.write(buffer, 0, bytesRead);
    }
