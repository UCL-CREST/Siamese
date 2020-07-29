    @Override
    protected void mouseDoubleClicked(final MouseEvent e) {
        if (logger.isTraceEnabled()) logger.trace("Double click");
        final JTree tree = (JTree) e.getSource();
        final TreeSelectionModel model = tree.getSelectionModel();
        for (final TreePath path : model.getSelectionPaths()) {
            final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
            final SortedFile sortedFile = (SortedFile) selectedNode.getUserObject();
            final File srcFile = sortedFile.getSrcFile();
            final File dstFile = sortedFile.getDstFile();
            if (Desktop.isDesktopSupported()) {
                try {
                    if (isSrcTree) {
                        if (srcFile.exists()) Desktop.getDesktop().open(srcFile); else logger.warn(MessageFormat.format(FILE_NOT_FOUND_ERR_MSG, srcFile.getAbsolutePath()));
                    } else {
                        if (dstFile.exists()) Desktop.getDesktop().open(dstFile); else logger.warn(MessageFormat.format(FILE_NOT_FOUND_ERR_MSG, dstFile.getAbsolutePath()));
                    }
                } catch (final Exception ex) {
                    logger.error("Exception : " + ex.getMessage() + (ex.getCause() != null ? ", cause : " + ex.getCause() : ""), ex);
                }
            } else logger.warn(MessageFormat.format(UNSUPPORTED_API_ERR_MSG, SystemUtils.OS_NAME));
        }
        if (tree.isCollapsed(0)) SwingUtils.expandAll(tree);
    }
