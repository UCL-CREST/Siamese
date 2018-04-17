    public void checkMoment() {
        int moment = -1;
        int[] pix = new int[131];
        BufferedImage image1 = this.bot.createScreenCapture(new Rectangle(i + 180, j + 97, 131, 1));
        try {
            ImageIO.write(image1, "png", new File("coincoin.png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        PixelGrabber pg = new PixelGrabber(image1, 0, 0, 131, 1, pix, 0, 131);
        try {
            pg.grabPixels();
        } catch (InterruptedException d) {
            System.err.println("en attente des pixels");
        }
        if (pix[130] == -1) {
            moment = 2;
        } else if (pix[100] == -1) {
            moment = 1;
        } else if (pix[0] == -1) {
            moment = 0;
        }
        System.out.println("Moment: " + moment);
        this.moment = moment;
    }
