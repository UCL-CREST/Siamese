    private void copyFromFile(ZipOutputStream zipOut, File file, String root) throws Exception {
        int len = 0;
        byte[] buffer = ContentManager.getDefaultBuffer();
        BufferedInputStream buff_inf;
        buff_inf = new BufferedInputStream(new FileInputStream(file), buffer.length);
        zipOut.putNextEntry(new ZipEntry(root + file.getName()));
        while ((len = buff_inf.read(buffer)) > 0) zipOut.write(buffer, 0, len);
        buff_inf.close();
    }
