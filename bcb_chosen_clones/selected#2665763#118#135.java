            @Override
            public void mouseClicked(int id) {
                File readmeFile = new File("." + File.separator + "readme.txt");
                if (!readmeFile.exists()) {
                    readmeFile = new File("." + File.separator + "deploy" + File.separator + "readme.txt");
                }
                if (readmeFile.exists()) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().open(readmeFile);
                        } catch (IOException e) {
                            MessageUtil.addMessage("Unable to open the readme file: " + e.getMessage());
                        }
                    }
                } else {
                    MessageUtil.addMessage("Unable to locate the readme file: " + readmeFile.getAbsolutePath());
                }
            }
