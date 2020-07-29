    public void includeCss(Group group, Writer out, PageContext pageContext) throws IOException {
        ByteArrayOutputStream outtmp = new ByteArrayOutputStream();
        if (AbstractGroupBuilder.getInstance().buildGroupJsIfNeeded(group, outtmp, pageContext.getServletContext())) {
            FileOutputStream fileStream = null;
            try {
                fileStream = new FileOutputStream(new File(RetentionHelper.buildFullRetentionFilePath(group, ".css")));
                IOUtils.copy(new ByteArrayInputStream(outtmp.toByteArray()), fileStream);
            } finally {
                if (fileStream != null) fileStream.close();
            }
        }
    }
