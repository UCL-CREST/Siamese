            @Override
            public void mouseClicked(int id) {
                File licenseFile = new File("." + File.separator + "license.txt");
                if (!licenseFile.exists()) {
                    licenseFile = new File("." + File.separator + "deploy" + File.separator + "license.txt");
                }
                if (licenseFile.exists()) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().open(licenseFile);
                        } catch (IOException e) {
                            MessageUtil.addMessage("Unable to open the license file: " + e.getMessage());
                        }
                    }
                } else {
                    MessageUtil.addMessage("Unable to locate the license file: " + licenseFile.getAbsolutePath());
                }
            }
