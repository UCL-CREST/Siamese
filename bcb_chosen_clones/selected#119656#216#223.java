            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(new URI("https://sourceforge.net/tracker/?func=add&group_id=235519&atid=1096880"));
                } catch (Exception ex) {
                    RunnerClass.logger.log(Level.SEVERE, null, ex);
                }
            }
