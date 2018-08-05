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
            char[] buffer = new char[8 * 1024];
            int counter = 0;
            while (true) {
                DequePayload payload = null;
                try {
                    payload = deque.takeFirst();
                } catch (InterruptedException ie) {
                }
                if (payload == null) break;
                IFaucet faucet = payload.faucet;
                Reader reader = payload.reader;
                if (logger.isTraceEnabled()) logger.trace("Removed reader: " + reader + " from the deque.");
                if (reader == null) break;
                if (logger.isTraceEnabled()) logger.trace("Using the reader " + reader + " in the mux sink");
                ZipEntry entry = new ZipEntry("entry" + counter++);
                outputStream.putNextEntry(entry);
                while ((count = reader.read(buffer)) != -1) {
                    String readInput = new String(buffer, 0, count);
                    if (logger.isTraceEnabled()) {
                        logger.trace("Read " + readInput + " from reader " + reader);
                    }
                    byte[] actualBytes = readInput.getBytes(charset);
                    outputStream.write(actualBytes, 0, actualBytes.length);
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
