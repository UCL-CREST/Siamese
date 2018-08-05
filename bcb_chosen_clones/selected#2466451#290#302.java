                public void actionPerformed(ActionEvent e) {
                    checkAndDisplayMessages();
                    if (Desktop.isDesktopSupported()) {
                        try {
                            URI uri = new URI("https://www.google.com/voice/");
                            Desktop.getDesktop().browse(uri);
                        } catch (URISyntaxException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
