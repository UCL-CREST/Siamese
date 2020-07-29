    public void inScan(File file, DirectoryScanner scanner) throws Exception {
        int i;
        FileInputStream in;
        byte[] buffer = new byte[BLOCK_SIZE];
        sink_.putNextEntry(new ZipEntry(scanner.path_.toString().replace(File.separatorChar, '/')));
        in = new FileInputStream(file);
        try {
            while ((i = in.read(buffer)) > 0) sink_.write(buffer, 0, i);
        } catch (Exception e) {
        }
        in.close();
        sink_.closeEntry();
    }
