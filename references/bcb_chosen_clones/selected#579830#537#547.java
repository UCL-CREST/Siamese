    public static void takeScreenCap(Rectangle rv, String filename) {
        try {
            Robot robot = new Robot();
            BufferedImage bi = robot.createScreenCapture(rv);
            new CFile("caps").mkdir();
            File file = new CFile("caps/" + filename + ".jpg");
            ImageIO.write(bi, "jpg", file);
        } catch (Throwable e) {
            log.warn("", e);
        }
    }
