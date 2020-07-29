    public void init() throws GateException {
        if (reportFile == null) throw new GateException("No report file set!");
        boolean restarting = false;
        if (!reportFile.getParentFile().exists() && !reportFile.getParentFile().mkdirs()) {
            throw new GateException("Could not create directories for " + reportFile.getAbsolutePath());
        }
        File backupFile = new File(reportFile.getAbsolutePath() + ".bak");
        if (reportFile.exists()) {
            restarting = true;
            logger.info("Existing report file found at \"" + reportFile.getAbsolutePath() + "\", attempting to restart");
            if (!reportFile.renameTo(backupFile)) {
                try {
                    byte[] buff = new byte[32 * 1024];
                    InputStream in = new BufferedInputStream(new FileInputStream(reportFile));
                    try {
                        OutputStream out = new BufferedOutputStream(new FileOutputStream(backupFile));
                        try {
                            int read = in.read(buff);
                            while (read != -1) {
                                out.write(buff, 0, read);
                                read = in.read(buff);
                            }
                        } finally {
                            out.close();
                        }
                    } finally {
                        in.close();
                    }
                } catch (IOException e) {
                    throw new GateException("Could not restart batch", e);
                }
            }
        }
        try {
            reportWriter = staxOutputFactory.createXMLStreamWriter(new BufferedOutputStream(new FileOutputStream(reportFile)));
            reportWriter.writeStartDocument();
            reportWriter.writeCharacters("\n");
            reportWriter.setDefaultNamespace(Tools.REPORT_NAMESPACE);
            reportWriter.writeStartElement(Tools.REPORT_NAMESPACE, "cloudReport");
            reportWriter.writeDefaultNamespace(Tools.REPORT_NAMESPACE);
            reportWriter.writeCharacters("\n");
            reportWriter.writeStartElement(Tools.REPORT_NAMESPACE, "documents");
        } catch (XMLStreamException e) {
            throw new GateException("Cannot write to the report file!", e);
        } catch (IOException e) {
            throw new GateException("Cannot write to the report file!", e);
        }
        if (restarting) {
            try {
                Set<String> completedDocuments = new HashSet<String>();
                logger.debug("Processing existing report file");
                InputStream bakIn = new BufferedInputStream(new FileInputStream(backupFile));
                XMLEventReader xer = staxInputFactory.createXMLEventReader(bakIn);
                try {
                    XMLEvent event;
                    while (xer.hasNext()) {
                        event = xer.nextEvent();
                        if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("documents")) {
                            break;
                        }
                    }
                    List<XMLEvent> events = new LinkedList<XMLEvent>();
                    String currentReturnCode = null;
                    String currentDocid = null;
                    while (xer.hasNext()) {
                        event = xer.nextEvent();
                        events.add(event);
                        if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("processResult")) {
                            currentReturnCode = event.asStartElement().getAttributeByName(new QName(XMLConstants.NULL_NS_URI, "returnCode")).getValue();
                            currentDocid = event.asStartElement().getAttributeByName(new QName(XMLConstants.NULL_NS_URI, "id")).getValue();
                        }
                        if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("processResult")) {
                            if (currentReturnCode.equals("SUCCESS") && currentDocid != null) {
                                completedDocuments.add(currentDocid);
                                for (XMLEvent evt : events) {
                                    Tools.writeStaxEvent(evt, reportWriter);
                                }
                            }
                            events.clear();
                            currentReturnCode = null;
                            currentDocid = null;
                        }
                        if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("documents")) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    logger.debug("Exception while parsing old report file - probably " + "reached the end of old report", e);
                } finally {
                    xer.close();
                    bakIn.close();
                    backupFile.delete();
                }
                List<String> unprocessedDocs = new ArrayList<String>();
                unprocessedDocs.addAll(Arrays.asList(documentIDs));
                unprocessedDocs.removeAll(completedDocuments);
                unprocessedDocumentIDs = unprocessedDocs.toArray(new String[unprocessedDocs.size()]);
            } catch (XMLStreamException e) {
                throw new GateException("Cannot write to the report file!", e);
            } catch (IOException e) {
                throw new GateException("Cannot write to the report file!", e);
            }
        } else {
            unprocessedDocumentIDs = documentIDs;
        }
    }
