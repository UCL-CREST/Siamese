    private void buildZipFile(String dirName) {
        String[] filenames = listFiles(dirName);
        byte[] buf = new byte[1024];
        try {
            String outFilename = dirName + "/outfile.zip";
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
            for (int i = 0; i < filenames.length; i++) {
                File file = new File(dirName + "/" + filenames[i]);
                if (file.exists()) {
                    FileInputStream in = new FileInputStream(dirName + "/" + filenames[i]);
                    out.putNextEntry(new ZipEntry(filenames[i]));
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
    }
