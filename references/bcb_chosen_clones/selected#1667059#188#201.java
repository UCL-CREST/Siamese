    protected void saveMyself(org.w3c.dom.Element root, ZipOutputStream out) throws IOException {
        org.w3c.dom.Element elem = appendChild(root, "image");
        org.w3c.dom.Element cont = elem.getOwnerDocument().createElementNS("http://cdox.sf.net/schema/fileformat", "content");
        org.w3c.dom.Element img = elem.getOwnerDocument().createElementNS("http://cdox.sf.net/schema/fileformat", "file");
        img.setAttribute("name", ID + ".image.png");
        cont.appendChild(img);
        elem.appendChild(cont);
        out.putNextEntry(new ZipEntry(ID + ".image.png"));
        quality = true;
        ImageIO.write(getBufferedImage(), "png", out);
        quality = false;
        resetImage();
        out.closeEntry();
    }
