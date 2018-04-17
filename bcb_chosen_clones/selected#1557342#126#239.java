    private boolean saveDocumentXml(String repository, String tempRepo) {
        boolean result = true;
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            String expression = "documents/document";
            InputSource insource = new InputSource(new FileInputStream(tempRepo + File.separator + AppConstants.DMS_XML));
            NodeList nodeList = (NodeList) xpath.evaluate(expression, insource, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                System.out.println(node.getNodeName());
                DocumentModel document = new DocumentModel();
                NodeList childs = node.getChildNodes();
                for (int j = 0; j < childs.getLength(); j++) {
                    Node child = childs.item(j);
                    if (child.getNodeType() == Node.ELEMENT_NODE) {
                        if (child.getNodeName() != null && child.getFirstChild() != null && child.getFirstChild().getNodeValue() != null) {
                            System.out.println(child.getNodeName() + "::" + child.getFirstChild().getNodeValue());
                        }
                        if (Document.FLD_ID.equals(child.getNodeName())) {
                            if (child.getFirstChild() != null) {
                                String szId = child.getFirstChild().getNodeValue();
                                if (szId != null && szId.length() > 0) {
                                    try {
                                        document.setId(new Long(szId));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else if (document.FLD_NAME.equals(child.getNodeName())) {
                            document.setName(child.getFirstChild().getNodeValue());
                            document.setTitle(document.getName());
                            document.setDescr(document.getName());
                            document.setExt(getExtension(document.getName()));
                        } else if (document.FLD_LOCATION.equals(child.getNodeName())) {
                            document.setLocation(child.getFirstChild().getNodeValue());
                        } else if (document.FLD_OWNER.equals(child.getNodeName())) {
                            Long id = new Long(child.getFirstChild().getNodeValue());
                            User user = new UserModel();
                            user.setId(id);
                            user = (User) userService.find(user);
                            if (user != null && user.getId() != null) {
                                document.setOwner(user);
                            }
                        }
                    }
                }
                boolean isSave = docService.save(document);
                if (isSave) {
                    String repo = preference.getRepository();
                    Calendar calendar = Calendar.getInstance();
                    StringBuffer sbRepo = new StringBuffer(repo);
                    sbRepo.append(File.separator);
                    StringBuffer sbFolder = new StringBuffer(sdf.format(calendar.getTime()));
                    sbFolder.append(File.separator).append(calendar.get(Calendar.HOUR_OF_DAY));
                    File fileFolder = new File(sbRepo.append(sbFolder).toString());
                    if (!fileFolder.exists()) {
                        fileFolder.mkdirs();
                    }
                    FileChannel fcSource = null, fcDest = null;
                    try {
                        StringBuffer sbFile = new StringBuffer(fileFolder.getAbsolutePath());
                        StringBuffer fname = new StringBuffer(document.getId().toString());
                        fname.append(".").append(document.getExt());
                        sbFile.append(File.separator).append(fname);
                        fcSource = new FileInputStream(tempRepo + File.separator + document.getName()).getChannel();
                        fcDest = new FileOutputStream(sbFile.toString()).getChannel();
                        fcDest.transferFrom(fcSource, 0, fcSource.size());
                        document.setLocation(sbFolder.toString());
                        document.setSize(fcSource.size());
                        log.info("Batch upload file " + document.getName() + " into [" + document.getLocation() + "] as " + document.getName() + "." + document.getExt());
                        folder.setId(DEFAULT_FOLDER);
                        folder = (Folder) folderService.find(folder);
                        if (folder != null && folder.getId() != null) {
                            document.setFolder(folder);
                        }
                        workspace.setId(DEFAULT_WORKSPACE);
                        workspace = (Workspace) workspaceService.find(workspace);
                        if (workspace != null && workspace.getId() != null) {
                            document.setWorkspace(workspace);
                        }
                        user.setId(DEFAULT_USER);
                        user = (User) userService.find(user);
                        if (user != null && user.getId() != null) {
                            document.setCrtby(user.getId());
                        }
                        document.setCrtdate(new Date());
                        document = (DocumentModel) docService.resetDuplicateDocName(document);
                        docService.save(document);
                        DocumentIndexer.indexDocument(preference, document);
                    } catch (FileNotFoundException notFoundEx) {
                        log.error("saveFile file not found: " + document.getName(), notFoundEx);
                    } catch (IOException ioEx) {
                        log.error("saveFile IOException: " + document.getName(), ioEx);
                    } finally {
                        try {
                            if (fcSource != null) {
                                fcSource.close();
                            }
                            if (fcDest != null) {
                                fcDest.close();
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }
