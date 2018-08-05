    protected synchronized Component getLoadButton() {
        if (loadButton == null) {
            loadButton = new JButton();
            loadButton.setText("Load");
            loadButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    InputStream is = null;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    Object o = getFileTree().getSelectedFile();
                    if (!(o instanceof File)) {
                        System.out.println("No file selected");
                        return;
                    }
                    File f = (File) o;
                    try {
                        is = new FileInputStream(f);
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                        return;
                    }
                    byte[] tmp = new byte[1024];
                    while (true) {
                        int count = 0;
                        try {
                            count = is.read(tmp);
                            if (count > 0) bos.write(tmp, 0, count);
                            if (count < 1024) break;
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            return;
                        }
                    }
                    byte[] allBytes = bos.toByteArray();
                    ResourceStoreLoader rsl = new ResourceStoreLoader();
                    Resource r = rsl.load(allBytes);
                }
            });
        }
        return loadButton;
    }
