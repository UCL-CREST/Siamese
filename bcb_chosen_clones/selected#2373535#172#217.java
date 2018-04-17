    public void buildZipFile() {
        File bdir = new File(getBeanDir());
        File mdir = new File(sloc + psep + "META-INF");
        File zdir = new File(sloc);
        String zname = name + ".zip";
        File myZipFile = new File(zdir, zname);
        byte[] buf = new byte[1024];
        try {
            myZipFile.createNewFile();
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(myZipFile));
            File[] mfiles = mdir.listFiles();
            File[] bfiles = bdir.listFiles();
            StringTokenizer dirs = new StringTokenizer(pack, "\\.");
            String dpath = "";
            out.putNextEntry(new ZipEntry("META-INF/"));
            for (int i = 0; i < mfiles.length; i++) {
                File cfile = new File(sloc + psep + "META-INF" + psep + mfiles[i].getName());
                FileInputStream in = new FileInputStream(cfile);
                out.putNextEntry(new ZipEntry("META-INF/" + mfiles[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            while (dirs.hasMoreTokens()) {
                dpath = dpath + dirs.nextToken() + "/";
                out.putNextEntry(new ZipEntry(dpath));
            }
            for (int i = 0; i < bfiles.length; i++) {
                File cfile = new File(getBeanDir() + psep + bfiles[i].getName());
                FileInputStream in = new FileInputStream(cfile);
                out.putNextEntry(new ZipEntry(dpath + bfiles[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
            System.out.println("I/O Exception: " + e);
        }
    }
