    public static void snapShot(String fileName, String format, int x, int y, int width, int height) throws Exception {
        Dimension d = null;
        try {
            d = Toolkit.getDefaultToolkit().getScreenSize();
            BufferedImage screenshot = (new Robot()).createScreenCapture(new Rectangle(x, y, width, height));
            File f = new File(fileName);
            System.out.print("Save File " + fileName);
            ImageIO.write(screenshot, format, f);
            System.out.print("..Finished!\n");
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (d != null) d = null;
        }
    }
