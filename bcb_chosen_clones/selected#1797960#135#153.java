        @Override
        public void mousePressed(MouseEvent e) {
            if (!Desktop.isDesktopSupported() || this.item.link == null || !e.isPopupTrigger()) return;
            JPopupMenu popupMenu = new JPopupMenu();
            popupMenu.add(new AbstractAction("Show in Browser") {

                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    try {
                        Desktop.getDesktop().browse(URI.create(item.link));
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Cannot open browser");
                    }
                }
            });
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
