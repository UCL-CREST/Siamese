    public static void showOpenFileDialog(TabbedView view) {
        JFileChooser loadDialog = new JFileChooser(view.getDocumentPanel().getDOMAdapter().getFile());
        Vector extentionList = new Vector();
        extentionList.add(new String("xml"));
        CustomFileFilter firstFilter = new CustomFileFilter(extentionList, "XML Documents");
        loadDialog.addChoosableFileFilter(firstFilter);
        extentionList = new Vector();
        extentionList.add(new String("xsl"));
        loadDialog.addChoosableFileFilter(new CustomFileFilter(extentionList, "XSL Stylesheets"));
        extentionList = new Vector();
        extentionList.add(new String("fo"));
        loadDialog.addChoosableFileFilter(new CustomFileFilter(extentionList, "XSL:FO Documents"));
        extentionList = new Vector();
        extentionList.add(new String("xml"));
        extentionList.add(new String("xsl"));
        extentionList.add(new String("fo"));
        loadDialog.addChoosableFileFilter(new CustomFileFilter(extentionList, "All XML Documents"));
        FileFilter all = loadDialog.getAcceptAllFileFilter();
        loadDialog.removeChoosableFileFilter(all);
        loadDialog.addChoosableFileFilter(all);
        loadDialog.setFileFilter(firstFilter);
        int returnVal = loadDialog.showOpenDialog(view);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            openXMLDocument(view, loadDialog.getSelectedFile());
        }
    }
