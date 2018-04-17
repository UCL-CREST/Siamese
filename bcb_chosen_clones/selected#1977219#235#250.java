    public static void zipChildren(File folder, String prefix, ZipOutputStream out) throws IOException {
        File[] files = folder.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isFile()) {
                String name = prefix + file.getName();
                ZipEntry entry = new ZipEntry(name);
                entry.setTime(file.lastModified());
                out.putNextEntry(entry);
                loadFromFile(file, out);
                out.closeEntry();
            } else if (file.isDirectory()) {
                zipChildren(file, prefix + file.getName() + "/", out);
            }
        }
    }
