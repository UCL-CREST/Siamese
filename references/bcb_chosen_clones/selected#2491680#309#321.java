    private void createTextFile(ZipOutputStream zipOut, String str, String basename, boolean utf8, boolean utf16) throws IOException {
        byte[] bytes = null;
        if (utf8) {
            bytes = str.getBytes("utf-8");
            zipOut.putNextEntry(new ZipEntry(basename + ".txt"));
            zipOut.write(bytes, 0, bytes.length);
        }
        if (utf16) {
            bytes = str.getBytes("utf-16");
            zipOut.putNextEntry(new ZipEntry(basename + ".utf-16.txt"));
            zipOut.write(bytes, 0, bytes.length);
        }
    }
