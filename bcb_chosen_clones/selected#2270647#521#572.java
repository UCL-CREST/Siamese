    private void showLog() {
        String logFileDestination;
        try {
            logFileDestination = ((RollingFileAppender) org.apache.log4j.LogManager.getRootLogger().getAppender("FILE")).getFile();
        } catch (Exception e) {
            showErrorDialog(Messages.getString("GuiController.8"));
            return;
        }
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(new File(logFileDestination));
                return;
            } catch (IOException e) {
                log.error(e, e);
            }
        }
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(logFileDestination)));
            try {
                while (bufferedReader.ready()) {
                    text.append(bufferedReader.readLine()).append("\n");
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            } finally {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    log.error(e, e);
                }
            }
        } catch (FileNotFoundException e) {
            log.error(e, e);
        }
        JInternalFrame logwindow = new JInternalFrame(Messages.getString("MainMenuBar.0"));
        logwindow.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        logwindow.setPreferredSize(new Dimension(900, 600));
        logwindow.setClosable(true);
        logwindow.setResizable(true);
        logwindow.setMaximizable(true);
        logwindow.setIconifiable(true);
        JScrollPane scrollPane = new JScrollPane();
        JTextArea textArea = new JTextArea(text.toString());
        scrollPane.setViewportView(textArea);
        logwindow.setContentPane(scrollPane);
        logwindow.pack();
        textArea.setCaretPosition(textArea.getDocument().getLength() - 1);
        ossobookFrame.getDesktop().add(logwindow);
        logwindow.setVisible(true);
        logwindow.moveToFront();
    }
