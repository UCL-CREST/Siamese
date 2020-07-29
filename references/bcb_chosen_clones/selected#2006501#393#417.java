            @Override
            public void mousePressed(MouseEvent e) {
                if (!e.isPopupTrigger()) return;
                Icon icon = findIconAt(e.getX(), e.getY());
                if (icon == null) return;
                JPopupMenu pop = new JPopupMenu();
                AbstractAction action = new ObjectAction<Icon>(icon, "Open URL ") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            Desktop.getDesktop().browse(getObject().getNode().getURL().toURI());
                        } catch (Exception e1) {
                            ThrowablePane.show(drawingArea, e1);
                        }
                    }
                };
                if (icon == null || !Desktop.isDesktopSupported()) {
                    action.setEnabled(false);
                }
                pop.add(action);
                pop.show(e.getComponent(), e.getX(), e.getY());
            }
