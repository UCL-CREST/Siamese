    public void getFile(OutputStream output, Fragment fragment) throws Exception {
        Assert.Arg.notNull(output, "output");
        Assert.Arg.notNull(fragment, "fragment");
        Assert.Arg.notNull(fragment.getId(), "fragment.getId()");
        if (this.delegate != null) {
            this.delegate.getFile(output, fragment);
            return;
        }
        ensureBaseDirectoryCreated();
        File filePath = getFragmentFilePath(fragment);
        InputStream input = FileUtils.openInputStream(filePath);
        try {
            IOUtils.copyLarge(input, output);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }
