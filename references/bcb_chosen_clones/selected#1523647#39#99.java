    public Object execute(ExecutionEvent event) throws ExecutionException {
        final List<InformationUnit> informationUnitsFromExecutionEvent = InformationHandlerUtil.getInformationUnitsFromExecutionEvent(event);
        Shell activeShell = HandlerUtil.getActiveShell(event);
        DirectoryDialog fd = new DirectoryDialog(activeShell, SWT.SAVE);
        String section = Activator.getDefault().getDialogSettings().get("lastExportSection");
        fd.setFilterPath(section);
        final String open = fd.open();
        if (open != null) {
            Activator.getDefault().getDialogSettings().put("lastExportSection", open);
            CancelableRunnable runnable = new CancelableRunnable() {

                @Override
                protected IStatus runCancelableRunnable(IProgressMonitor monitor) {
                    IStatus returnValue = Status.OK_STATUS;
                    monitor.beginTask(NLS.bind(Messages.SaveFileOnDiskHandler_SavingFiles, open), informationUnitsFromExecutionEvent.size());
                    for (InformationUnit informationUnit : informationUnitsFromExecutionEvent) {
                        if (!monitor.isCanceled()) {
                            monitor.setTaskName(NLS.bind(Messages.SaveFileOnDiskHandler_Saving, informationUnit.getLabel()));
                            InformationStructureRead read = InformationStructureRead.newSession(informationUnit);
                            read.getValueByNodeId(Activator.FILENAME);
                            IFile binaryReferenceFile = InformationUtil.getBinaryReferenceFile(informationUnit);
                            FileWriter writer = null;
                            try {
                                if (binaryReferenceFile != null) {
                                    File file = new File(open, (String) read.getValueByNodeId(Activator.FILENAME));
                                    InputStream contents = binaryReferenceFile.getContents();
                                    writer = new FileWriter(file);
                                    IOUtils.copy(contents, writer);
                                    monitor.worked(1);
                                }
                            } catch (Exception e) {
                                returnValue = StatusCreator.newStatus(NLS.bind(Messages.SaveFileOnDiskHandler_ErrorSaving, informationUnit.getLabel(), e));
                                break;
                            } finally {
                                if (writer != null) {
                                    try {
                                        writer.flush();
                                        writer.close();
                                    } catch (IOException e) {
                                    }
                                }
                            }
                        }
                    }
                    return returnValue;
                }
            };
            ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(activeShell);
            try {
                progressMonitorDialog.run(true, true, runnable);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof CoreException) {
                    ErrorDialog.openError(activeShell, Messages.SaveFileOnDiskHandler_ErrorSaving2, Messages.SaveFileOnDiskHandler_ErrorSaving2, ((CoreException) e.getCause()).getStatus());
                } else {
                    ErrorDialog.openError(activeShell, Messages.SaveFileOnDiskHandler_ErrorSaving2, Messages.SaveFileOnDiskHandler_ErrorSaving2, StatusCreator.newStatus(Messages.SaveFileOnDiskHandler_ErrorSaving3, e));
                }
            } catch (InterruptedException e) {
            }
        }
        return null;
    }
