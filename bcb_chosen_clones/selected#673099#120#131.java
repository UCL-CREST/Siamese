                        public void run() {
                            if (Desktop.isDesktopSupported()) {
                                Desktop desktop = Desktop.getDesktop();
                                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                                    try {
                                        desktop.browse(URI.create("http://drop.to/goblin"));
                                    } catch (Exception exc) {
                                        System.err.println("Nije omoguceno krstarenje Internetom");
                                    }
                                }
                            }
                        }
