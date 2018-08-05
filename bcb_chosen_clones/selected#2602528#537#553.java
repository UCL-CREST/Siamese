    private void updateManifest(Manifest m, ZipOutputStream zos) throws IOException {
        addVersion(m);
        addCreatedBy(m);
        if (ename != null) {
            addMainClass(m, ename);
        }
        ZipEntry e = new ZipEntry(MANIFEST_NAME);
        e.setTime(System.currentTimeMillis());
        if (flag0) {
            crc32Manifest(e, m);
        }
        zos.putNextEntry(e);
        m.write(zos);
        if (vflag) {
            output(getMsg("out.update.manifest"));
        }
    }
