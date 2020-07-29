            public void mouseClicked(MouseEvent e) {
                long delay = System.currentTimeMillis() - lastClicked;
                if (e.getButton() == MouseEvent.BUTTON1 && delay > 1000) {
                    e.consume();
                    lastClicked = System.currentTimeMillis();
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            try {
                                desktop.browse(new URI("http://www.lootdb.com/eq2/item/" + ItemLink.this.chatLink.getId()));
                            } catch (Exception ex) {
                                System.err.println(ex.getMessage());
                            }
                        }
                    }
                }
            }
