    @Override
    protected void write(InputStream in, OutputStream out, javax.sound.sampled.AudioFormat javaSoundFormat) throws IOException {
        if (USE_JAVASOUND) {
            super.write(in, out, javaSoundFormat);
        } else {
            try {
                byte[] header = JavaSoundCodec.createWavHeader(javaSoundFormat);
                if (header == null) throw new IOException("Unable to create wav header");
                out.write(header);
                IOUtils.copyStream(in, out);
            } catch (InterruptedIOException e) {
                logger.log(Level.FINE, "" + e, e);
                throw e;
            } catch (IOException e) {
                logger.log(Level.WARNING, "" + e, e);
                throw e;
            }
        }
    }
