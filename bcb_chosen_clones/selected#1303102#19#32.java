    public static File takeAPictureAndSaveIt(String path, int wait) {
        try {
            Thread.sleep(wait);
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            String imagefile = "img" + ORDER++ + ".gif";
            File file = new File(path + "/" + imagefile);
            ImageIO.write(image, "gif", file);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
