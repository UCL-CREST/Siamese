    public MotixFileItem(final InputStream is, final String name, final String contentType, final int index) throws IOException {
        this.name = name;
        this.contentType = contentType;
        this.index = index;
        this.extension = FilenameUtils.getExtension(this.name);
        this.isImage = ImageUtils.isImage(name);
        ArrayInputStream isAux = null;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            IOUtils.copy(is, out);
            isAux = new ArrayInputStream(out.toByteArray());
            if (this.isImage) {
                this.bufferedImage = imaging.read(isAux);
            }
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(isAux);
        }
        this.inputStream = new ArrayInputStream(out.toByteArray());
    }
