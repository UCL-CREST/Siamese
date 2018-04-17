    private static void writeOneAttachment(Context context, Writer writer, OutputStream out, Attachment attachment) throws IOException, MessagingException {
        writeHeader(writer, "Content-Type", attachment.mMimeType + ";\n name=\"" + attachment.mFileName + "\"");
        writeHeader(writer, "Content-Transfer-Encoding", "base64");
        writeHeader(writer, "Content-Disposition", "attachment;" + "\n filename=\"" + attachment.mFileName + "\";" + "\n size=" + Long.toString(attachment.mSize));
        writeHeader(writer, "Content-ID", attachment.mContentId);
        writer.append("\r\n");
        InputStream inStream = null;
        try {
            Uri fileUri = Uri.parse(attachment.mContentUri);
            inStream = context.getContentResolver().openInputStream(fileUri);
            writer.flush();
            Base64OutputStream base64Out = new Base64OutputStream(out);
            IOUtils.copy(inStream, base64Out);
            base64Out.close();
        } catch (FileNotFoundException fnfe) {
        } catch (IOException ioe) {
            throw new MessagingException("Invalid attachment.", ioe);
        }
    }
