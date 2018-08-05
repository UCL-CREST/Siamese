    public Resource createNew(String name, InputStream in, Long length, String contentType) throws IOException {
        File dest = new File(this.getRealFile(), name);
        LOGGER.debug("PUT?? - real file: " + this.getRealFile() + ",name: " + name);
        if (isOwner) {
            if (!".request".equals(name) && !".tokens".equals(name)) {
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(dest);
                    IOUtils.copy(in, out);
                } finally {
                    IOUtils.closeQuietly(out);
                }
            } else {
                if (ServerConfiguration.isDynamicSEL()) {
                } else {
                }
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(dest);
                    IOUtils.copy(in, out);
                } finally {
                    IOUtils.closeQuietly(out);
                }
            }
            return factory.resolveFile(this.host, dest);
        } else {
            LOGGER.error("User isn't owner of this folder");
            return null;
        }
    }
