    private static void addZipEntry(ZipOutputStream zipOut, String rootPath, File file) throws FileNotFoundException, IOException {
        byte[] buf = new byte[1024];
        FileInputStream in = new FileInputStream(file);
        String path = "";
        if (!rootPath.equals("")) {
            rootPath = rootPath.replaceAll("\\\\", "/");
            path = file.getAbsolutePath().replaceAll("\\\\", "/");
            path = path.substring(rootPath.length());
        } else {
            path = file.getName();
        }
        zipOut.putNextEntry(new ZipEntry(path));
        int len;
        while ((len = in.read(buf)) > 0) {
            zipOut.write(buf, 0, len);
        }
        zipOut.closeEntry();
        in.close();
    }
