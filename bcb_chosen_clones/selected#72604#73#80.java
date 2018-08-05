    protected static void scaleImage(InputStream in, String name, ZipOutputStream out, double scaleX, double scaleY) throws Exception {
        BufferedImage img = ImageIO.read(in);
        Image scaled = img.getScaledInstance((int) (img.getWidth() * scaleX), (int) (img.getHeight() * scaleY), Image.SCALE_SMOOTH);
        BufferedImage bScaled = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        bScaled.getGraphics().drawImage(scaled, 0, 0, null);
        out.putNextEntry(new ZipEntry(name));
        ImageIO.write(bScaled, "PNG", out);
    }
