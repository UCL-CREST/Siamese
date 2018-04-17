    public static void zipFilesRecursively(ZipOutputStream out, File zipOutputFile, File[] files, File entryRoot) throws IOException {
        if (entryRoot == null && files == null) throw new IllegalArgumentException("entryRoot and files must not both be null!");
        if (entryRoot != null && !entryRoot.isDirectory()) throw new IllegalArgumentException("entryRoot is not a directory: " + entryRoot.getAbsolutePath());
        if (files == null) {
            files = new File[] { entryRoot };
        }
        byte[] buf = new byte[1024 * 5];
        for (File file : files) {
            if (zipOutputFile != null) if (file.equals(zipOutputFile)) continue;
            if (file.isDirectory()) {
                File[] dirFiles = file.listFiles();
                zipFilesRecursively(out, zipOutputFile, dirFiles, entryRoot);
            } else {
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                String relativePath = entryRoot == null ? file.getName() : getRelativePath(entryRoot, file.getAbsoluteFile());
                ZipEntry entry = new ZipEntry(relativePath);
                entry.setTime(file.lastModified());
                out.putNextEntry(entry);
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
        }
    }
