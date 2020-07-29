    public JButton createPreviewFileChooserButton() {
        Action a = new AbstractAction(getString("FileChooserDemo.previewbutton")) {

            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = createFileChooser();
                ExampleFileFilter filter = new ExampleFileFilter(new String[] { "jpg", "gif" }, getString("FileChooserDemo.filterdescription"));
                ExampleFileView fileView = new ExampleFileView();
                fileView.putIcon("jpg", jpgIcon);
                fileView.putIcon("gif", gifIcon);
                fc.setFileView(fileView);
                fc.addChoosableFileFilter(filter);
                fc.setFileFilter(filter);
                fc.setAccessory(new FilePreviewer(fc));
                int result = fc.showOpenDialog(getDemoPanel());
                if (result == JFileChooser.APPROVE_OPTION) {
                    loadImage(fc.getSelectedFile().getPath());
                }
            }
        };
        return createButton(a);
    }
