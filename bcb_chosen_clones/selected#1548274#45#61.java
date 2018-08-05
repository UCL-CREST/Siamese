            public void mouseClicked(MouseEvent e) {
                long delay = System.currentTimeMillis() - lastClicked;
                if (e.getButton() == MouseEvent.BUTTON1 && delay > 1000) {
                    e.consume();
                    lastClicked = System.currentTimeMillis();
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            try {
                                desktop.browse(new URI("http://everquest2.com/Valor/" + URLEncoder.encode(label.getText(), "UTF-8") + "/"));
                            } catch (Exception ex) {
                                System.err.println(ex.getMessage());
                            }
                        }
                    }
                }
            }
