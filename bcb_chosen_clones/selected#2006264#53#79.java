    public static File takeAPictureAndSaveIt(int wait, String dir) {
        try {
            Thread.sleep(wait);
            boolean wasVisible = false;
            if (dir == null && Context.wind != null) {
                if (Context.wind.isVisible()) {
                    Context.wind.setVisible(false);
                    wasVisible = true;
                }
            }
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            String imagefile = "img" + ORDER++ + ".gif";
            if (dir != null) {
                imagefile = dir + "/snd" + ORDER + ".nk";
            }
            File file = new File(imagefile);
            ImageIO.write(image, "gif", file);
            if (dir == null && wasVisible && Context.wind != null) {
                Context.wind.setVisible(true);
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
