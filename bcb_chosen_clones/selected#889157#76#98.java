    public void zipFile(String filedir, String zippath) {
        List fl = this.getAllFilePath(filedir);
        File f = new File(zippath);
        try {
            FileOutputStream fo = new FileOutputStream(f);
            ZipOutputStream zo = new ZipOutputStream(fo);
            for (int i = 0; i < fl.size(); i++) {
                File ff = (File) fl.get(i);
                ZipEntry z = new ZipEntry(this.getZipEntryPath(ff.getPath(), filedir));
                zo.putNextEntry(z);
                FileInputStream fi = new FileInputStream(ff);
                byte inbuf[] = new byte[BUFSIZE];
                int n = 0;
                while ((n = fi.read(inbuf, 0, BUFSIZE)) != -1) {
                    zo.write(inbuf, 0, n);
                }
                fi.close();
            }
            zo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
