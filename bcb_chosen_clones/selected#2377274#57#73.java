    public void run() {
        try {
            Thread.sleep(delay * SECONDS);
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(bounds);
            String filename = file.getName();
            int lastDot = filename.lastIndexOf('.');
            String extension = filename.substring(lastDot + 1);
            ImageIO.write(image, extension, file);
        } catch (IOException exception) {
            throw new RuntimeException("IOException: " + exception.getMessage());
        } catch (AWTException exception) {
            throw new RuntimeException("AWTException: " + exception.getMessage());
        } catch (InterruptedException interrupt) {
            throw new RuntimeException("interrupt: " + interrupt.getMessage());
        }
    }
