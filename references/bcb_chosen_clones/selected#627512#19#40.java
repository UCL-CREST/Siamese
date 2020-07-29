    public static void zipFiles(String filename, String[] files) throws IOException {
        ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
        byte[] buffer = new byte[10000];
        for (int i = 0; i < files.length; i++) {
            File f = new File(files[i]);
            if (f.exists()) {
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(files[i]));
                zip.putNextEntry(new ZipEntry(files[i]));
                boolean done = false;
                while (!done) {
                    int num = in.read(buffer);
                    if (num > 0) {
                        zip.write(buffer, 0, num);
                    }
                    done = (num < buffer.length);
                }
                in.close();
            }
        }
        zip.flush();
        zip.close();
    }
