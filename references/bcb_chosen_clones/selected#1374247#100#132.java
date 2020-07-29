    public void removeExifTag(File jpegImageFile, File dst) throws IOException, ImageReadException, ImageWriteException {
        OutputStream os = null;
        try {
            TiffOutputSet outputSet = null;
            IImageMetadata metadata = Sanselan.getMetadata(jpegImageFile);
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            if (null != jpegMetadata) {
                TiffImageMetadata exif = jpegMetadata.getExif();
                if (null != exif) {
                    outputSet = exif.getOutputSet();
                }
            }
            if (null == outputSet) {
                IOUtils.copyFileNio(jpegImageFile, dst);
                return;
            }
            {
                outputSet.removeField(TiffConstants.EXIF_TAG_APERTURE_VALUE);
                TiffOutputDirectory exifDirectory = outputSet.getExifDirectory();
                if (null != exifDirectory) exifDirectory.removeField(TiffConstants.EXIF_TAG_APERTURE_VALUE);
            }
            os = new FileOutputStream(dst);
            os = new BufferedOutputStream(os);
            new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os, outputSet);
            os.close();
            os = null;
        } finally {
            if (os != null) try {
                os.close();
            } catch (IOException e) {
            }
        }
    }
