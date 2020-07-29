    public void processEPubFile(File outputFile) throws IOException {
        if (book.getEpubFile() == null) return;
        File inputFile = book.getEpubFile().getFile();
        ZipFile zipInputFile = null;
        ZipOutputStream zos = null;
        try {
            try {
                Map<String, ZipEntry> cssFilesBackupMap = new HashMap<String, ZipEntry>();
                zipInputFile = new ZipFile(inputFile);
                outputFile.getParentFile().mkdirs();
                zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)));
                Enumeration entries = zipInputFile.entries();
                while (entries.hasMoreElements()) {
                    Object o = entries.nextElement();
                    if (o instanceof ZipEntry) {
                        ZipEntry zipEntry = (ZipEntry) o;
                        InputStream inputStream = zipInputFile.getInputStream(zipEntry);
                        if (zipEntry.getName().toUpperCase().endsWith("CONTENT.OPF")) {
                            try {
                                Document doc = JDOM.INSTANCE.getSaxBuilder().build(inputStream);
                                try {
                                    doc.getRootElement().addNamespaceDeclaration(Namespace.Opf.getJdomNamespace());
                                } catch (org.jdom.IllegalAddException e) {
                                    logger.warn("processEbubFile: Unable to add namespace declaration '" + Namespace.Opf + "' for book: " + book.getTitle() + " (file " + inputFile + ")");
                                }
                                try {
                                    doc.getRootElement().addNamespaceDeclaration(Namespace.Dc.getJdomNamespace());
                                } catch (org.jdom.IllegalAddException e) {
                                    logger.warn("processEbubFile: Unable to add namespace declaration '" + Namespace.Dc + "' for book: " + book.getTitle() + " (file " + inputFile + ")");
                                }
                                try {
                                    doc.getRootElement().addNamespaceDeclaration(Namespace.DcTerms.getJdomNamespace());
                                } catch (org.jdom.IllegalAddException e) {
                                    logger.warn("processEbubFile: Unable to add namespace declaration '" + Namespace.DcTerms + "' for book: " + book.getTitle() + " (file " + inputFile + ")");
                                }
                                try {
                                    doc.getRootElement().addNamespaceDeclaration(Namespace.Calibre.getJdomNamespace());
                                } catch (org.jdom.IllegalAddException e) {
                                    logger.warn("processEbubFile: Unable to add namespace declaration '" + Namespace.Calibre + "' for book: " + book.getTitle() + " (file " + inputFile + ")");
                                }
                                Element metadata = doc.getRootElement().getChild("metadata", Namespace.Opf.getJdomNamespace());
                                if (metadata != null) processMetadataElement(metadata);
                                try {
                                    ZipEntry newEntry = new ZipEntry(zipEntry.getName());
                                    zos.putNextEntry(newEntry);
                                    JDOM.INSTANCE.getOutputter().output(doc, zos);
                                } finally {
                                    zos.closeEntry();
                                }
                            } catch (IOException io) {
                                logger.error(io);
                                logger.error("... for book: " + book.getTitle() + " (file " + inputFile + ")");
                            }
                        } else {
                            BufferedInputStream in = null;
                            try {
                                String filename = zipEntry.getName();
                                if (isRestoreCss()) {
                                    if (filename.toUpperCase().endsWith(".CSS_BAK")) {
                                        filename = filename.substring(0, filename.length() - 4);
                                    } else if (filename.toUpperCase().endsWith(".CSS")) {
                                        if (zipInputFile.getEntry(filename + "_BAK") != null) filename = null;
                                    }
                                } else if (isRemoveCss()) {
                                    if (filename.toUpperCase().endsWith(".CSS_BAK")) {
                                        filename = null;
                                    } else if (filename.toUpperCase().endsWith(".CSS")) {
                                        if (getDefaultCss() != null) {
                                            try {
                                                BufferedInputStream in2 = null;
                                                try {
                                                    ZipEntry newEntry = new ZipEntry(filename);
                                                    newEntry.setMethod(ZipEntry.DEFLATED);
                                                    zos.putNextEntry(newEntry);
                                                    byte[] data = new byte[1024];
                                                    in2 = new BufferedInputStream(new FileInputStream(getDefaultCss()), 1024);
                                                    int count;
                                                    while ((count = in2.read(data, 0, data.length)) != -1) {
                                                        zos.write(data, 0, count);
                                                    }
                                                } finally {
                                                    zos.closeEntry();
                                                    if (in2 != null) in2.close();
                                                }
                                            } catch (IOException e) {
                                                logger.error(e);
                                                logger.error("... for book: " + book.getTitle() + " (cannot copy the default stylesheet)");
                                            }
                                        }
                                        filename += "_BAK";
                                        if (zipInputFile.getEntry(filename) != null) filename = null;
                                    }
                                }
                                if (filename != null) {
                                    try {
                                        ZipEntry newEntry = new ZipEntry(filename);
                                        newEntry.setMethod(zipEntry.getMethod());
                                        if (newEntry.getMethod() == ZipEntry.STORED) {
                                            newEntry.setSize(zipEntry.getSize());
                                            newEntry.setCrc(zipEntry.getCrc());
                                        }
                                        zos.putNextEntry(newEntry);
                                        byte[] data = new byte[1024];
                                        in = new BufferedInputStream(inputStream, 1024);
                                        int count;
                                        while ((count = in.read(data, 0, data.length)) != -1) {
                                            zos.write(data, 0, count);
                                        }
                                    } finally {
                                        zos.closeEntry();
                                        if (in != null) in.close();
                                    }
                                }
                            } catch (IOException e) {
                                logger.error(e);
                                logger.error("... for book: " + book.getTitle() + " (file " + inputFile + ")");
                            }
                        }
                    }
                }
            } finally {
                if (zos != null) {
                    zos.close();
                }
                if (zipInputFile != null) {
                    zipInputFile.close();
                }
            }
        } catch (JDOMException je) {
            logger.warn("ProcessePubFile: Unexpected JDOMException for book: " + book.getTitle() + " (file " + inputFile + ")");
            logger.warn(je);
            throw new IOException(je);
        } catch (Exception e) {
            logger.warn("ProcessePubFile: Unexpected Exception for book: " + book.getTitle() + " (file " + inputFile + ")");
            logger.warn(e);
            throw new IOException(e);
        }
    }
