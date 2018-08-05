    public static void screenshot(Component c) {
        Rectangle screenRect = c.getBounds();
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        long t = System.currentTimeMillis();
        String outFileName = Prefs.current.screenshotdir + "/shot" + t + ".png";
        BufferedImage image = robot.createScreenCapture(screenRect);
        try {
            ImageIO.write(image, "png", new File(outFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Saved screen shot (" + image.getWidth() + " x " + image.getHeight() + " pixels) to file \"" + outFileName + "\".");
        if (Prefs.current.screenshotupload) {
            int w = image.getWidth();
            int h = image.getHeight();
            if ((w > 800) | (h > 625)) {
                if (w > h) {
                    h = (int) Math.floor((800f / w) * h);
                    w = 800;
                } else {
                    w = (int) Math.floor((625f / h) * w);
                    h = 625;
                }
            }
            BufferedImage smallimage = getScaledInstance(image, w, h, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
            String tempFileName = Prefs.current.screenshotdir + "/" + t + ".jpg";
            try {
                ImageIO.write(smallimage, "jpg", new File(tempFileName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HTTPUtils.SubmitPicture(tempFileName, "");
        }
    }
