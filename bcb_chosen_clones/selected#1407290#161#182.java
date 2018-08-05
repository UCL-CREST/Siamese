    public void zip_compressFiles() throws Exception {
        FileInputStream in = null;
        File f1 = new File("C:\\WINDOWS\\regedit.exe");
        File f2 = new File("C:\\WINDOWS\\win.ini");
        File file = new File("C:\\" + NTUtil.class.getName() + ".zip");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
        out.putNextEntry(new ZipEntry("regedit.exe"));
        in = new FileInputStream(f1);
        while (in.available() > 0) {
            out.write(in.read());
        }
        in.close();
        out.closeEntry();
        out.putNextEntry(new ZipEntry("win.ini"));
        in = new FileInputStream(f2);
        while (in.available() > 0) {
            out.write(in.read());
        }
        in.close();
        out.closeEntry();
        out.close();
    }
