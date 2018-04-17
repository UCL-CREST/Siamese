    public static String getTextFromPart(Part part) {
        try {
            if (part != null && part.getBody() != null) {
                InputStream in = part.getBody().getInputStream();
                String mimeType = part.getMimeType();
                if (mimeType != null && MimeUtility.mimeTypeMatches(mimeType, "text/*")) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    IOUtils.copy(in, out);
                    in.close();
                    in = null;
                    String charset = getHeaderParameter(part.getContentType(), "charset");
                    if (charset != null) {
                        charset = CharsetUtil.toJavaCharset(charset);
                    }
                    if (charset == null) {
                        charset = "ASCII";
                    }
                    String result = out.toString(charset);
                    out.close();
                    return result;
                }
            }
        } catch (OutOfMemoryError oom) {
            Log.e(Email.LOG_TAG, "Unable to getTextFromPart " + oom.toString());
        } catch (Exception e) {
            Log.e(Email.LOG_TAG, "Unable to getTextFromPart " + e.toString());
        }
        return null;
    }
