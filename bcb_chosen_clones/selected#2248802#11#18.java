    public static void criarScreenShot() {
        try {
            BufferedImage bi;
            bi = new Robot().createScreenCapture(new Rectangle(0, 0, 1024, 768));
            ImageIO.write(bi, "jpg", new File("suporte.jpg"));
        } catch (Exception e) {
        }
    }
