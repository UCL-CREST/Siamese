    private void saveProjectMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(this);
        String path = "c:\\";
        String fileName = "TestSave";
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            path = fc.getSelectedFile().getParent();
            fileName = fc.getSelectedFile().getName();
        } else {
            return;
        }
        save(path + fileName + "-Map.jpg", colorMapImage);
        ImageIcon originalimage = (ImageIcon) originalLabel.getIcon();
        BufferedImage oi = (BufferedImage) originalimage.getImage();
        save(path + fileName + "-Marked.jpg", oi);
        save(path + fileName + "-original.jpg", im.getImage());
        byte[] buffer = new byte[18024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(path + fileName + ".zip"));
            out.setLevel(Deflater.DEFAULT_COMPRESSION);
            FileInputStream in1 = new FileInputStream(path + fileName + "-Map.jpg");
            FileInputStream in2 = new FileInputStream(path + fileName + "-Marked.jpg");
            FileInputStream in3 = new FileInputStream(path + fileName + "-original.jpg");
            out.putNextEntry(new ZipEntry(fileName + "-Map.jpg"));
            int len;
            while ((len = in1.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.putNextEntry(new ZipEntry(fileName + "-Marked.jpg"));
            len = 0;
            while ((len = in2.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.putNextEntry(new ZipEntry(fileName + "-original.jpg"));
            len = 0;
            while ((len = in3.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.close();
            in1.close();
            in2.close();
            in3.close();
            File originalFile = new File(path + fileName + "-original.jpg");
            File mapFile = new File(path + fileName + "-Map.jpg");
            File markedFile = new File(path + fileName + "-Marked.jpg");
            originalFile.delete();
            mapFile.delete();
            markedFile.delete();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
