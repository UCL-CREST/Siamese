    private boolean saveLOBDataToFileSystem() {
        if ("".equals(m_attachmentPathRoot)) {
            log.severe("no attachmentPath defined");
            return false;
        }
        if (m_items == null || m_items.size() == 0) {
            setBinaryData(null);
            return true;
        }
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.newDocument();
            final Element root = document.createElement("attachments");
            document.appendChild(root);
            document.setXmlStandalone(true);
            for (int i = 0; i < m_items.size(); i++) {
                log.fine(m_items.get(i).toString());
                File entryFile = m_items.get(i).getFile();
                final String path = entryFile.getAbsolutePath();
                log.fine(path + " - " + m_attachmentPathRoot);
                if (!path.startsWith(m_attachmentPathRoot)) {
                    log.fine("move file: " + path);
                    FileChannel in = null;
                    FileChannel out = null;
                    try {
                        final File destFolder = new File(m_attachmentPathRoot + File.separator + getAttachmentPathSnippet());
                        if (!destFolder.exists()) {
                            if (!destFolder.mkdirs()) {
                                log.warning("unable to create folder: " + destFolder.getPath());
                            }
                        }
                        final File destFile = new File(m_attachmentPathRoot + File.separator + getAttachmentPathSnippet() + File.separator + entryFile.getName());
                        in = new FileInputStream(entryFile).getChannel();
                        out = new FileOutputStream(destFile).getChannel();
                        in.transferTo(0, in.size(), out);
                        in.close();
                        out.close();
                        if (entryFile.exists()) {
                            if (!entryFile.delete()) {
                                entryFile.deleteOnExit();
                            }
                        }
                        entryFile = destFile;
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.severe("unable to copy file " + entryFile.getAbsolutePath() + " to " + m_attachmentPathRoot + File.separator + getAttachmentPathSnippet() + File.separator + entryFile.getName());
                    } finally {
                        if (in != null && in.isOpen()) {
                            in.close();
                        }
                        if (out != null && out.isOpen()) {
                            out.close();
                        }
                    }
                }
                final Element entry = document.createElement("entry");
                entry.setAttribute("name", getEntryName(i));
                String filePathToStore = entryFile.getAbsolutePath();
                filePathToStore = filePathToStore.replaceFirst(m_attachmentPathRoot.replaceAll("\\\\", "\\\\\\\\"), ATTACHMENT_FOLDER_PLACEHOLDER);
                log.fine(filePathToStore);
                entry.setAttribute("file", filePathToStore);
                root.appendChild(entry);
            }
            final Source source = new DOMSource(document);
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final Result result = new StreamResult(bos);
            final Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
            final byte[] xmlData = bos.toByteArray();
            log.fine(bos.toString());
            setBinaryData(xmlData);
            return true;
        } catch (Exception e) {
            log.log(Level.SEVERE, "saveLOBData", e);
        }
        setBinaryData(null);
        return false;
    }
