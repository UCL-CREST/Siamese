    public ManageUsers() {
        if (System.getProperty("user.home") != null) {
            dataFile = new File(System.getProperty("user.home") + File.separator + "MyRx" + File.separator + "MyRx.dat");
            File dataFileDir = new File(System.getProperty("user.home") + File.separator + "MyRx");
            dataFileDir.mkdirs();
        } else {
            dataFile = new File("MyRx.dat");
        }
        try {
            dataFile.createNewFile();
        } catch (IOException e1) {
            logger.error(e1);
            JOptionPane.showMessageDialog(Menu.getMainMenu(), e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        File oldDataFile = new File("MyRx.dat");
        if (oldDataFile.exists()) {
            FileChannel src = null, dst = null;
            try {
                src = new FileInputStream(oldDataFile.getAbsolutePath()).getChannel();
                dst = new FileOutputStream(dataFile.getAbsolutePath()).getChannel();
                dst.transferFrom(src, 0, src.size());
                if (!oldDataFile.delete()) {
                    oldDataFile.deleteOnExit();
                }
            } catch (FileNotFoundException e) {
                logger.error(e);
                JOptionPane.showMessageDialog(Menu.getMainMenu(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                logger.error(e);
                JOptionPane.showMessageDialog(Menu.getMainMenu(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    src.close();
                    dst.close();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }
    }
