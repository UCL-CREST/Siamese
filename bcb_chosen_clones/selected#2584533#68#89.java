    private Font getFont() {
        try {
            InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(font_name);
            if (fis == null) {
                String pack = this.getClass().getPackage().getName().replace(".", System.getProperty("file.separator"));
                String subdir = "WEB-INF.classes.".replace(".", System.getProperty("file.separator"));
                String path = servletContext.getRealPath("/");
                path = path.concat(subdir);
                path = path.concat(pack);
                path = path.concat(System.getProperty("file.separator"));
                File file = new File(path + font_name);
                fis = new FileInputStream(file);
            }
            font = Font.createFont(Font.TRUETYPE_FONT, fis);
            fis.close();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return font.deriveFont(this.font_size);
    }
