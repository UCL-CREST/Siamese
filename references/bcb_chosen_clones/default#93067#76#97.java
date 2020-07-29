    public void actionPerformed(ActionEvent evt) {
        int button = fileDialog.showOpenDialog(this);
        if (button != JFileChooser.APPROVE_OPTION) return;
        LoaderFileFilter ff = (LoaderFileFilter) fileDialog.getFileFilter();
        try {
            File selected_file = fileDialog.getSelectedFile();
            URL selected_url = selected_file.toURL();
            FileLoaderDescriptor fld = ff.getDescriptor();
            Loader ldr = manager.getFileLoader(fld);
            if (ldr != null) {
                ldr.setFlags(Loader.LOAD_ALL);
                Scene scene = ldr.load(selected_url);
                BranchGroup bg = scene.getSceneGroup();
                bg.setCapability(BranchGroup.ALLOW_DETACH);
                if (currentContent != null) currentContent.detach();
                locale.addBranchGraph(bg);
                currentContent = bg;
            }
        } catch (IOException ioe) {
            System.out.println("Error loading file " + ioe.getMessage());
        }
    }
