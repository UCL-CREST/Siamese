    public static void compress(String path, String parent, ZipOutputStream zip) throws IOException {
        File[] f = new File(path).listFiles();
        byte[] buffer = new byte[4096];
        int bytes_read;
        for (int i = 0; i < f.length; i++) {
            if (f[i].isFile()) {
                FileInputStream in = new FileInputStream(f[i]);
                ZipEntry entry = new ZipEntry(parent + f[i].getName());
                zip.putNextEntry(entry);
                while ((bytes_read = in.read(buffer)) != -1) zip.write(buffer, 0, bytes_read);
                in.close();
            } else if (f[i].isDirectory()) {
                compress(f[i].getAbsolutePath(), parent + f[i].getName() + File.separator, zip);
            }
        }
    }
