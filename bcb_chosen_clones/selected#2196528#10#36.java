    public ImportFromFile() {
        JFrame jf = new JFrame();
        JFileChooser fc = new JFileChooser(KTH.data._path);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fc.showOpenDialog(jf);
        File file = null;
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            KTH.data._path = file.getParent();
            System.out.println(file.getAbsolutePath() + "\n");
        } else {
        }
        if (file != null) {
            File f = new File(KTH.data.getHomeDir() + File.separator + KTH.data.katalogName);
            System.out.println(f + (f.exists() ? " is found " : " is missing "));
            if (f.exists() && !IOUtils.isNewerContent(file, f)) {
            } else {
                try {
                    IOUtils.copyFile(file, f, true);
                } catch (Exception e) {
                    KTH.out.err(e);
                }
            }
            KTH.dc2.removeDc("Import");
        } else {
        }
    }
