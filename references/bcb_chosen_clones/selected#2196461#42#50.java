    private static void addFile(File source, File root, ZipOutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        InputStream in = new BufferedInputStream(new FileInputStream(source));
        ZipEntry entry = new ZipEntry(FileUtil.relativePath(root, source));
        out.putNextEntry(entry);
        int count;
        while ((count = in.read(buffer, 0, BUFFER_SIZE)) != -1) out.write(buffer, 0, count);
        in.close();
    }
