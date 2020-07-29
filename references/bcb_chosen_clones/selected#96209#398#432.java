    public static void createJarFile(String fileName, Properties manifest, File filesToJar[], FileFilter filter) throws IOException {
        ZipOutputStream outStream = null;
        try {
            outStream = new ZipOutputStream(new FileOutputStream(fileName));
            ZipEntry entry = new ZipEntry("META-INF/MANIFEST.MF");
            outStream.putNextEntry(entry);
            outStream.write("Manifest-Version: 1.0\r\n".getBytes());
            outStream.write("Created-By: Perfmon4j\r\n".getBytes());
            Iterator<Map.Entry<Object, Object>> manifestItr = manifest.entrySet().iterator();
            while (manifestItr.hasNext()) {
                Map.Entry<Object, Object> e = manifestItr.next();
                outStream.write((e.getKey() + ": " + e.getValue() + "\r\n").getBytes());
            }
            outStream.closeEntry();
            for (File f : filesToJar) {
                if (f.isDirectory()) {
                    File filesFromDirectory[] = f.listFiles(filter);
                    for (File f2 : filesFromDirectory) {
                        if (!f.getName().startsWith(".")) {
                            addFileToZipOutputStream(outStream, f2, "", filter);
                        } else {
                            logger.logDebug("Skipping hidden file/directory: " + f.getName());
                        }
                    }
                } else {
                    addFileToZipOutputStream(outStream, f, "", filter);
                }
            }
        } finally {
            if (outStream != null) {
                outStream.flush();
                outStream.close();
            }
        }
    }
