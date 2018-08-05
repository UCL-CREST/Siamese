    public void loadXesSource() {
        try {
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            if (EncoderLoad.directory == null) EncoderLoad.directory = new File(System.getProperty("user.dir"));
            fileChooser.setCurrentDirectory(EncoderLoad.directory);
            fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            int returnvalue = fileChooser.showDialog(null, "Load");
            if (returnvalue == javax.swing.JFileChooser.APPROVE_OPTION) {
                ((EncoderLoad) (ELoad)).setBaseDir(fileChooser.getCurrentDirectory());
                ELoad.setBase(fileChooser.getSelectedFile());
                Vector vsymbolTable = new Vector();
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(fileChooser.getSelectedFile());
                org.w3c.dom.Node root = doc.getFirstChild();
                if (baseNode == null) {
                    baseNode = new nodePackageDec(JavaParserTreeConstants.JJTNODEPACKAGEDEC);
                }
                SimpleNode.vsymbolTable = new Vector();
                for (int i = 0; i < JavaParserConstants.tokenImage.length; i++) {
                    String im = JavaParserConstants.tokenImage[i];
                    if (im.charAt(0) == '\"') im = im.substring(1, im.length() - 1);
                    SimpleNode.vsymbolTable.addElement(im);
                }
                baseNode.loadfromDom(root, SimpleNode.vsymbolTable);
                symbolTable.table = new String[SimpleNode.vsymbolTable.size()];
                SimpleNode.vsymbolTable.toArray(symbolTable.table);
            }
            if (encTree != null) encTree.setModel(baseNode);
            dataChanged();
        } catch (Exception e) {
            System.out.println("xes.loadXesSource " + e);
        }
    }
