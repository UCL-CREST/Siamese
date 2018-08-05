    private void addToZip(ZipOutputStream out, File file, String path) throws Exception {
        byte data[] = null;
        ZipEntry entry = null;
        if (file != null) {
            String name = file.getAbsolutePath();
            name = name.substring(path.length() + 1, name.length()).replace('\\', '/');
            System.out.println(">>>> Adding: " + name);
            if (file != null) {
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (File f : files) {
                        addToZip(out, f, path);
                    }
                } else {
                    entry = new ZipEntry(name);
                    out.putNextEntry(entry);
                    data = read(file);
                    if (data != null && data.length > 0) {
                        out.write(data, 0, data.length);
                    }
                    out.closeEntry();
                    out.flush();
                }
            }
        }
    }
