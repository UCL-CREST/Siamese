    public byte[] downloadAttachmentContent(Attachment issueAttachment) throws IOException {
        byte[] result = null;
        URL url = new URL(issueAttachment.getContentURL());
        BufferedReader inputReader = null;
        try {
            inputReader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = inputReader.readLine()) != null) {
                contentBuilder.append(line);
            }
            result = contentBuilder.toString().getBytes();
        } finally {
            if (inputReader != null) {
                inputReader.close();
            }
        }
        return result;
    }
