    private static void addToZip(ZipOutputStream zout, String file, String code, String currentDirectory) {
        try {
            ZipEntry entry = new ZipEntry(currentDirectory + file);
            zout.putNextEntry(entry);
            zout.write(code.getBytes(), 0, code.length());
            zout.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
