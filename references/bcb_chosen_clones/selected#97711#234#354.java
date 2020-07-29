    private OutputStream getOutputStream(boolean delete, long timeStamp) throws IOException {
        final String nestedURL = getNestedURL();
        final File tempFile = File.createTempFile("Archive", "zip");
        InputStream sourceInputStream = null;
        OutputStream tempOutputStream = null;
        try {
            tempOutputStream = new FileOutputStream(tempFile);
            try {
                sourceInputStream = createInputStream(nestedURL);
            } catch (IOException exception) {
            }
            OutputStream outputStream = tempOutputStream;
            InputStream inputStream = sourceInputStream;
            int archiveSeparator = urlString.indexOf(nestedURL) + nestedURL.length();
            int nextArchiveSeparator = urlString.indexOf("!/", archiveSeparator + 2);
            ZipOutputStream zipOutputStream;
            final byte[] bytes = new byte[4096];
            ZipEntry outputZipEntry;
            boolean found = false;
            for (; ; ) {
                String entry = URI.decode(nextArchiveSeparator < 0 ? urlString.substring(archiveSeparator + 2) : urlString.substring(archiveSeparator + 2, nextArchiveSeparator));
                zipOutputStream = null;
                ZipInputStream zipInputStream = inputStream == null ? null : new ZipInputStream(inputStream);
                inputStream = zipInputStream;
                while (zipInputStream != null && zipInputStream.available() >= 0) {
                    ZipEntry zipEntry = zipInputStream.getNextEntry();
                    if (zipEntry == null) {
                        break;
                    } else {
                        boolean match = entry.equals(zipEntry.getName());
                        if (!found) {
                            found = match && nextArchiveSeparator < 0;
                        }
                        if (timeStamp != -1 || !match) {
                            if (zipOutputStream == null) {
                                zipOutputStream = new ZipOutputStream(outputStream);
                                outputStream = zipOutputStream;
                            }
                            if (timeStamp != -1 && match && nextArchiveSeparator < 0) {
                                zipEntry.setTime(timeStamp);
                            }
                            zipOutputStream.putNextEntry(zipEntry);
                            for (int size; (size = zipInputStream.read(bytes, 0, bytes.length)) > -1; ) {
                                zipOutputStream.write(bytes, 0, size);
                            }
                        }
                    }
                }
                archiveSeparator = nextArchiveSeparator;
                nextArchiveSeparator = urlString.indexOf("!/", archiveSeparator + 2);
                if ((delete || timeStamp != -1) && archiveSeparator < 0) {
                    if (!found) {
                        throw new IOException("Archive entry not found " + urlString);
                    }
                    outputZipEntry = null;
                    break;
                } else {
                    outputZipEntry = new ZipEntry(entry);
                    if (zipOutputStream == null) {
                        zipOutputStream = new ZipOutputStream(outputStream);
                        outputStream = zipOutputStream;
                    }
                    zipOutputStream.putNextEntry(outputZipEntry);
                    if (archiveSeparator > 0) {
                        continue;
                    } else {
                        break;
                    }
                }
            }
            tempOutputStream = null;
            final boolean deleteRequired = sourceInputStream != null;
            FilterOutputStream result = new FilterOutputStream(zipOutputStream == null ? outputStream : zipOutputStream) {

                protected boolean isClosed;

                @Override
                public void close() throws IOException {
                    if (!isClosed) {
                        isClosed = true;
                        super.close();
                        boolean useRenameTo = nestedURL.startsWith("file:");
                        if (useRenameTo) {
                            File targetFile = new File(URI.decode(nestedURL.substring(5)));
                            if (deleteRequired && !targetFile.delete()) {
                                throw new IOException("cannot delete " + targetFile.getPath());
                            } else if (!tempFile.renameTo(targetFile)) {
                                useRenameTo = false;
                            }
                        }
                        if (!useRenameTo) {
                            InputStream inputStream = null;
                            OutputStream outputStream = null;
                            try {
                                inputStream = new FileInputStream(tempFile);
                                outputStream = createOutputStream(nestedURL);
                                for (int size; (size = inputStream.read(bytes, 0, bytes.length)) > -1; ) {
                                    outputStream.write(bytes, 0, size);
                                }
                            } finally {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (outputStream != null) {
                                    outputStream.close();
                                }
                            }
                        }
                    }
                }
            };
            return outputZipEntry == null ? result : yield(outputZipEntry, result);
        } finally {
            if (tempOutputStream != null) {
                tempOutputStream.close();
            }
            if (sourceInputStream != null) {
                sourceInputStream.close();
            }
        }
    }
