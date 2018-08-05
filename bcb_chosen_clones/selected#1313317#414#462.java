    public static File createZipFile(BPMNGraphComponent graphComponent, String barFilename) {
        String[] filenames;
        String filename = BPMNEditor.getCurrentFile().getName();
        if (!filename.endsWith(".bpmn20.xml")) {
            filename = filename.substring(0, filename.lastIndexOf(".")) + ".bpmn20.xml";
        }
        String randomFilename = System.getProperty("java.io.tmpdir", "/tmp") + "/yaoqiang_deploy_" + filename.substring(0, filename.lastIndexOf(".bpmn20.xml")) + new Random().nextInt(Integer.MAX_VALUE);
        String imageFilename = randomFilename + ".png";
        BufferedImage image = mxCellRenderer.createBufferedImage(graphComponent.getGraph(), null, 1, null, graphComponent.isAntiAlias(), null, graphComponent.getCanvas());
        if (image != null) {
            try {
                ImageIO.write(image, "png", new File(imageFilename));
            } catch (IOException e) {
                e.printStackTrace();
            }
            filenames = new String[] { filename, filename.substring(0, filename.lastIndexOf(".bpmn20.xml")) + ".png" };
        } else {
            filenames = new String[] { filename };
        }
        String zipFilename = barFilename;
        if (zipFilename == null) {
            zipFilename = randomFilename + ".bar";
        }
        ZipOutputStream out;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFilename));
            int len;
            byte[] buffer = new byte[1024];
            for (int i = 0; i < filenames.length; i++) {
                String tempname = BPMNEditor.getCurrentFile().getAbsolutePath();
                if (i == 1) {
                    tempname = imageFilename;
                }
                FileInputStream in = new FileInputStream(tempname);
                out.putNextEntry(new ZipEntry(filenames[i]));
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new File(zipFilename);
    }
