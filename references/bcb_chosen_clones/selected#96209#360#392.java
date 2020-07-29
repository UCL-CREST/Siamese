    private static void addFileToZipOutputStream(ZipOutputStream stream, File file, String path, FileFilter filter) throws IOException {
        if (file.getName().startsWith(".")) {
            logger.logDebug("Skipping hidden file/directory: " + file.getName());
            return;
        }
        if (!"".equals(path)) {
            path += "/";
        }
        path += file.getName();
        if (file.isDirectory()) {
            File files[] = file.listFiles(filter);
            for (File f : files) {
                addFileToZipOutputStream(stream, f, path, filter);
            }
        } else {
            ZipEntry entry = new ZipEntry(path);
            FileInputStream inFile = null;
            try {
                inFile = new FileInputStream(file);
                stream.putNextEntry(entry);
                byte buffer[] = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = inFile.read(buffer)) != -1) {
                    stream.write(buffer, 0, bytesRead);
                }
                stream.closeEntry();
            } finally {
                if (inFile != null) {
                    inFile.close();
                }
            }
        }
    }
