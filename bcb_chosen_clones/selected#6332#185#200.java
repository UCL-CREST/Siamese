            public void actionPerformed(ActionEvent evt) {
                Object[] buttons = { "Visit our Website", "Ok" };
                if (JOptionPane.showOptionDialog(JPCApplication.this, ABOUT_US, "About JPC", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[1]) == 0) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(JPC_URI);
                        } catch (IOException e) {
                            LOGGING.log(Level.INFO, "Couldn't find or launch the default browser.", e);
                        } catch (UnsupportedOperationException e) {
                            LOGGING.log(Level.INFO, "Browse action not supported.", e);
                        } catch (SecurityException e) {
                            LOGGING.log(Level.INFO, "Browse action not permitted.", e);
                        }
                    }
                }
            }
