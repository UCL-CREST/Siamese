        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            if (actionCommand.compareTo("About") == 0) {
                AboutWindow aw = new AboutWindow();
                aw.setLocationRelativeTo(bw);
                aw.setBounds(20, 40, 800, 600);
                aw.setVisible(true);
                return;
            }
            String url = docsDirectory + "/" + helpMenuFiles.get(actionCommand);
            boolean browserLaunched = false;
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                URI uri = null;
                File helpFile = new File(url);
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    browserLaunched = true;
                    try {
                        uri = helpFile.toURI();
                        desktop.browse(uri);
                    } catch (IOException ioe) {
                        System.out.println("The system cannot find the " + uri + " file specified");
                    }
                }
            }
            if (!browserLaunched) {
                HelpWindow hw = new HelpWindow(url);
                hw.setLocationRelativeTo(bw);
                hw.setBounds(20, 40, 800, 600);
                hw.setVisible(true);
            }
        }
