    public void setFontFamily(String family, boolean file) {
        if (file) try {
            Font f = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/" + family));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(f);
            this.fontFamily = core.loadFont("Fonts/" + family).getFamily();
        } catch (FileNotFoundException e) {
            System.out.println("Fichier font introuvable");
            e.printStackTrace();
        } catch (FontFormatException e) {
            System.out.println("Format de font incorrect");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } else this.fontFamily = family;
        windowFont = new Font(fontFamily, windowFont.getStyle(), windowFont.getSize());
    }
