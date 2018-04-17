    protected static void capturaPantalla(String path) {
        BufferedImage pantalla = obtenerCapturaPantalla();
        try {
            pantalla = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            File file = new File(path);
            ImageIO.write(pantalla, "jpg", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
