    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (qName.equals(ArchiveNormaliser.ARCHIVE_PREFIX + ":" + ArchiveNormaliser.ENTRY_TAG)) {
            entryCounter++;
            String xenaFilename = atts.getValue(ArchiveNormaliser.ARCHIVE_PREFIX + ":" + ArchiveNormaliser.ENTRY_OUTPUT_FILENAME);
            if (xenaFilename == null) {
                xenaFilename = atts.getValue(ArchiveNormaliser.ENTRY_OUTPUT_FILENAME);
            }
            if (xenaFilename == null) {
                throw new SAXException("Archive entry has a null xena filename.");
            }
            File entryFile = new File(sourceDirectory, xenaFilename);
            if (entryFile.exists() && entryFile.isFile()) {
                File entryExportFile = null;
                try {
                    ExportResult exportResult = normaliserManager.export(new XenaInputSource(entryFile), outputDirectory, true);
                    entryExportFile = exportResult.getOutputFile();
                    String originalPath = atts.getValue(ArchiveNormaliser.ARCHIVE_PREFIX + ":" + ArchiveNormaliser.ENTRY_ORIGINAL_PATH_ATTRIBUTE);
                    if (originalPath == null) {
                        originalPath = atts.getValue(ArchiveNormaliser.ENTRY_ORIGINAL_PATH_ATTRIBUTE);
                    }
                    if (originalPath == null) {
                        throw new SAXException("Archive entry has a null original path.");
                    }
                    int extensionIndex = entryExportFile.getName().lastIndexOf(".");
                    String entryExportFileExtension = extensionIndex >= 0 ? entryExportFile.getName().substring(extensionIndex + 1) : "";
                    String entryName = normaliserManager.adjustOutputFileExtension(originalPath, entryExportFileExtension);
                    ZipEntry currentEntry = new ZipEntry(entryName);
                    String originalDate = atts.getValue(ArchiveNormaliser.ARCHIVE_PREFIX + ":" + ArchiveNormaliser.ENTRY_ORIGINAL_FILE_DATE_ATTRIBUTE);
                    if (originalDate == null) {
                        originalDate = atts.getValue(ArchiveNormaliser.ENTRY_ORIGINAL_FILE_DATE_ATTRIBUTE);
                    }
                    if (originalDate == null) {
                        throw new SAXException("Archive entry has a null original date.");
                    }
                    SimpleDateFormat formatter = new SimpleDateFormat(ArchiveNormaliser.DATE_FORMAT_STRING);
                    currentEntry.setTime(formatter.parse(originalDate).getTime());
                    zipOS.putNextEntry(currentEntry);
                    FileInputStream entryExportIS = new FileInputStream(entryExportFile);
                    byte[] buffer = new byte[10 * 1024];
                    int bytesRead = entryExportIS.read(buffer);
                    while (bytesRead > 0) {
                        zipOS.write(buffer, 0, bytesRead);
                        bytesRead = entryExportIS.read(buffer);
                    }
                    entryExportIS.close();
                    zipOS.closeEntry();
                } catch (Exception ex) {
                    throw new SAXException("Problem exporting archive entry " + atts.getValue(ArchiveNormaliser.ARCHIVE_PREFIX + ":" + ArchiveNormaliser.ENTRY_ORIGINAL_PATH_ATTRIBUTE), ex);
                } finally {
                    if (entryExportFile != null) {
                        entryExportFile.delete();
                    }
                }
            }
        }
    }
