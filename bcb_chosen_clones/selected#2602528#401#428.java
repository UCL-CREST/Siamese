    void create(OutputStream out, Manifest manifest) throws IOException {
        ZipOutputStream zos = new JarOutputStream(out);
        if (flag0) {
            zos.setMethod(ZipOutputStream.STORED);
        }
        if (manifest != null) {
            if (vflag) {
                output(getMsg("out.added.manifest"));
            }
            ZipEntry e = new ZipEntry(MANIFEST_DIR);
            e.setTime(System.currentTimeMillis());
            e.setSize(0);
            e.setCrc(0);
            zos.putNextEntry(e);
            e = new ZipEntry(MANIFEST_NAME);
            e.setTime(System.currentTimeMillis());
            if (flag0) {
                crc32Manifest(e, manifest);
            }
            zos.putNextEntry(e);
            manifest.write(zos);
            zos.closeEntry();
        }
        for (File file : entries) {
            addFile(zos, file);
        }
        zos.close();
    }
