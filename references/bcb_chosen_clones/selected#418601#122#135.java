                    @Override
                    public void mouseClicked(MouseEvent arg0) {
                        try {
                            if (Desktop.isDesktopSupported()) {
                                Desktop desktop = Desktop.getDesktop();
                                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                                    URI website = new URI(TrackPlanMain.APP_WEBSITE);
                                    Desktop.getDesktop().browse(website);
                                }
                            }
                        } catch (Exception e) {
                            return;
                        }
                    }
