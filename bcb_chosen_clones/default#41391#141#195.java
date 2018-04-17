    private void createButtons() {
        redownloadButton = new DarkButton("Redownload image", 30, 120);
        redownloadButton.setBorderPainted(false);
        redownloadButton.setBackground(new Color(0, 0, 0, 0));
        redownloadButton.setFocusable(false);
        redownloadButton.setOpaque(false);
        redownloadButton.setFont(new Font("Dialog", Font.PLAIN, 10));
        redownloadButton.setForeground(Color.WHITE);
        redownloadButton.setContentAreaFilled(false);
        redownloadButton.setVerticalTextPosition(AbstractButton.CENTER);
        redownloadButton.setHorizontalTextPosition(AbstractButton.CENTER);
        redownloadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                avatar.forceDownloadImage();
            }
        });
        changePictureButton = new DarkButton("Set local image", 30, 120);
        changePictureButton.setFont(new Font("Dialog", Font.PLAIN, 10));
        changePictureButton.setContentAreaFilled(false);
        changePictureButton.setVerticalTextPosition(AbstractButton.CENTER);
        changePictureButton.setHorizontalTextPosition(AbstractButton.CENTER);
        changePictureButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (fc == null) {
                    fc = new JFileChooser();
                    fc.addChoosableFileFilter(new ImageFilter());
                    fc.setAcceptAllFileFilterUsed(false);
                    fc.setFileView(new ImageFileView());
                    fc.setAccessory(new ImagePreview(fc));
                }
                int returnVal = fc.showOpenDialog(albumPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    avatar.setImage(file);
                }
                fc.setSelectedFile(null);
            }
        });
        closeButton = new JButton(new CloseIcon(16, 16));
        closeButton.setBorderPainted(false);
        closeButton.setBackground(new Color(0, 0, 0, 0));
        closeButton.setFocusable(false);
        closeButton.setOpaque(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setForeground(Color.WHITE);
        closeButton.setPressedIcon(new CloseIcon(15, 15));
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ((AlbumApplet) parent).removeAvatarFrame();
            }
        });
    }
