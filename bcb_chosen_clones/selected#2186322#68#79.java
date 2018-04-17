    private static void doZipFile(ZipOutputStream zipOS, String zipPath, File file) throws IOException {
        String fileName = file.getName();
        byte[] data = new byte[BUFFER];
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        zipOS.putNextEntry(new ZipEntry(zipPath + fileName));
        int count;
        while ((count = bis.read(data, 0, BUFFER)) != -1) {
            zipOS.write(data, 0, count);
        }
        zipOS.closeEntry();
        bis.close();
    }
