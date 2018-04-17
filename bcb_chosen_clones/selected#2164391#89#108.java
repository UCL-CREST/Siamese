    public static void addToZip(File f, ZipOutputStream zip) throws IOException {
        if (f.isDirectory()) {
            for (File s : f.listFiles()) {
                addToZip(s, zip);
            }
        } else {
            String baseName = FileDestDir.getCanonicalPath();
            String fullPath = f.getCanonicalPath();
            String nameSufix = fullPath.substring(baseName.length() + 1);
            FileInputStream in = new FileInputStream(f);
            byte[] data = new byte[in.available()];
            in.read(data);
            ZipEntry entry = new ZipEntry(nameSufix.replace('\\', '/'));
            entry.setSize(data.length);
            entry.setTime(f.lastModified());
            zip.putNextEntry(entry);
            zip.write(data);
            zip.closeEntry();
        }
    }
