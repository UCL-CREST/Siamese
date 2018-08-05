    public void visit(BosMember member) throws BosException {
        String relative = AddressingUtil.getRelativePath(member.getDataSourceUri(), baseUri);
        URL resultUrl;
        try {
            resultUrl = new URL(outputUrl, relative);
            File resultFile = new File(resultUrl.toURI());
            resultFile.getParentFile().mkdirs();
            log.info("Creating result file \"" + resultFile.getAbsolutePath() + "\"...");
            IOUtils.copy(member.getInputStream(), new FileOutputStream(resultFile));
        } catch (Exception e) {
            throw new BosException(e);
        }
    }
