    public static File getPantallazo() {
        try {
            Robot robot = new Robot();
            Toolkit medidor = Toolkit.getDefaultToolkit();
            Dimension dimensiones = medidor.getScreenSize();
            Rectangle pantalla = new Rectangle(dimensiones);
            BufferedImage imagen = robot.createScreenCapture(pantalla);
            File pantallazo = new File("pantallazo.png");
            pantallazo.deleteOnExit();
            ImageIO.write(imagen, "png", pantallazo);
            return pantallazo;
        } catch (AWTException ex) {
            ex.printStackTrace();
            return null;
        } catch (Exception e) {
            Logger.getRootLogger().error(e.toString());
            return null;
        }
    }
