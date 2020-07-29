    private void getFullItemDescription(int startX, int startY, int tab, int item, String rootDir) {
        try {
            Rectangle rect = new Rectangle(startX, startY, ITEM_WIDTH, ITEM_HEIGHT);
            BufferedImage bi = robot.createScreenCapture(rect);
            BufferedImage bi2 = bi.getSubimage(38, 0, ITEM_WIDTH - 38, 36);
            String white = muleOCR.getWhiteText(bi2);
            String yellow = muleOCR.getYellowText(bi2);
            String name;
            System.out.println("w = " + white);
            System.out.println("y = " + yellow);
            if (white == null || white.equals("")) {
                name = yellow;
            } else {
                name = white;
            }
            StringTokenizer stok = new StringTokenizer(name, "\n", false);
            if (stok.hasMoreTokens()) {
                name = stok.nextToken();
            }
            javax.imageio.ImageIO.write(bi, "png", new File(rootDir + File.separator + Item.getFileName(name)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
