    private static void copyExistingZipFiles(final ZipFile fromZip, final ZipOutputStream toZip, final String fileToOverwrite, final byte[] copyBuffer) throws IOException {
        final Enumeration entries = fromZip.entries();
        while (entries.hasMoreElements()) {
            final ZipEntry entry = (ZipEntry) entries.nextElement();
            if (!entry.getName().equals(fileToOverwrite)) {
                BufferedInputStream entryStream = null;
                try {
                    entryStream = new BufferedInputStream(fromZip.getInputStream(entry));
                    toZip.putNextEntry(entry);
                    int bytesRead;
                    while ((bytesRead = entryStream.read(copyBuffer)) != -1) {
                        toZip.write(copyBuffer, 0, bytesRead);
                    }
                } finally {
                    if (entryStream != null) {
                        try {
                            entryStream.close();
                        } catch (final IOException e) {
                            ;
                        }
                    }
                }
            }
        }
    }
