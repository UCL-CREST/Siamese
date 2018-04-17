    public static void screenshot(String pattern) {
        String sssavepath = storage.ConfigFile.getProperty("sslocation", "");
        if (sssavepath.equals("")) {
            System.err.println("Please set your Database values in Preferences-->Databases");
        }
        try {
            Toolkit thiskit = Toolkit.getDefaultToolkit();
            Dimension size = thiskit.getScreenSize();
            Rectangle rect = new Rectangle(0, 0, size.width, size.height);
            Robot bot = new Robot();
            BufferedImage image = bot.createScreenCapture(rect);
            File outfile = new File(sssavepath + "\\" + KeyRef.processText(pattern) + ".jpg");
            ImageIO.write(image, "jpg", outfile);
        } catch (AWTException e) {
            System.err.println("Error in capturing Screenshot");
            e.printStackTrace();
        } catch (IOException e1) {
            System.out.println("Error Making Screenshot File");
            e1.printStackTrace();
        }
    }
