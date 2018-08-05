    public void write(ZipOutputStream out) throws IOException {
        FileInputStream in = null;
        try {
            out.putNextEntry(new ZipEntry(relativePath + getName()));
            in = new FileInputStream(this);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            out.write(buffer, 0, buffer.length);
        } finally {
            try {
                out.closeEntry();
            } catch (IOException ex) {
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
        }
    }
