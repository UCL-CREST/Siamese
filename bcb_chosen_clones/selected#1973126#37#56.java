    public void run() {
        FileOutputStream fos = null;
        while (true) {
            try {
                BufferedImage image = robot.createScreenCapture(rectangle);
                fos = new FileOutputStream("C:\\records\\" + i + ".gif");
                JPEGCodec.createJPEGEncoder(fos).encode(image);
                fos.close();
                i = i + 1;
                Thread.sleep(25);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
                try {
                    if (fos != null) fos.close();
                } catch (Exception e1) {
                }
            }
        }
    }
