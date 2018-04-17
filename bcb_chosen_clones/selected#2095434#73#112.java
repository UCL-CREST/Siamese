    private static void unsignJar(File origFile, File destFile) throws ZipException, IOException {
        log.info("removing signing info from " + origFile);
        ZipFile zip = new ZipFile(origFile);
        byte[] buff = new byte[1024];
        File tmpFile = new File(destFile.getPath() + ".tmp");
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(tmpFile));
        InputStream in = null;
        try {
            Enumeration e = zip.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                String name = entry.getName().toLowerCase();
                if (name.endsWith(".sf") || name.endsWith(".rsa")) {
                    continue;
                }
                out.putNextEntry(entry);
                in = zip.getInputStream(entry);
                try {
                    for (int len = in.read(buff); len != -1; len = in.read(buff)) {
                        out.write(buff, 0, len);
                    }
                } finally {
                    if (in != null) {
                        in.close();
                        in = null;
                    }
                    out.flush();
                }
            }
        } finally {
            zip.close();
            out.flush();
            out.close();
        }
        tmpFile.renameTo(destFile);
        if (!origFile.delete()) {
            log.warn("Cannot delete original file: " + origFile);
        }
        log.info("files saved as " + destFile);
    }
