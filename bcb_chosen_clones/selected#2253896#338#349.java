    private void createButtonCopyToClipboard() {
        buttonCopyToClipboard = new Button(shell, SWT.PUSH);
        buttonCopyToClipboard.setText("Co&py to Clipboard");
        buttonCopyToClipboard.setLayoutData(SharedStyle.relativeToBottomRight(buttonClose));
        buttonCopyToClipboard.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent event) {
                IOUtils.copyToClipboard(Version.getEnvironmentReport());
            }
        });
    }
