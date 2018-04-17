    public static void MakeZip(final OutputStream outs, final String[] filenames) {
        final byte[] buf = new byte[2048];
        try {
            final ZipOutputStream out = new ZipOutputStream(outs);
            out.setLevel(ZipOutputStream.STORED);
            for (int i = 0; i < filenames.length; i++) {
                File file = new File(filenames[i]);
                if (file.canRead()) {
                    final FileInputStream in = new FileInputStream(file);
                    try {
                        out.putNextEntry(new ZipEntry(file.getName()));
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        out.closeEntry();
                    } catch (ZipException z) {
                        System.err.println("EXPORT ERROR (Continuing):" + z.toString());
                    }
                    in.close();
                } else {
                    System.out.println("Skipping file during zip:" + filenames[i]);
                }
            }
            out.close();
        } catch (IOException e) {
            System.err.println("EXPORT FATAL ERROR:" + e.toString());
            System.err.println(e);
        }
    }
