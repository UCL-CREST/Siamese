            @Override
            public void mouseClicked(int id) {
                URI uri;
                try {
                    uri = new URI(DialogMessages.website);
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(uri);
                        } catch (IOException e) {
                            MessageUtil.addMessage("Unable to open the license file: " + e.getMessage());
                        }
                    }
                } catch (URISyntaxException e1) {
                    MessageUtil.addMessage("Unable to open the website: " + e1.getMessage());
                }
            }
