    public boolean makeBackupToZip(String[] filenames, String destinationdir, String zipFileName) {
        boolean success = false;
        destinationdir = destinationdir + System.getProperty("file.separator");
        System.out.println(destinationdir);
        byte[] buf = new byte[1024];
        try {
            String outFilename = zipFileName;
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(destinationdir + outFilename));
            for (int i = 0; i < filenames.length; i++) {
                if (filenames[i] != null) {
                    FileInputStream in = new FileInputStream(filenames[i]);
                    out.putNextEntry(new ZipEntry(filenames[i].substring(filenames[i].lastIndexOf(System.getProperty("file.separator")), filenames[i].length())));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
