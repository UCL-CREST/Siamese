    public File getLayerFile(String id) throws IOException {
        File f = null;
        if (zipFile != null) {
            ZipOutputStream out = null;
            byte[] buf = new byte[1024];
            for (Enumeration e = zipFile.entries(); e.hasMoreElements(); ) {
                ZipEntry ze = (ZipEntry) e.nextElement();
                String baseName = getBase(ze.getName());
                if (baseName.equals(id)) {
                    if (out == null) {
                        f = File.createTempFile("layer_", ".zip");
                        out = new ZipOutputStream(new FileOutputStream(f));
                    }
                    InputStream in = zipFile.getInputStream(ze);
                    out.putNextEntry(new ZipEntry(ze.getName()));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                }
            }
            if (out != null) {
                out.close();
            }
        } else {
            f = file;
        }
        return f;
    }
