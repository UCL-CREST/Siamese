            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ApplicationType app = MetaManager.getMainWindow().getApplicationBar().getApplication();
                    File outputHtml = new ApplicationHelper().applyViewApplicationXsl(app);
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        URI uri = outputHtml.toURI();
                        logger.debug("Displaying in browser: " + uri.toString());
                        desktop.browse(uri);
                    } else {
                        logger.debug("Displaying via dialog");
                        String title = "View Recommendation: " + app.getName();
                        ViewHtmlDialog recDialog = new ViewHtmlDialog(MetaManager.getMainWindow(), true, title, outputHtml);
                        recDialog.setVisible(true);
                    }
                } catch (Exception ex) {
                    logger.fatal("Error while viewing an application.", ex);
                }
            }
