    public void createPartControl(Composite parent) {
        viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.setContentProvider(new FileContentProvider());
        viewer.setLabelProvider(new FileLabelProvider());
        viewer.setInput(File.listRoots());
        viewer.addOpenListener(new IOpenListener() {

            @Override
            public void open(OpenEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                File file = (File) selection.getFirstElement();
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.OPEN)) {
                        try {
                            desktop.open(file);
                        } catch (IOException e) {
                        }
                    }
                }
            }
        });
    }
