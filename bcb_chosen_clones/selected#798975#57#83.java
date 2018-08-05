    public static void zipFiles(String[] infiles, String[] filenames, String outfile) {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outfile));
            for (int i = 0; i < infiles.length; i++) {
                File f = new File(infiles[i]);
                FileInputStream in = new FileInputStream(f);
                String fname = f.getName();
                if (filenames != null) {
                    if (filenames.length == infiles.length) {
                        if (!filenames[i].trim().equalsIgnoreCase("")) {
                            fname = filenames[i];
                        }
                    }
                }
                out.putNextEntry(new ZipEntry(fname));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
        }
    }
