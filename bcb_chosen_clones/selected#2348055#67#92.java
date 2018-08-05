                public void run() {
                    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                    System.out.printf("width=%f, height=%f%n", d.getWidth(), d.getHeight());
                    Robot robot = null;
                    try {
                        robot = new Robot();
                    } catch (AWTException e1) {
                        e1.printStackTrace();
                    }
                    while (true) {
                        try {
                            BufferedImage img = robot.createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
                            ImageIO.write(img, "PNG", listener.getOutputStream());
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                    try {
                        System.out.println("close:" + listener);
                        listener.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
