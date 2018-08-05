    public void writeFiles(ZipOutputStream zos, DataOutputStream dos) {
        if (image_file.length() == 0) return;
        try {
            if (image == null) loadImage();
            int w = image.getWidth(this);
            int h = image.getHeight(this);
            PixelGrabber grabber = new PixelGrabber(image, 0, 0, -1, -1, true);
            try {
                grabber.grabPixels();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            zos.putNextEntry(new ZipEntry(image_file));
            dos.writeInt(w);
            dos.writeInt(h);
            int a[] = (int[]) grabber.getPixels();
            dos.writeInt(a.length);
            for (int i = 0; i < a.length; i++) dos.writeInt(a[i]);
            zos.closeEntry();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
