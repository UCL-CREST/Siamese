    public static void createScreenCapture(String jpegFile) {
        try {
            Toolkit tk = Toolkit.getDefaultToolkit();
            tk.sync();
            Rectangle ecran = new Rectangle(tk.getScreenSize());
            Robot robot = new Robot();
            robot.setAutoDelay(0);
            robot.setAutoWaitForIdle(false);
            BufferedImage image = robot.createScreenCapture(ecran);
            File file = new File(jpegFile);
            javax.imageio.ImageIO.write(image, "JPEG", file);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
