    private void writeToZip(String dirIni, File file, ZipOutputStream zipOut) throws IOException, FileNotFoundException {
        if (this.getName().equals(file.getName())) return;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                writeToZip(dirIni, f, zipOut);
            }
            return;
        }
        byte[] fileBytes = getFileBytes(file);
        dirIni = dirIni.replace("\\", "/");
        String fileName = file.getPath();
        fileName = fileName.replace(dirIni + "\\", "").replace('\\', '/');
        fileName = fileName.replace(dirIni + "/", "");
        ZipEntry entry = new ZipEntry(fileName);
        zipOut.putNextEntry(entry);
        zipOut.write(fileBytes);
        zipOut.flush();
    }
