        public void actionPerformed(ActionEvent e) {
            String aboutText = "Slug application.";
            String aboutURL = System.getProperty(GateConstants.ABOUT_URL_JAVA_PROPERTY_NAME);
            boolean canShowInPane = false;
            if (aboutURL != null) {
                try {
                    URL url = new URL(aboutURL);
                    AboutPaneDialog dlg = new AboutPaneDialog(ShellSlacFrame.this, "Slug application about", true);
                    canShowInPane = dlg.setURL(url);
                    if (canShowInPane) {
                        dlg.setSize(300, 200);
                        dlg.setLocationRelativeTo(ShellSlacFrame.this);
                        dlg.setVisible(true);
                    } else {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                        String line = "";
                        StringBuffer content = new StringBuffer();
                        do {
                            content.append(line);
                            line = reader.readLine();
                        } while (line != null);
                        if (content.length() != 0) {
                            aboutText = content.toString();
                        }
                    }
                } catch (Exception ex) {
                    if (DEBUG) {
                        ex.printStackTrace();
                    }
                }
            }
            if (!canShowInPane) JOptionPane.showMessageDialog(ShellSlacFrame.this, aboutText, "Slug application about", JOptionPane.INFORMATION_MESSAGE);
        }
