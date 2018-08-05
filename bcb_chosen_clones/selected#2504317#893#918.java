    private void frameShot(JFrame f) {
        try {
            BufferedImage screencapture = null;
            screencapture = new Robot().createScreenCapture(new Rectangle(f.getX(), f.getY(), f.getWidth(), f.getHeight()));
            JFileChooser chooser2 = new JFileChooser(".");
            File deffile = new File("frameshot.jpg");
            chooser2.setSelectedFile(deffile);
            chooser2.setFileFilter(new FileFilter("jpg"));
            int result2 = chooser2.showSaveDialog(gui);
            String selecfile = null;
            if (result2 == JFileChooser.APPROVE_OPTION) {
                selecfile = chooser2.getSelectedFile().getPath();
                File file = new File(selecfile);
                ImageIO.write(screencapture, "jpg", file);
                JOptionPane.showMessageDialog(null, "Frameimage saved as " + file.getName(), "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            return;
        } catch (HeadlessException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Error: Could not Save Frameimage", "Error", JOptionPane.ERROR_MESSAGE);
    }
