    public void dispatch(com.sun.star.util.URL aURL, com.sun.star.beans.PropertyValue[] aArguments) {
        if (aURL.Protocol.compareTo("org.openoffice.oosvn.oosvn:") == 0) {
            OoDocProperty docProperty = getProperty();
            settings.setCancelFired(false);
            if (aURL.Path.compareTo("svnUpdate") == 0) {
                try {
                    try {
                        settings = getSerializedSettings(docProperty);
                    } catch (NullPointerException ex) {
                        new DialogSettings(new javax.swing.JFrame(), true, settings).setVisible(true);
                        if (settings.getCancelFired()) return;
                        new DialogFileChooser(new javax.swing.JFrame(), true, settings).setVisible(true);
                        if (settings.getCancelFired()) return;
                    } catch (Exception ex) {
                        error("Error getting settings", ex);
                        return;
                    }
                    Object[][] logs = getLogs(settings);
                    long checkVersion = -1;
                    if (logs.length == 0) {
                        error("Sorry, the specified repository is empty.");
                        return;
                    }
                    new DialogSVNHistory(new javax.swing.JFrame(), true, settings, logs).setVisible(true);
                    if (settings.getCancelFired()) return;
                    File tempDir = new File(settings.getCheckoutPath() + svnWorker.tempDir);
                    if (tempDir.exists()) {
                        if (deleteFileDir(tempDir) == false) {
                            error("Error while deleting temporary checkout dir.");
                        }
                    }
                    svnWorker.checkout(settings);
                    File[] tempFiles = tempDir.listFiles();
                    File anyOdt = null;
                    File thisOdt = null;
                    for (int j = 0; j < tempFiles.length; j++) {
                        if (tempFiles[j].toString().endsWith(".odt")) anyOdt = tempFiles[j];
                        if (tempFiles[j].toString().equals(settings.getCheckoutDoc()) && settings.getCheckoutDoc() != null) thisOdt = tempFiles[j];
                    }
                    if (thisOdt != null) anyOdt = thisOdt;
                    String url;
                    if (settings.getCheckoutDoc() == null || !settings.getCheckoutDoc().equals(anyOdt.getName())) {
                        File newOdt = new File(settings.getCheckoutPath() + "/" + anyOdt.getName());
                        if (newOdt.exists()) newOdt.delete();
                        anyOdt.renameTo(newOdt);
                        File svnInfo = new File(settings.getCheckoutPath() + svnWorker.tempDir + "/.svn");
                        File newSvnInfo = new File(settings.getCheckoutPath() + "/.svn");
                        if (newSvnInfo.exists()) {
                            if (deleteFileDir(newSvnInfo) == false) {
                                error("Error while deleting temporary checkout dir.");
                            }
                        }
                        url = "file:///" + newOdt.getPath().replace("\\", "/");
                        svnInfo.renameTo(newSvnInfo);
                        anyOdt = newOdt;
                        loadDocumentFromUrl(url);
                        settings.setCheckoutDoc(anyOdt.getName());
                        try {
                            settings.serializeOut();
                        } catch (Exception ex) {
                            error("Error occured when re-newing settings.", ex);
                        }
                    } else {
                        try {
                            settings.serializeOut();
                        } catch (Exception ex) {
                            error("Error occured when re-newing settings.", ex);
                        }
                        url = "file:///" + anyOdt.getPath().replace("\\", "/");
                        XDispatchProvider xDispatchProvider = (XDispatchProvider) UnoRuntime.queryInterface(XDispatchProvider.class, m_xFrame);
                        PropertyValue property[] = new PropertyValue[1];
                        property[0] = new PropertyValue();
                        property[0].Name = "URL";
                        property[0].Value = url;
                        XMultiServiceFactory xMSF = createProvider();
                        Object objDispatchHelper = m_xServiceManager.createInstanceWithContext("com.sun.star.frame.DispatchHelper", m_xContext);
                        XDispatchHelper xDispatchHelper = (XDispatchHelper) UnoRuntime.queryInterface(XDispatchHelper.class, objDispatchHelper);
                        xDispatchHelper.executeDispatch(xDispatchProvider, ".uno:CompareDocuments", "", 0, property);
                    }
                } catch (Exception ex) {
                    error(ex);
                }
                return;
            }
            if (aURL.Path.compareTo("svnCommit") == 0) {
                try {
                    try {
                        settings = getSerializedSettings(docProperty);
                    } catch (Exception ex) {
                        error("Error getting settings", ex);
                        return;
                    }
                    Collection logs = svnWorker.getLogs(settings);
                    long headRevision = svnWorker.getHeadRevisionNumber(logs);
                    long committedRevision = -1;
                    new DialogCommitMessage(new javax.swing.JFrame(), true, settings).setVisible(true);
                    if (settings.getCancelFired()) return;
                    try {
                        settings.serializeOut();
                    } catch (Exception ex) {
                        error("Error occured when re-newing settings.", ex);
                    }
                    if (headRevision == 0) {
                        File impDir = new File(settings.getCheckoutPath() + svnWorker.tempDir + "/.import");
                        if (impDir.exists()) if (deleteFileDir(impDir) == false) {
                            error("Error while creating temporary import directory.");
                            return;
                        }
                        if (!impDir.mkdirs()) {
                            error("Error while creating temporary import directory.");
                            return;
                        }
                        File impFile = new File(settings.getCheckoutPath() + svnWorker.tempDir + "/.import/" + settings.getCheckoutDoc());
                        try {
                            FileChannel srcChannel = new FileInputStream(settings.getCheckoutPath() + "/" + settings.getCheckoutDoc()).getChannel();
                            FileChannel dstChannel = new FileOutputStream(impFile).getChannel();
                            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                            srcChannel.close();
                            dstChannel.close();
                        } catch (Exception ex) {
                            error("Error while importing file", ex);
                            return;
                        }
                        final String checkoutPath = settings.getCheckoutPath();
                        try {
                            settings.setCheckoutPath(impDir.toString());
                            committedRevision = svnWorker.importDirectory(settings, false).getNewRevision();
                        } catch (Exception ex) {
                            settings.setCheckoutPath(checkoutPath);
                            error("Error while importing file", ex);
                            return;
                        }
                        settings.setCheckoutPath(checkoutPath);
                        if (impDir.exists()) if (deleteFileDir(impDir) == false) error("Error while creating temporary import directory.");
                        try {
                            File newSvnInfo = new File(settings.getCheckoutPath() + "/.svn");
                            if (newSvnInfo.exists()) {
                                if (deleteFileDir(newSvnInfo) == false) {
                                    error("Error while deleting temporary checkout dir.");
                                }
                            }
                            File tempDir = new File(settings.getCheckoutPath() + svnWorker.tempDir);
                            if (tempDir.exists()) {
                                if (deleteFileDir(tempDir) == false) {
                                    error("Error while deleting temporary checkout dir.");
                                }
                            }
                            svnWorker.checkout(settings);
                            File svnInfo = new File(settings.getCheckoutPath() + svnWorker.tempDir + "/.svn");
                            svnInfo.renameTo(newSvnInfo);
                            if (deleteFileDir(tempDir) == false) {
                                error("Error while managing working copy");
                            }
                            try {
                                settings.serializeOut();
                            } catch (Exception ex) {
                                error("Error occured when re-newing settings.", ex);
                            }
                        } catch (Exception ex) {
                            error("Error while checking out a working copy for the location", ex);
                        }
                        showMessageBox("Import succesful", "Succesfully imported as revision no. " + committedRevision);
                        return;
                    } else {
                        try {
                            committedRevision = svnWorker.commit(settings, false).getNewRevision();
                        } catch (Exception ex) {
                            error("Error while committing changes. If the file is not working copy, you must use 'Checkout / Update' first.", ex);
                        }
                        if (committedRevision == -1) {
                            showMessageBox("Update - no changes", "No changes was made. Maybe you must just save the changes.");
                        } else {
                            showMessageBox("Commit succesfull", "Commited as revision no. " + committedRevision);
                        }
                    }
                } catch (Exception ex) {
                    error(ex);
                }
                return;
            }
            if (aURL.Path.compareTo("svnHistory") == 0) {
                try {
                    try {
                        settings = getSerializedSettings(docProperty);
                    } catch (Exception ex) {
                        error("Error getting settings", ex);
                        return;
                    }
                    Object[][] logs = getLogs(settings);
                    long checkVersion = settings.getCheckoutVersion();
                    settings.setCheckoutVersion(-99);
                    new DialogSVNHistory(new javax.swing.JFrame(), true, settings, logs).setVisible(true);
                    settings.setCheckoutVersion(checkVersion);
                } catch (Exception ex) {
                    error(ex);
                }
                return;
            }
            if (aURL.Path.compareTo("settings") == 0) {
                try {
                    settings = getSerializedSettings(docProperty);
                } catch (NoSerializedSettingsException ex) {
                    try {
                        settings.setCheckout(docProperty.getDocURL());
                    } catch (Exception exx) {
                    }
                } catch (Exception ex) {
                    error("Error getting settings; maybe you" + " need to save your document." + " If this is your first" + " checkout of the document, use Checkout" + " function directly.");
                    return;
                }
                new DialogSettings(new javax.swing.JFrame(), true, settings).setVisible(true);
                try {
                    settings.serializeOut();
                } catch (Exception ex) {
                    error("Error occured when saving settings.", ex);
                }
                return;
            }
            if (aURL.Path.compareTo("about") == 0) {
                showMessageBox("OoSvn :: About", "Autor: �t�p�n Cenek (stepan@geek.cz)");
                return;
            }
        }
    }
