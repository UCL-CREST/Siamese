    private byte[] injectModifiedText() {
        byte[] buffer = new byte[BUFFER_SIZE];
        boolean contentFound = false;
        boolean stylesFound = false;
        ByteArrayOutputStream out = new ByteArrayOutputStream(2 * fileData.length);
        ZipOutputStream zipOutputStream = new ZipOutputStream(out);
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(fileData));
        try {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                zipOutputStream.putNextEntry(new ZipEntry(zipEntry.getName()));
                if (content != null && zipEntry.getName().equals("content.xml")) {
                    contentFound = true;
                    if (log.isTraceEnabled()) {
                        log.trace("Write content.xml to fileData\n" + content);
                    } else if (log.isDebugEnabled()) {
                        log.trace("Write content.xml to fileData, length = " + content.length());
                    }
                    zipOutputStream.write(content.getBytes("UTF-8"));
                } else if (styles != null && zipEntry.getName().equals("styles.xml")) {
                    stylesFound = true;
                    if (log.isTraceEnabled()) {
                        log.trace("Write styles.xml to fileData\n" + styles);
                    } else if (log.isDebugEnabled()) {
                        log.debug("Write styles.xml to fileData, length = " + styles.length());
                    }
                    zipOutputStream.write(styles.getBytes("UTF-8"));
                } else {
                    int read = zipInputStream.read(buffer);
                    while (read > -1) {
                        zipOutputStream.write(buffer, 0, read);
                        read = zipInputStream.read(buffer);
                    }
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        } catch (IOException ex) {
            log.error("Exception while injecting content and styles into odt", ex);
            throw new IllegalArgumentException("fileData is probably not an odt file: " + ex);
        } finally {
            IOUtils.closeQuietly(zipInputStream);
            IOUtils.closeQuietly(zipOutputStream);
        }
        if (content != null && !contentFound) {
            log.error("fileData is not an odt file, no content.xml found, throwing exception.");
            throw new IllegalArgumentException("fileData is not an odt file, no content.xml found");
        }
        if (styles != null && !stylesFound) {
            log.error("fileData is not an odt file, no styles.xml found, throwing exception.");
            throw new IllegalArgumentException("fileData is not an odt file, no styles.xml found");
        }
        byte[] result = out.toByteArray();
        if (log.isDebugEnabled()) {
            log.debug("Injected content. File data changed from " + fileData.length + " bytes to " + result.length + " bytes.");
        }
        return result;
    }
