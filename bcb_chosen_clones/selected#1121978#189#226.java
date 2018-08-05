    private void buildBundle() {
        if (targetProject == null) {
            MessageDialog.openError(getShell(), "Error", "No target SPAB project selected!");
            return;
        }
        if (projectProcessDirSelector.getText().trim().length() == 0) {
            MessageDialog.openError(getShell(), "Error", "No process directory selected for project " + targetProject.getName() + "!");
            return;
        }
        deleteBundleFile();
        try {
            File projectDir = targetProject.getLocation().toFile();
            File projectProcessesDir = new File(projectDir, projectProcessDirSelector.getText());
            boolean bpmoCopied = IOUtils.copyProcessFilesSecure(getBPMOFile(), projectProcessesDir);
            boolean sbpelCopied = IOUtils.copyProcessFilesSecure(getSBPELFile(), projectProcessesDir);
            boolean xmlCopied = IOUtils.copyProcessFilesSecure(getBPEL4SWSFile(), projectProcessesDir);
            bundleFile = IOUtils.archiveBundle(projectDir, Activator.getDefault().getStateLocation().toFile());
            if (bpmoCopied) {
                new File(projectProcessesDir, getBPMOFile().getName()).delete();
            }
            if (sbpelCopied) {
                new File(projectProcessesDir, getSBPELFile().getName()).delete();
            }
            if (xmlCopied) {
                new File(projectProcessesDir, getBPEL4SWSFile().getName()).delete();
            }
        } catch (Throwable anyError) {
            LogManager.logError(anyError);
            MessageDialog.openError(getShell(), "Error", "Error building SPAB :\n" + anyError.getMessage());
            updateBundleUI();
            return;
        }
        bpmoFile = getBPMOFile();
        sbpelFile = getSBPELFile();
        xmlFile = getBPEL4SWSFile();
        updateBundleUI();
        getWizard().getContainer().updateButtons();
    }
