        private MenuItem createNewMenuItemForBuildReport(BuildReport theBuildReport, Font theBuildFailedFont, Font theBuildSucessFont) {
            MenuItem newMenuItem = new MenuItem(getMenuItemLabelForBuildReport(theBuildReport));
            if (theBuildReport.hasFailed()) {
                newMenuItem.setFont(theBuildFailedFont);
                this.numberOfFailedBuilds++;
            } else {
                newMenuItem.setFont(theBuildSucessFont);
            }
            newMenuItem.setActionCommand(theBuildReport.getId());
            newMenuItem.setName(theBuildReport.getName());
            ActionListener newMenuItemActionListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(monitor.getBuildURI(e.getActionCommand()));
                        } catch (IOException err) {
                        }
                    }
                }
            };
            newMenuItem.addActionListener(newMenuItemActionListener);
            return newMenuItem;
        }
