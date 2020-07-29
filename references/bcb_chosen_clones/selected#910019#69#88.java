    public void close() throws IOException {
        if (version < 2) return;
        for (ZipSerializerImage zsi : images) {
            ZipEntry ze = new ZipEntry(zsi.name);
            zos.putNextEntry(ze);
            ImageIO.write(zsi.im, "png", zos);
        }
        for (ZipSerializerFile zsf : files) {
            ZipEntry ze = new ZipEntry(zsf.internalname);
            zos.putNextEntry(ze);
            byte[] buf = new byte[1024];
            int i = 0;
            FileInputStream is = new FileInputStream(zsf.filename);
            while ((i = is.read(buf)) != -1) {
                zos.write(buf, 0, i);
            }
            is.close();
        }
        zos.close();
    }
