    private void doFinishLoadAttachment(long attachmentId) {
        if (attachmentId != mLoadAttachmentId) {
            return;
        }
        Attachment attachment = Attachment.restoreAttachmentWithId(MessageView.this, attachmentId);
        Uri attachmentUri = AttachmentProvider.getAttachmentUri(mAccountId, attachment.mId);
        Uri contentUri = AttachmentProvider.resolveAttachmentIdToContentUri(getContentResolver(), attachmentUri);
        if (mLoadAttachmentSave) {
            try {
                File file = createUniqueFile(Environment.getExternalStorageDirectory(), attachment.mFileName);
                InputStream in = getContentResolver().openInputStream(contentUri);
                OutputStream out = new FileOutputStream(file);
                IOUtils.copy(in, out);
                out.flush();
                out.close();
                in.close();
                Toast.makeText(MessageView.this, String.format(getString(R.string.message_view_status_attachment_saved), file.getName()), Toast.LENGTH_LONG).show();
                new MediaScannerNotifier(this, file, mHandler);
            } catch (IOException ioe) {
                Toast.makeText(MessageView.this, getString(R.string.message_view_status_attachment_not_saved), Toast.LENGTH_LONG).show();
            }
        } else {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(contentUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                mHandler.attachmentViewError();
            }
        }
    }
