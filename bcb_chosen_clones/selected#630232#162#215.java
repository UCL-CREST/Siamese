    @Override
    public void consume() {
        if (!hookedUp && faucetTemplate == null) throw new XformationException("Sink has not been set up correctly: " + "faucet has not been set");
        switch(type) {
            case File:
                try {
                    outputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
                } catch (FileNotFoundException e) {
                    throw new XformationException("Unable to create output stream", e);
                }
                break;
            case Stream:
                break;
        }
        if (!hookedUp) {
            if (faucetTemplate instanceof IPipelineItem) {
                ((IPipelineItem) faucetTemplate).consume(this);
            }
        }
        try {
            int count;
            byte[] buffer = new byte[8 * 1024];
            int counter = 0;
            while (true) {
                DequePayload payload = null;
                try {
                    payload = deque.takeFirst();
                } catch (InterruptedException ie) {
                }
                if (payload == null) break;
                IFaucet faucet = payload.faucet;
                InputStream inputStream = payload.inputStream;
                if (logger.isTraceEnabled()) logger.trace("Removed input stream: " + inputStream + " from the deque.");
                if (inputStream == null) break;
                if (logger.isTraceEnabled()) logger.trace("Using the reader " + inputStream + " in the mux sink");
                ZipEntry entry = new ZipEntry("entry" + counter++);
                outputStream.putNextEntry(entry);
                while ((count = inputStream.read(buffer)) != -1) {
                    if (logger.isTraceEnabled()) {
                        String readInput = ByteArrayUtil.getHexDump(buffer, 0, count);
                        logger.trace("Read " + readInput + " from input stream " + inputStream);
                    }
                    outputStream.write(buffer, 0, count);
                }
                outputStream.closeEntry();
                faucet.dispose();
            }
            outputStream.close();
            faucetTemplate.dispose();
        } catch (IOException ioe) {
            logger.error("Error while consuming input", ioe);
            throw new XformationException("Unable to transform stream", ioe);
        }
    }
