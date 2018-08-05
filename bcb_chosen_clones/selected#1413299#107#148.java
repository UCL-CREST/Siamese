    public String getSummaryText() {
        if (summaryText == null) {
            for (Iterator iter = xdcSources.values().iterator(); iter.hasNext(); ) {
                XdcSource source = (XdcSource) iter.next();
                File packageFile = new File(source.getFile().getParentFile(), "xdc-package.html");
                if (packageFile.exists()) {
                    Reader in = null;
                    try {
                        in = new FileReader(packageFile);
                        StringWriter out = new StringWriter();
                        IOUtils.copy(in, out);
                        StringBuffer buf = out.getBuffer();
                        int pos1 = buf.indexOf("<body>");
                        int pos2 = buf.lastIndexOf("</body>");
                        if (pos1 >= 0 && pos1 < pos2) {
                            summaryText = buf.substring(pos1 + 6, pos2);
                        } else {
                            summaryText = "";
                        }
                    } catch (FileNotFoundException e) {
                        LOG.error(e.getMessage(), e);
                        summaryText = "";
                    } catch (IOException e) {
                        LOG.error(e.getMessage(), e);
                        summaryText = "";
                    } finally {
                        if (in != null) {
                            try {
                                in.close();
                            } catch (IOException e) {
                                LOG.error(e.getMessage(), e);
                            }
                        }
                    }
                    break;
                } else {
                    summaryText = "";
                }
            }
        }
        return summaryText;
    }
