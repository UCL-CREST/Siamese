    public void execute() {
        try {
            if (mode == MODE_INVALID) return;
            String fileName = "shot_" + formatter.format(Calendar.getInstance().getTime()) + ".jpg";
            BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            File file = new File(fileName);
            if (mode == MODE_QUALITY_AND_SCALE) {
                File fileTemp = new File(fileName + ".tmp");
                ImageIO.write(image, "jpg", fileTemp);
                Jpeg.processImageWithQualityAndScaling(fileTemp, file, quality, scaling);
                fileTemp.delete();
            } else if (mode == MODE_NORMAL) {
                ImageIO.write(image, "jpg", file);
            }
            System.out.println("Saved screen shot (" + image.getWidth() + " x " + image.getHeight() + " pixels) to file \"" + fileName + "\".");
            GlobalClass.files.add(new File(fileName));
        } catch (HeadlessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
