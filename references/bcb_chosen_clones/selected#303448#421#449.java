            @Override
            public void mousePressed(MouseEvent e) {
                Figure f = getFigureAt(e.getX(), e.getY());
                if (f == null) return;
                if (!(e.isPopupTrigger() || e.isControlDown())) return;
                JPopupMenu popup = new JPopupMenu();
                JMenuItem menu = new JMenuItem(new ObjectAction<Page>(f.page, "Open " + f.page) {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String uri = Wikipedia.BASE + "/wiki/" + getObject().getQNameEncoded();
                        try {
                            if (RevisionVisualization.this.appletContext == null) {
                                Desktop d = Desktop.getDesktop();
                                d.browse(new URI(uri));
                            } else {
                                RevisionVisualization.this.appletContext.getAppletContext().showDocument(new URL(uri), "_" + System.currentTimeMillis());
                            }
                        } catch (Exception err) {
                            ThrowablePane.show(RevisionVisualization.this, err);
                        }
                    }
                });
                menu.setEnabled(RevisionVisualization.this.appletContext == null && Desktop.isDesktopSupported());
                popup.add(menu);
                popup.show(drawingArea, e.getX(), e.getY());
            }
