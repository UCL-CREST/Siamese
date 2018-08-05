    private void addEditLog(ZipOutputStream zos, NucleiMgr nucleiMgr) throws IOException {
        ZipNuclei zn = nucleiMgr.getZipNuclei();
        String ename = nucleiMgr.PARAMETERS + C.Fileseparator + "EditLog.txt";
        zos.putNextEntry(new ZipEntry(ename));
        zos.write(iEditLog.getText().getBytes());
        zos.closeEntry();
    }
