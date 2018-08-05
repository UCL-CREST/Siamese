    private Attachment setupSimpleAttachment(Context context, long messageId, boolean withBody) throws IOException {
        Attachment attachment = new Attachment();
        attachment.mFileName = "the file.jpg";
        attachment.mMimeType = "image/jpg";
        attachment.mSize = 0;
        attachment.mContentId = null;
        attachment.mContentUri = "content://com.android.email/1/1";
        attachment.mMessageKey = messageId;
        attachment.mLocation = null;
        attachment.mEncoding = null;
        if (withBody) {
            InputStream inStream = new ByteArrayInputStream(TEST_STRING.getBytes());
            File cacheDir = context.getCacheDir();
            File tmpFile = File.createTempFile("setupSimpleAttachment", "tmp", cacheDir);
            OutputStream outStream = new FileOutputStream(tmpFile);
            IOUtils.copy(inStream, outStream);
            attachment.mContentUri = "file://" + tmpFile.getAbsolutePath();
        }
        return attachment;
    }
