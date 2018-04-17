    public void putNextEntry(String nombre, File f) throws IOException {
        byte[] data = new byte[BUFFER_SIZE];
        LeedorFileChannel origin = new LeedorFileChannel(f);
        ZipEntry entry = new ZipEntry(nombre);
        out.putNextEntry(entry);
        int count;
        while ((count = origin.read(data)) != -1) {
            out.write(data, 0, count);
        }
        origin.close();
    }
