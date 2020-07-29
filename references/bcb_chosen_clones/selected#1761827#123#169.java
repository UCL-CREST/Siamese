    public void updateZipEntry(File file, String updateEntryName, String updateEntryContent) throws ZipException, IOException {
        File out = File.createTempFile("zip", "update", new File("temp"));
        ZipOutputStream zos = null;
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            zos = new ZipOutputStream(new FileOutputStream(out));
            ZipEntry entry;
            boolean updated = false;
            InputStream is;
            while (entries.hasMoreElements()) {
                entry = entries.nextElement();
                if (!updated && entry.getName().equals(updateEntryName)) {
                    zos.putNextEntry(new ZipEntry(updateEntryName));
                    zos.write(updateEntryContent.getBytes());
                    zos.closeEntry();
                    updated = true;
                } else {
                    zos.putNextEntry(entry);
                    is = zipFile.getInputStream(entry);
                    try {
                        byte[] readBuffer = new byte[4096];
                        int bytesIn = 0;
                        while ((bytesIn = is.read(readBuffer)) != -1) {
                            zos.write(readBuffer, 0, bytesIn);
                        }
                        zos.closeEntry();
                    } finally {
                        connStreamUtils.closeStreamNoError(is, "Cannot close input stream.");
                    }
                }
            }
            if (!updated) {
                zos.putNextEntry(new ZipEntry(updateEntryName));
                zos.write(updateEntryContent.getBytes());
                zos.closeEntry();
            }
        } finally {
            connStreamUtils.closeStreamNoError(zos, "Cannot close output stream.");
            zipFile.close();
        }
        file.delete();
        createCopy(out, file);
        if (!out.delete()) {
        }
    }
