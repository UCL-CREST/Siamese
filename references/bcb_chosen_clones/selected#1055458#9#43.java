    public void be(String zipFileName, String[] filesToZip, int mennyire, String akt_konyvtar) throws Exception {
        byte[] buffer = new byte[18024];
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        switch(mennyire) {
            case 0:
                {
                    out.setLevel(Deflater.BEST_SPEED);
                    break;
                }
            case 1:
                {
                    out.setLevel(Deflater.DEFAULT_COMPRESSION);
                    break;
                }
            case 2:
                {
                    out.setLevel(Deflater.BEST_COMPRESSION);
                    break;
                }
        }
        for (int i = 0; i < filesToZip.length; i++) {
            FileInputStream in = new FileInputStream(filesToZip[i]);
            String utvonal = filesToZip[i].substring(akt_konyvtar.length() - 1, filesToZip[i].length());
            out.putNextEntry(new ZipEntry(utvonal));
            File u = new File(filesToZip[i]);
            if (u.isDirectory()) continue;
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.closeEntry();
            in.close();
        }
        out.close();
    }
