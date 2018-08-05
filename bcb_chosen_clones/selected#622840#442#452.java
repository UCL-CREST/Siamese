    private void addFileToZip(String filePath, ZipOutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int bytes_read;
        FileInputStream in = new FileInputStream(filePath);
        ZipEntry zip = new ZipEntry((new File(filePath)).getPath());
        out.putNextEntry(zip);
        while ((bytes_read = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytes_read);
        }
        in.close();
    }
