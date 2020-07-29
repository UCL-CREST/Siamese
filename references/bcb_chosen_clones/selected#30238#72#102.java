    static void createSaveFile(File file, JLayeredPane sp, String title) throws FileNotFoundException, IOException {
        int bgDisplay = 0;
        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(file));
        String manifest = "<?xml version='1.0' encoding='utf-8'?>\n" + "<!DOCTYPE slideshow [" + "<!ELEMENT scrapbook (scrappage+)>\n" + "<!ELEMENT scrappage (color, imageelem*, textelem*)>\n" + "<!ELEMENT imageelem (file, scale, rotataion, xpos, ypos)>\n" + "<!ELEMENT textelem (text, fontname, fontsize, fontbold, fontitalis, color, scale, rotataion, xpos, ypos)>\n" + "<!ELEMENT color (red, green, blue)>]>\n" + "<scrapbook>\n" + "<scrappage>\n" + "<title><![CDATA[" + title + "]]></title>\n";
        for (int i = 0; i < sp.getComponentCount(); i++) {
            if (sp.getComponent(i).getClass().getName().equals("digiscrap.ScrapElementImage")) {
                String fName = "images/image" + i + ".png";
                manifest += manifestImage((ScrapElementImage) (sp.getComponent(i)), fName);
                zip.putNextEntry(new ZipEntry(fName));
                ImageIO.write(((ScrapElementImage) (sp.getComponent(i))).getImg(), "png", zip);
            } else if (sp.getComponent(i).getClass().getName().equals("digiscrap.ScrapElementText")) {
                manifest += manifestText((ScrapElementText) (sp.getComponent(i)));
            } else if (sp.getComponent(i).getClass().getName().equals("digiscrap.ScrapPanel")) {
                String fName = "images/background.png";
                bgDisplay = ((ScrapPanel) (sp.getComponent(i))).getBgDisplay();
                manifest += "<bgManifested />";
                zip.putNextEntry(new ZipEntry(fName));
                ImageIO.write(((ScrapPanel) (sp.getComponent(i))).getBgImage(), "png", zip);
            }
        }
        manifest += "<background>\n" + "<color>\n" + "<red>" + sp.getBackground().getRed() + "</red>\n" + "<green>" + sp.getBackground().getGreen() + "</green>\n" + "<blue>" + sp.getBackground().getBlue() + "</blue>\n" + "<bgDisplay>" + bgDisplay + "</bgDisplay>\n" + "</color>\n" + "</background>\n" + "</scrappage>\n" + "</scrapbook>\n";
        zip.putNextEntry(new ZipEntry("digiscrap.xml"));
        zip.write(manifest.getBytes());
        String fName = "previews/preview1.png";
        manifest += "<bgManifested />";
        zip.putNextEntry(new ZipEntry(fName));
        BufferedImage bi = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);
        sp.print(bi.getGraphics());
        ImageIO.write(bi, "png", zip);
        zip.close();
    }
