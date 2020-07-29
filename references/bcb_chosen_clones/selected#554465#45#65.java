    private void zipSubFolder(File folder, ZipOutputStream out) throws Exception {
        File children[] = folder.listFiles();
        for (File child : children) {
            if (child.isDirectory()) {
                zipSubFolder(child, out);
            } else {
                String filePath = child.getAbsolutePath();
                String relPath = filePath.substring(folderPathIdx + 1);
                updateMonitor(child.getName());
                FileInputStream fi = new FileInputStream(child);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(relPath);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
        }
    }
