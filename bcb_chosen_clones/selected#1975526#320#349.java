    private static String getSummaryText(File packageFile) {
        String retVal = null;
        Reader in = null;
        try {
            in = new FileReader(packageFile);
            StringWriter out = new StringWriter();
            IOUtils.copy(in, out);
            StringBuffer buf = out.getBuffer();
            int pos1 = buf.indexOf("<body>");
            int pos2 = buf.lastIndexOf("</body>");
            if (pos1 >= 0 && pos1 < pos2) {
                retVal = buf.substring(pos1 + 6, pos2);
            } else {
                retVal = "";
            }
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage(), e);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        return retVal;
    }
