    private void writeZipFile(File f, ZipOutputStream zos, String hiberarchy) throws IOException {
        if (f.exists()) {
            if (f.isDirectory()) {
                hiberarchy += f.getName() + "/";
                File[] fif = f.listFiles();
                for (int i = 0; i < fif.length; i++) {
                    writeZipFile(fif[i], zos, hiberarchy);
                }
            } else {
                FileInputStream fis = null;
                fis = new FileInputStream(f);
                ZipEntry ze = new ZipEntry(hiberarchy + f.getName());
                zos.putNextEntry(ze);
                byte[] b = new byte[1024];
                while (fis.read(b) != -1) {
                    zos.write(b);
                    b = new byte[1024];
                }
                if (fis != null) fis.close();
            }
        }
    }
