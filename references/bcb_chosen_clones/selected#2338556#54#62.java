            public void actionPerformed(final ActionEvent e) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                    try {
                        java.awt.Desktop.getDesktop().open(new File("auftraege/test.pdf"));
                    } catch (final IOException ex) {
                        System.err.println("Der Auftrag konnte nicht angezeigt werden.");
                    }
                }
            }
