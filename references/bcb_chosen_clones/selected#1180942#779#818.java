    private void backupRepository(OpProjectSession session, File zipFile) throws IOException {
        long startTime = System.currentTimeMillis();
        logger.info("Starting repository backup....");
        OpBroker broker = session.newBroker();
        try {
            FileOutputStream fileOut = new FileOutputStream(zipFile);
            ZipOutputStream zipOut = new ZipOutputStream(fileOut);
            zipOut.putNextEntry(new ZipEntry(DATA_XML));
            XDocumentWriter writer = new XDocumentWriter(zipOut);
            List<Triple<String, OpBackupMember, String>> toExport = new LinkedList<Triple<String, OpBackupMember, String>>();
            writeXMLFile(session, broker, writer, toExport);
            writer.flush();
            zipOut.closeEntry();
            for (Triple<String, OpBackupMember, String> entry : toExport) {
                OpSiteObjectIfc object = broker.getObject(entry.getFirst());
                OpBackupMember member = entry.getSecond();
                String path = entry.getThird();
                Object value = OpBackupLoader.getValueFromObject(object, member);
                zipOut.putNextEntry(new ZipEntry(path));
                if (value instanceof XSizeInputStream) {
                    XSizeInputStream in = (XSizeInputStream) value;
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        zipOut.write(buf, 0, len);
                    }
                    in.close();
                } else {
                    zipOut.write((byte[]) value);
                }
                zipOut.closeEntry();
            }
            zipOut.flush();
            zipOut.close();
            long elapsedTimeSecs = (System.currentTimeMillis() - startTime) / 1000;
            logger.info("Repository backup completed in " + elapsedTimeSecs + " seconds");
        } finally {
            broker.closeAndEvict();
        }
    }
