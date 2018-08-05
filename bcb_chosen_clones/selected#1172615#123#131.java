    protected void zipFile(File from, File to) throws IOException {
        FileInputStream in = new FileInputStream(from);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(to));
        byte[] buffer = new byte[4096];
        int bytes_read;
        while ((bytes_read = in.read(buffer)) != -1) out.write(buffer, 0, bytes_read);
        in.close();
        out.close();
    }
