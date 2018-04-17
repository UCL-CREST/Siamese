    @Override
    public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
        Collection<IResource> resources = getResources();
        monitor = new SubProgressMonitor(monitor, resources.size());
        File fp = _archiveFile.toFile();
        try {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Using " + fp.getAbsolutePath());
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(fp)));
            byte[] block = new byte[4096];
            for (IResource resource : resources) {
                IPath path = resource.getFullPath();
                if (_strip.isPrefixOf(path)) path = path.removeFirstSegments(_strip.segmentCount());
                String name = path.toString();
                if (resource instanceof IContainer) name += "/";
                if (name.startsWith("/")) name = name.substring(1, name.length());
                if (LOGGER.isDebugEnabled()) LOGGER.debug("Compressing " + name);
                monitor.subTask("Compression " + name);
                ZipEntry entry = new ZipEntry(name);
                zos.putNextEntry(entry);
                if (resource instanceof IFile) {
                    IFile file = (IFile) resource;
                    entry.setComment(Boolean.toString(file.getResourceAttributes().isExecutable()));
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
                monitor.worked(1);
            }
            zos.flush();
            zos.close();
        } catch (IOException ioe) {
            LOGGER.error("IOException, deleting file " + fp, ioe);
            if (fp != null) fp.delete();
            throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "IOException while compressing contents", ioe));
        } finally {
            monitor.done();
        }
        return Status.OK_STATUS;
    }
