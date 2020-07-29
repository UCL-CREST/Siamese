    private static void registerFont() {
        boolean found = false;
        String[] fonts = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (int i = 0; i < fonts.length; i++) {
            if (fonts[i].equals("DejaVu Sans")) found = true;
        }
        if (found) {
            Logger.logger.log(Level.INFO, "Font already installed");
        } else {
            try {
                java.io.InputStream fontStream = Main.class.getResourceAsStream("/com/ctext/ite/gui/resources/DejaVuSans.ttf");
                java.awt.Font myFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, fontStream);
                fontStream.close();
                if (java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(myFont)) Logger.logger.log(Level.INFO, "Font Registered"); else Logger.logger.log(Level.INFO, "Font Not Registered");
            } catch (Exception ex) {
                Logger.logger.log(Level.WARNING, "Error Loading Font", ex);
            }
        }
    }
