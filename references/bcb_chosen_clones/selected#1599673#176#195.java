    private boolean confirmAndModify(MDPRArchiveAccessor archiveAccessor) {
        String candidateBackupName = archiveAccessor.getArchiveFileName() + ".old";
        String backupName = createUniqueFileName(candidateBackupName);
        MessageFormat format = new MessageFormat(AUTO_MOD_MESSAGE);
        String message = format.format(new String[] { backupName });
        boolean ok = MessageDialog.openQuestion(new Shell(Display.getDefault()), AUTO_MOD_TITLE, message);
        if (ok) {
            File orig = new File(archiveAccessor.getArchiveFileName());
            try {
                IOUtils.copyFiles(orig, new File(backupName));
                DeviceRepositoryAccessorManager dram = new DeviceRepositoryAccessorManager(archiveAccessor, new ODOMFactory());
                dram.writeRepository();
            } catch (IOException e) {
                EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            } catch (RepositoryException e) {
                EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            }
        }
        return ok;
    }
