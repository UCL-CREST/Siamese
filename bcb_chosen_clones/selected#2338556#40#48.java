            public void actionPerformed(final ActionEvent e) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        java.awt.Desktop.getDesktop().open(new File("resources/hilfe/index.html"));
                    } catch (final IOException ex) {
                        System.err.println("Die Hilfeseite von BAUS! konnte nicht gefunden werden");
                    }
                }
            }
