    public String getTextData() {
        if (tempFileWriter != null) {
            try {
                tempFileWriter.flush();
                tempFileWriter.close();
                FileReader in = new FileReader(tempFile);
                StringWriter out = new StringWriter();
                int len;
                char[] buf = new char[BUFSIZ];
                while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
                out.close();
                in.close();
                return out.toString();
            } catch (IOException ioe) {
                Logger.instance().log(Logger.ERROR, LOGGER_PREFIX, "XMLTextData.getTextData", ioe);
                return "";
            }
        } else if (textBuffer != null) return textBuffer.toString(); else return null;
    }
