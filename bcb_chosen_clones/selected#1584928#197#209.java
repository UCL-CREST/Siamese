        public void shoot(Rectangle screenRect) {
            try {
                Robot robot = new Robot();
                BufferedImage image = robot.createScreenCapture(screenRect);
                String outFileName = "shot" + (++shot) + ".png";
                File file = new File(path, outFileName);
                file.mkdirs();
                ImageIO.write(image, "png", file);
                System.out.println(file.getPath());
            } catch (AWTException ae) {
            } catch (IOException ioe) {
            }
        }
