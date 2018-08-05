    public void uncaughtException(final Thread t, final Throwable e) {
        final Display display = Display.getCurrent();
        final Shell shell = new Shell(display);
        final MessageBox message = new MessageBox(shell, SWT.OK | SWT.CANCEL | SWT.ICON_ERROR);
        message.setText("Hawkscope Error");
        message.setMessage(e.getMessage() + "\nSubmit Hawkscope Error Report to Issue Tracker?");
        log.error("Uncaught exception", e);
        if (message.open() == SWT.OK) {
            IOUtils.copyToClipboard(Version.getBugReport(e));
            try {
                Program.launch(Constants.HAWKSCOPE_URL_ROOT + "issues/entry?comment=" + URLEncoder.encode("Please paste the Hawkscope Error " + "Report here. It's currently copied to your " + "clipboard. Thank you for your support!", Constants.ENCODING));
            } catch (final Exception e1) {
                Program.launch(Constants.HAWKSCOPE_URL_ROOT + "issues/entry");
            }
        }
        shell.dispose();
    }
