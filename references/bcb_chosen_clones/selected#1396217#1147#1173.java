    public static boolean ZipFiles(String[] files, String zipname, boolean type) throws Exception {
        FileOutputStream os = new FileOutputStream(zipname);
        ZipOutputStream zip = new ZipOutputStream(os);
        for (int i = 0; i < files.length; i++) {
            File file = new File(files[i]);
            if (file.exists()) {
                byte[] buf = new byte[1024];
                int len;
                ZipEntry zipEntry = new ZipEntry(type ? file.getName() : file.getPath());
                try {
                    FileInputStream fin = new FileInputStream(file);
                    BufferedInputStream in = new BufferedInputStream(fin);
                    zip.putNextEntry(zipEntry);
                    while ((len = in.read(buf)) >= 0) {
                        zip.write(buf, 0, len);
                    }
                    in.close();
                    zip.closeEntry();
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
            }
        }
        zip.close();
        os.close();
        return true;
    }
