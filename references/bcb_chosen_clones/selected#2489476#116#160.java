    public static Photo createPhoto(String title, String userLogin, String pathToPhoto, String basePathImage) throws NoSuchAlgorithmException, IOException {
        String id = CryptSHA1.genPhotoID(userLogin, title);
        String extension = pathToPhoto.substring(pathToPhoto.lastIndexOf("."));
        String destination = basePathImage + id + extension;
        FileInputStream fis = new FileInputStream(pathToPhoto);
        FileOutputStream fos = new FileOutputStream(destination);
        FileChannel fci = fis.getChannel();
        FileChannel fco = fos.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            int read = fci.read(buffer);
            if (read == -1) break;
            buffer.flip();
            fco.write(buffer);
            buffer.clear();
        }
        fci.close();
        fco.close();
        fos.close();
        fis.close();
        ImageIcon image;
        ImageIcon thumb;
        String destinationThumb = basePathImage + "thumb/" + id + extension;
        image = new ImageIcon(destination);
        int maxSize = 150;
        int origWidth = image.getIconWidth();
        int origHeight = image.getIconHeight();
        if (origWidth > origHeight) {
            thumb = new ImageIcon(image.getImage().getScaledInstance(maxSize, -1, Image.SCALE_SMOOTH));
        } else {
            thumb = new ImageIcon(image.getImage().getScaledInstance(-1, maxSize, Image.SCALE_SMOOTH));
        }
        BufferedImage bi = new BufferedImage(thumb.getIconWidth(), thumb.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        g.drawImage(thumb.getImage(), 0, 0, null);
        try {
            ImageIO.write(bi, "JPG", new File(destinationThumb));
        } catch (IOException ioe) {
            System.out.println("Error occured saving thumbnail");
        }
        Photo photo = new Photo(id);
        photo.setTitle(title);
        photo.basePathImage = basePathImage;
        return photo;
    }
