    public void exec() {
        JFileChooser jfc = new JFileChooser();
        if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            try {
                Model model = new ModelMem();
                RDFReader jenaReader = new JenaReader();
                Reader r = new InputStreamReader(new FileInputStream(file), "UTF8");
                jenaReader.read(model, r, getBaseURI());
                replaceProjectModel(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
