    private void writeResponse(final Collection<? extends Resource> resources, final HttpServletResponse response) throws IOException {
        for (final Resource resource : resources) {
            InputStream in = null;
            try {
                in = resource.getInputStream();
                final OutputStream out = response.getOutputStream();
                final long bytesCopied = IOUtils.copyLarge(in, out);
                if (bytesCopied < 0L) throw new StreamCorruptedException("Bad number of copied bytes (" + bytesCopied + ") for resource=" + resource.getFilename());
                if (logger.isDebugEnabled()) logger.debug("writeResponse(" + resource.getFile() + ") copied " + bytesCopied + " bytes");
            } finally {
                if (in != null) in.close();
            }
        }
    }
