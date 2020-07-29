    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ZipOutputStream zipout = new ZipOutputStream(resp.getOutputStream());
        ZipOutputStream zipout2 = new ZipOutputStream(new FileOutputStream(new File("c:/t.zip")));
        for (int i = 0; i < 200; i++) {
            BufferedImage img = new BufferedImage(800 + i, 600 + i, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) img.getGraphics();
            g.setPaint(new GradientPaint(0, 0, Color.YELLOW, img.getWidth() - 1, img.getHeight() - 1, Color.BLUE, true));
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
            for (int t = 0; t < img.getHeight(); t += 10) {
                g.setColor(new Color(t + 1 * 100 + 1 * 10000));
                g.drawLine(0, t, img.getWidth(), t);
            }
            g.dispose();
            ZipEntry entry = new ZipEntry("hoge" + i + ".bmp");
            zipout.putNextEntry(entry);
            ImageIO.write(img, "bmp", zipout);
            ZipEntry entry2 = new ZipEntry("hoge" + i + ".bmp");
            zipout2.putNextEntry(entry2);
            ImageIO.write(img, "bmp", zipout2);
        }
        zipout.close();
        zipout2.close();
    }
