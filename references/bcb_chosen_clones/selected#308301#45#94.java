    @Override
    protected IStatus run(IProgressMonitor monitor) {
        final int BUFFER_SIZE = 1024;
        final int DISPLAY_BUFFER_SIZE = 8196;
        File sourceFile = new File(_sourceFile);
        File destFile = new File(_destFile);
        if (sourceFile.exists()) {
            try {
                Log.getInstance(FileCopierJob.class).debug(String.format("Start copy of %s to %s", _sourceFile, _destFile));
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
                monitor.beginTask(Messages.getString("FileCopierJob.MainTask") + " " + _sourceFile, (int) ((sourceFile.length() / DISPLAY_BUFFER_SIZE) + 4));
                monitor.worked(1);
                byte[] buffer = new byte[BUFFER_SIZE];
                int stepRead = 0;
                int read;
                boolean copying = true;
                while (copying) {
                    read = bis.read(buffer);
                    if (read > 0) {
                        bos.write(buffer, 0, read);
                        stepRead += read;
                    } else {
                        copying = false;
                    }
                    if (monitor.isCanceled()) {
                        bos.close();
                        bis.close();
                        deleteFile(_destFile);
                        return Status.CANCEL_STATUS;
                    }
                    if (stepRead >= DISPLAY_BUFFER_SIZE) {
                        monitor.worked(1);
                        stepRead = 0;
                    }
                }
                bos.flush();
                bos.close();
                bis.close();
                monitor.worked(1);
            } catch (Exception e) {
                processError("Error while copying: " + e.getMessage());
            }
            Log.getInstance(FileCopierJob.class).debug("End of copy.");
            return Status.OK_STATUS;
        } else {
            processError(Messages.getString("FileCopierJob.ErrorSourceDontExists") + sourceFile.getAbsolutePath());
            return Status.CANCEL_STATUS;
        }
    }
