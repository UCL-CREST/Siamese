    public void add2zip(String chroot, File file2zip, ZipOutputStream zos) throws IOException {
        final int BUFFER = 2048;
        String[] dirList;
        byte[] readBuffer = new byte[BUFFER];
        int bytesIn = 0;
        if (file2zip.isDirectory()) {
            dirList = file2zip.list();
        } else {
            dirList = new String[1];
            dirList[0] = "";
        }
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(file2zip, dirList[i]);
            if (f.isDirectory()) {
                add2zip(chroot, f, zos);
            } else {
                FileInputStream fis = new FileInputStream(f);
                ZipEntry entry = new ZipEntry(f.getPath().substring(chroot.length()));
                zos.putNextEntry(entry);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
            }
        }
    }
