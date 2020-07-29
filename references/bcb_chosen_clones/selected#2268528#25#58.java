    public void logging() throws Fault {
        final InterceptorWrapper wrap = new InterceptorWrapper(message);
        final LoggingMessage buffer = new LoggingMessage("Inbound Message\n----------------------------");
        String encoding = (String) wrap.getEncoding();
        if (encoding != null) {
            buffer.getEncoding().append(encoding);
        }
        Object headers = wrap.getProtocolHeaders();
        if (headers != null) {
            buffer.getHeader().append(headers);
        }
        InputStream is = (InputStream) wrap.getContent(InputStream.class);
        if (is != null) {
            CachedOutputStream bos = new CachedOutputStream();
            try {
                IOUtils.copy(is, bos);
                bos.flush();
                is.close();
                this.message.setContent(InputStream.class, bos.getInputStream());
                if (bos.getTempFile() != null) {
                    logger.error("\nMessage (saved to tmp file):\n");
                    logger.error("Filename: " + bos.getTempFile().getAbsolutePath() + "\n");
                }
                if (bos.size() > limit) {
                    logger.error("(message truncated to " + limit + " bytes)\n");
                }
                bos.writeCacheTo(buffer.getPayload(), limit);
                bos.close();
            } catch (IOException e) {
                throw new Fault(e);
            }
        }
        logger.debug("Message received :\n" + buffer.getPayload().toString());
    }
