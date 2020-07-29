    private void writeJar(JarOutputStream out, byte[] buf, Object file) throws IOException {
        if (!isLeaf(file)) {
            Object[] files = listFiles(file);
            for (int i = 0; i < files.length; i++) {
                writeJar(out, buf, files[i]);
            }
        } else {
            ZipEntry z = new ZipEntry(getPath(file));
            z.setSize(getSize(file));
            z.setTime(getTime(file));
            out.putNextEntry(z);
            InputStream in = getInputStream(file);
            int i;
            while ((i = in.read(buf, 0, buf.length)) >= 0) {
                out.write(buf, 0, i);
            }
            in.close();
            out.closeEntry();
        }
    }
