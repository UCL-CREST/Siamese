    private final void createActionMap() {
        m_actionMap.clear();
        m_actionMap.put(EdaActionKey.NEW_PCB_DOC, new EdaGlobalAction(this, EdaActionKey.NEW_PCB_DOC) {

            public void actionPerformed(ActionEvent e) {
                getEda().addDocument(new EdaPcb(new EdaPcbPane(getEda())));
            }
        });
        m_actionMap.put(EdaActionKey.NEW_SCHEMATIC_DOC, new EdaGlobalAction(this, EdaActionKey.NEW_SCHEMATIC_DOC) {

            public void actionPerformed(ActionEvent e) {
                getEda().addDocument(new EdaSchematic(new EdaSchematicPane(getEda())));
            }
        });
        m_actionMap.put(EdaActionKey.NEW_DOCUMENT, new EdaGlobalAction(this, EdaActionKey.NEW_DOCUMENT) {

            public void actionPerformed(ActionEvent e) {
                EdaNewDocumentDialog.showDialog(getEda());
            }
        });
        m_actionMap.put(EdaActionKey.IMPORT_GEDA_FILE, new EdaGlobalAction(this, EdaActionKey.IMPORT_GEDA_FILE) {

            private File chooseGedaFile() {
                fileDialog.setDialogTitle("Choose a symbol file");
                if (fileDialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    return fileDialog.getSelectedFile();
                }
                return null;
            }

            public void actionPerformed(ActionEvent e) {
                final File gedaFile = chooseGedaFile();
                if (gedaFile == null) {
                    return;
                }
                final EdaGedaFileParser parser = new EdaGedaFileParser(new EdaDefaultSaveableObjectFactory(), false);
                try {
                    final EdaDrawing drawing = parser.parseGedaSymbolFile(gedaFile);
                    final EdaDrawingPane pane = new EdaSchematicPane(getEda(), drawing);
                    final EdaSchematic document = new EdaSchematic(pane);
                    document.setName(gedaFile.getName());
                    getEda().addDocument(document);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        m_actionMap.put(EdaActionKey.NEW_PROJECT, new EdaGlobalAction(this, EdaActionKey.NEW_PROJECT) {

            public void actionPerformed(ActionEvent e) {
                EdaProject ep = new EdaProject(getEda());
                ep.setName("Untitled");
                getEda().getProjectTree().addProject(ep);
            }
        });
        m_actionMap.put(EdaActionKey.CLOSE_PROJECT, new EdaGlobalAction(this, EdaActionKey.CLOSE_PROJECT) {

            public void actionPerformed(ActionEvent e) {
                getEda().removeProject();
            }
        });
        m_actionMap.put(EdaActionKey.OPEN_PROJECT, new EdaGlobalAction(this, EdaActionKey.OPEN_PROJECT) {

            public void actionPerformed(ActionEvent e) {
                getEda().openProject();
            }
        });
        m_actionMap.put(EdaActionKey.SAVE_PROJECT, new EdaGlobalAction(this, EdaActionKey.SAVE_PROJECT) {

            public void actionPerformed(ActionEvent e) {
                getEda().saveProject();
            }
        });
        m_actionMap.put(EdaActionKey.NEXT_WINDOW, new EdaGlobalAction(this, EdaActionKey.NEXT_WINDOW) {

            public void actionPerformed(ActionEvent e) {
                getEda().nextWindow();
            }
        });
        m_actionMap.put(EdaActionKey.RENAME_NODE, new EdaGlobalAction(this, EdaActionKey.RENAME_NODE) {

            public void actionPerformed(ActionEvent e) {
                getEda().renameNode();
            }
        });
        m_actionMap.put(EdaActionKey.CLOSE_DOCUMENT, new EdaGlobalAction(this, EdaActionKey.CLOSE_DOCUMENT) {

            public void actionPerformed(ActionEvent e) {
                getEda().closeDocument();
            }
        });
        m_actionMap.put(EdaActionKey.CUT_TREE_NODE, new EdaGlobalAction(this, EdaActionKey.CUT_TREE_NODE) {

            public void actionPerformed(ActionEvent e) {
                getEda().getProjectTree().cut();
            }
        });
        m_actionMap.put(EdaActionKey.COPY_TREE_NODE, new EdaGlobalAction(this, EdaActionKey.COPY_TREE_NODE) {

            public void actionPerformed(ActionEvent e) {
                getEda().getProjectTree().copy();
            }
        });
        m_actionMap.put(EdaActionKey.MOVE_NODE_UP, new EdaGlobalAction(this, EdaActionKey.MOVE_NODE_UP) {

            public void actionPerformed(ActionEvent e) {
                getEda().getProjectTree().moveUp();
            }
        });
        m_actionMap.put(EdaActionKey.MOVE_NODE_DOWN, new EdaGlobalAction(this, EdaActionKey.MOVE_NODE_DOWN) {

            public void actionPerformed(ActionEvent e) {
                getEda().getProjectTree().moveDown();
            }
        });
        m_actionMap.put(EdaActionKey.MOVE_NODE_RIGHT, new EdaGlobalAction(this, EdaActionKey.MOVE_NODE_RIGHT) {

            public void actionPerformed(ActionEvent e) {
                getEda().getProjectTree().moveRight();
            }
        });
        m_actionMap.put(EdaActionKey.MOVE_NODE_LEFT, new EdaGlobalAction(this, EdaActionKey.MOVE_NODE_LEFT) {

            public void actionPerformed(ActionEvent e) {
                getEda().getProjectTree().moveLeft();
            }
        });
        m_actionMap.put(EdaActionKey.PASTE_TREE_NODE, new EdaGlobalAction(this, EdaActionKey.PASTE_TREE_NODE) {

            public void actionPerformed(ActionEvent e) {
                getEda().getProjectTree().paste();
            }
        });
        m_actionMap.put(EdaActionKey.SHOW_DOCUMENT, new EdaGlobalAction(this, EdaActionKey.SHOW_DOCUMENT) {

            public void actionPerformed(ActionEvent e) {
                getEda().showDocument();
            }
        });
        m_actionMap.put(EdaActionKey.NEW_FOLDER, new EdaGlobalAction(this, EdaActionKey.NEW_FOLDER) {

            public void actionPerformed(ActionEvent e) {
                getEda().newFolder();
            }
        });
        m_actionMap.put(EdaActionKey.ADD_LIBRARY, new EdaGlobalAction(this, EdaActionKey.ADD_LIBRARY) {

            public void actionPerformed(ActionEvent e) {
                getEda().addLibrary();
            }
        });
        m_actionMap.put(EdaActionKey.REMOVE_NODE, new EdaGlobalAction(this, EdaActionKey.REMOVE_NODE) {

            public void actionPerformed(ActionEvent e) {
                getEda().removeNode();
            }
        });
        m_actionMap.put(EdaActionKey.SETTINGS, new EdaGlobalAction(this, EdaActionKey.SETTINGS) {

            public void actionPerformed(ActionEvent e) {
                getEda().showSettings();
            }
        });
        m_actionMap.put(EdaActionKey.PRINT, new EdaGlobalAction(this, EdaActionKey.PRINT) {

            public void actionPerformed(ActionEvent e) {
                getEda().print();
            }
        });
        m_actionMap.put(EdaActionKey.EXIT, new EdaGlobalAction(this, EdaActionKey.EXIT) {

            public void actionPerformed(ActionEvent e) {
                getEda().close();
            }
        });
        m_actionMap.put(EdaActionKey.ABOUT, new EdaGlobalAction(this, EdaActionKey.ABOUT) {

            public void actionPerformed(ActionEvent e) {
                new EdaSplashScreen(getEda());
            }
        });
        m_actionMap.put(EdaActionKey.WEB_SITE, new EdaGlobalAction(this, EdaActionKey.WEB_SITE) {

            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("http://www.circuitsmith.com"));
                    } catch (Exception ioe) {
                    }
                }
            }
        });
        m_actionMap.put(EdaActionKey.DEBUG, new EdaGlobalAction(this, EdaActionKey.DEBUG) {

            public void actionPerformed(ActionEvent e) {
                EdaProject p = getEda().getProjectTree().getSelectedProject();
                EdaTreeNode n = getEda().getProjectTree().getSelectedNode();
                System.out.println("Eda.createActionMap working on node " + n.getName());
                for (Iterator<EdaTreeNode> i = n.iterator(); i.hasNext(); ) {
                    EdaTreeNode tn = i.next();
                    if (tn instanceof EdaSchematic) {
                        EdaSchematic s = (EdaSchematic) tn;
                        EdaAttributeList al = s.getDrawing().getAttributeList();
                        EdaAttribute f = al.get("footprint");
                        if (f != null) {
                            String fs[] = f.getValue().split("//");
                            if (fs.length > 1) {
                                EdaDocAttribute fl = new EdaDocAttribute("footprint-lib", fs[0]);
                                f.setValue(fs[1]);
                                fl.linkDocument(p);
                                al.add(fl);
                                System.out.println("Eda.createActionMap debug doing footprints for " + tn.getName());
                            }
                        }
                    }
                }
            }
        });
        m_actionMap.put(EdaActionKey.RELOAD_LIBRARY, new EdaGlobalAction(this, EdaActionKey.RELOAD_LIBRARY) {

            public void actionPerformed(ActionEvent e) {
                getEda().reloadLibrary();
            }
        });
        m_actionMap.put(EdaActionKey.CALC_NETS, new EdaGlobalAction(this, EdaActionKey.CALC_NETS) {

            public void actionPerformed(ActionEvent e) {
                getEda().calcNets();
            }
        });
    }
