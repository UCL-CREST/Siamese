    private void screenShot() {
        try {
            BufferedImage screencapture = null;
            screencapture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            JFileChooser chooser2 = new JFileChooser(".");
            File deffile = new File("screenshot.jpg");
            chooser2.setFileFilter(new FileFilter("jpg"));
            chooser2.setSelectedFile(deffile);
            int result2 = chooser2.showSaveDialog(gui);
            String selecfile = null;
            if (result2 == JFileChooser.APPROVE_OPTION) {
                selecfile = chooser2.getSelectedFile().getPath();
                File file = new File(selecfile);
                ImageIO.write(screencapture, "jpg", file);
                JOptionPane.showMessageDialog(null, "Screenimage saved as " + file.getName(), "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            return;
        } catch (HeadlessException e) {
            e.printStackTrace();
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Error: Could not Save Screenimage", "Error", JOptionPane.ERROR_MESSAGE);
    }
