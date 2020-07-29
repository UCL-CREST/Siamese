    private static void pack() throws Exception {
        File apk = new File(apkPath);
        File directory = new File(apk.getParent(), apk.getName().replace(".apk", "_") + System.currentTimeMillis());
        directory.mkdir();
        packageDex(directory);
        File bak = new File(directory, apk.getName());
        ZipInputStream in = new ZipInputStream(new FileInputStream(apk));
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(bak));
        byte[] b = new byte[102400];
        int len;
        ZipEntry entry;
        while ((entry = in.getNextEntry()) != null) {
            if (dex.equals(entry.getName())) {
                out.putNextEntry(new ZipEntry(entry.getName()));
                FileInputStream read = new FileInputStream(directory + "/" + dex);
                while ((len = read.read(b)) != -1) {
                    if (len != 0) out.write(b, 0, len);
                }
                read.close();
            } else {
                out.putNextEntry(entry);
                while ((len = in.read(b)) != -1) {
                    if (len != 0) out.write(b, 0, len);
                }
                in.closeEntry();
            }
            out.closeEntry();
        }
        in.close();
        out.finish();
        out.close();
        signApk(bak);
        bak.renameTo(new File(directory.getPath() + ".apk"));
        for (File f : directory.listFiles()) {
            f.delete();
        }
        directory.delete();
    }
