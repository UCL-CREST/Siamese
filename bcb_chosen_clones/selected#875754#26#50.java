    public void outputEntry(ZipOutputStream zos, File f, boolean isRecursive) throws Error, IOException {
        String adjustedPath = f.getPath().replace(File.separatorChar, '/');
        if (f.isDirectory() && !adjustedPath.endsWith("/")) {
            adjustedPath += '/';
        }
        String fileName = f.getName();
        ZipEntry entry = new ZipEntry(fileName);
        entry.setTime(f.lastModified());
        zos.putNextEntry(entry);
        if (f.isDirectory()) {
            if (isRecursive) {
                String[] files = f.list();
                for (int i = 0; i < files.length; ++i) {
                    outputEntry(zos, new File(f, files[i]), isRecursive);
                }
            }
        } else {
            FileInputStream fis = new FileInputStream(f);
            byte buf[] = new byte[1024];
            for (int cnt; (cnt = fis.read(buf)) != -1; ) {
                zos.write(buf, 0, cnt);
            }
            fis.close();
        }
    }
