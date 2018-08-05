    @Override
    public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
        monitor.beginTask("Archiving " + _root.getName(), WORK_UNITS + 1);
        Collection<IResource> resources = getResources(_root, monitor);
        IStatus returnStatus = Status.OK_STATUS;
        int lastTotal = 0;
        int currentTotal = 0;
        try {
            IPath strip = _root.getFullPath().removeLastSegments(1);
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(_archive.getLocation().toFile())));
            byte[] block = new byte[4096];
            float count = 0;
            for (IResource resource : resources) {
                IPath path = resource.getFullPath();
                if (strip.isPrefixOf(path)) path = path.removeFirstSegments(strip.segmentCount());
                String name = path.toString();
                if (name.startsWith("/")) name = name.substring(1, name.length());
                if (resource instanceof IContainer) name += "/";
                monitor.setTaskName("Archiving " + name);
                ZipEntry entry = new ZipEntry(name);
                zos.putNextEntry(entry);
                if (resource instanceof IFile) {
                    IFile file = (IFile) resource;
                    InputStream is = file.getContents(true);
                    int read = 0;
                    int written = 0;
                    while ((read = is.read(block)) > 0) {
                        zos.write(block, 0, read);
                        written += read;
                    }
                    is.close();
                    if (LOGGER.isDebugEnabled()) LOGGER.debug(written + " bytes ");
                }
                zos.closeEntry();
                count++;
                currentTotal = (int) (WORK_UNITS * count / resources.size());
                if (currentTotal > lastTotal) {
                    monitor.worked(currentTotal - lastTotal);
                    lastTotal = currentTotal;
                }
            }
            zos.close();
            return returnStatus;
        } catch (IOException ioe) {
            if (_archive.exists()) _archive.delete(true, monitor);
            return new Status(IStatus.ERROR, UIPlugin.ID, "Failed to archive " + _root.getName(), ioe);
        } finally {
            if (returnStatus.isOK() && _deleteWhenDone) delete(monitor);
            _root.getParent().refreshLocal(IResource.DEPTH_INFINITE, monitor);
            monitor.done();
        }
    }
