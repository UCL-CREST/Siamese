    public byte[] read(IFile input) {
        InputStream contents = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            contents = input.getContents();
            IOUtils.copy(contents, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            Activator.logUnexpected(null, e);
        } catch (CoreException e) {
            Activator.logUnexpected(null, e);
        } finally {
            IOUtils.closeQuietly(contents);
        }
        return null;
    }
