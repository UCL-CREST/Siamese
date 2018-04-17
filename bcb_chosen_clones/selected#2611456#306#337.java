    private void maybeOpenFile(File file) {
        UIPropertyContext context = UIPropertyContext.getInstance();
        String value = context.getProperty(UIPropertyContext.ALWAYS_OPEN_EXPORT_FILE);
        Boolean openFile = StringUtils.isEmpty(value) ? null : Boolean.valueOf(value);
        if (openFile == null) {
            JCheckBox checkbox = new JCheckBox();
            checkbox.setText("Always perform this action");
            JPanel message = PCGenFrame.buildMessageLabelPanel("Do you want to open " + file.getName() + "?", checkbox);
            int ret = JOptionPane.showConfirmDialog(this, message, "Select an Option", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (ret == JOptionPane.CLOSED_OPTION) {
                return;
            }
            openFile = BooleanUtils.toBoolean(ret, JOptionPane.YES_OPTION, JOptionPane.NO_OPTION);
            if (checkbox.isSelected()) {
                context.setBoolean(UIPropertyContext.ALWAYS_OPEN_EXPORT_FILE, openFile);
            }
        }
        if (!openFile) {
            return;
        }
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            pcgenFrame.showErrorMessage("Cannot Open " + file.getName(), "Operating System does not support this operation");
            return;
        }
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            String message = "Failed to open " + file.getName();
            pcgenFrame.showErrorMessage(Constants.APPLICATION_NAME, message);
            Logging.errorPrint(message, ex);
        }
    }
