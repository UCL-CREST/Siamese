    public static void saveAttachmentBody(Context context, Part part, Attachment localAttachment, long accountId) throws MessagingException, IOException {
        if (part.getBody() != null) {
            long attachmentId = localAttachment.mId;
            InputStream in = part.getBody().getInputStream();
            File saveIn = AttachmentProvider.getAttachmentDirectory(context, accountId);
            if (!saveIn.exists()) {
                saveIn.mkdirs();
            }
            File saveAs = AttachmentProvider.getAttachmentFilename(context, accountId, attachmentId);
            saveAs.createNewFile();
            FileOutputStream out = new FileOutputStream(saveAs);
            long copySize = IOUtils.copy(in, out);
            in.close();
            out.close();
            String contentUriString = AttachmentProvider.getAttachmentUri(accountId, attachmentId).toString();
            localAttachment.mSize = copySize;
            localAttachment.mContentUri = contentUriString;
            ContentValues cv = new ContentValues();
            cv.put(AttachmentColumns.SIZE, copySize);
            cv.put(AttachmentColumns.CONTENT_URI, contentUriString);
            Uri uri = ContentUris.withAppendedId(Attachment.CONTENT_URI, attachmentId);
            context.getContentResolver().update(uri, cv, null, null);
        }
    }
