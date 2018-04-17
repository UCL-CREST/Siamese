    static void snap() throws AWTException, IOException {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.printf("width=%f, height=%f%n", d.getWidth(), d.getHeight());
        BufferedImage img = new Robot().createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
        System.out.println(ImageIO.write(img, "PNG", new File("d:/curScreen.PNG")));
    }
