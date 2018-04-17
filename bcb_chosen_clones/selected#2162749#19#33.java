    public static String takeAPictureToWebPageAndSaveIt(String absolutePath, String picd, String browser, int wait) {
        try {
            Process p = Runtime.getRuntime().exec(browser + " " + absolutePath);
            Thread.sleep(wait);
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            String imagefile = "img" + ORDER++ + ".gif";
            ImageIO.write(image, "gif", new File(picd + "/" + imagefile));
            p.destroy();
            return imagefile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false.gif";
    }
