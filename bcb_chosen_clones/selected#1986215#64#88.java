    public static void packeAlleDrinkObjekte(String zipfile) {
        File f = new File(".");
        try {
            String entries[] = f.list(new DrnFilenameFilter());
            byte[] buf = new byte[BLOCKSIZE];
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            for (int i = 0; i < entries.length; ++i) {
                File ff = new File(entries[i]);
                System.out.println("adding " + ff);
                FileReader in = new FileReader(ff);
                out.putNextEntry(new ZipEntry(entries[i]));
                int len;
                while ((len = in.read()) > 0) {
                    out.write(len);
                }
                in.close();
                in = null;
                FileUtil.deleteFile(ff);
            }
            out.close();
            out = null;
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }
