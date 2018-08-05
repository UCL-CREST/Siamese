    private void putEntry(ZipOutputStream zo, File file) throws Exception {
        String name = file.getPath().substring(prefix.length());
        ZipEntry entry = new ZipEntry(name);
        zo.putNextEntry(entry);
        BufferedInputStream bi = new BufferedInputStream(new FileInputStream(file));
        while (true) {
            int n = bi.read(buffer);
            if (n < 0) break;
            zo.write(buffer, 0, n);
        }
        zo.closeEntry();
        bi.close();
    }
