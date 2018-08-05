    public static void createScreenShot(File a_file, Rectangle a_rectangle) throws Exception {
        Robot l_robot = new Robot();
        BufferedImage l_img = l_robot.createScreenCapture(a_rectangle);
        a_file.getParentFile().mkdirs();
        String l_extension = FTools.getExtension(a_file);
        ImageIO.write(l_img, l_extension, a_file);
    }
