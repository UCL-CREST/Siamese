        public void mousePressed(MouseEvent e) {
            if (e.getComponent() == tree) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
                    if (tp != null) {
                        tree.setSelectionPath(tp);
                        selectedFolderNode = (PackerFolderTreeNode) tp.getLastPathComponent();
                        selectedItem = null;
                        folderPopUp.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            } else if (e.getComponent() == content) {
                int index = content.locationToIndex(e.getPoint());
                if (index != -1 && content.getCellBounds(index, index).contains(e.getPoint())) {
                    content.setSelectedIndex(index);
                    selectedItem = (PackerItem) content.getModel().getElementAt(index);
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        if (selectedItem instanceof PackerFolder) {
                            folderPopUp.show(e.getComponent(), e.getX(), e.getY());
                        } else if (selectedItem instanceof PackerFile) {
                            filePopUp.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                } else {
                    if (selectedFolderNode != null) {
                        selectedItem = null;
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            folderPopUp.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                }
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    index = content.locationToIndex(e.getPoint());
                    if (index != -1 && content.getCellBounds(index, index).contains(e.getPoint())) {
                        selectedItem = (PackerItem) content.getModel().getElementAt(index);
                        if (selectedItem instanceof PackerFolder) {
                            selectedFolderNode = selectedFolderNode.getChildNode((PackerFolder) selectedItem);
                            tree.setSelectionPath(selectedFolderNode.getPath());
                        } else if (selectedItem instanceof PackerFile && Desktop.isDesktopSupported()) {
                            PackerFile file = (PackerFile) selectedItem;
                            try {
                                File extracted = file.extract(getTempFolder());
                                extracted = extracted.getCanonicalFile();
                                extracted.deleteOnExit();
                                Desktop desktop = Desktop.getDesktop();
                                desktop.open(extracted);
                            } catch (IOException e1) {
                                LOG.log(Level.INFO, "Problem with opening file. Maybe no association created for this file.", e1);
                            } catch (ValidationException e1) {
                                ResourceBundle resource = ResourceBundle.getBundle("cz.zcu.fav.hofhans.resources.view.Dialogs", panel.getLocale());
                                JOptionPane.showMessageDialog(panel, e1.getLocalizedMessage(), resource.getString("validationError"), JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        }
