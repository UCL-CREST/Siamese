                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (Desktop.isDesktopSupported()) {
                            Desktop desktop = Desktop.getDesktop();
                            try {
                                desktop.browse(new URI("http://acts202.sourceforge.net"));
                                d.dispose();
                            } catch (IOException e1) {
                            } catch (URISyntaxException e2) {
                            }
                        } else {
                        }
                    }
