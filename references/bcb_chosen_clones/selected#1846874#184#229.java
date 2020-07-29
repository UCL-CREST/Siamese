    @SuppressWarnings("unchecked")
    protected AbstractID3v2FrameBody readBody(String identifier, ByteBuffer byteBuffer, int frameSize) throws InvalidFrameException, InvalidDataTypeException {
        logger.finest("Creating framebody:start");
        AbstractID3v2FrameBody frameBody;
        try {
            Class<AbstractID3v2FrameBody> c = (Class<AbstractID3v2FrameBody>) Class.forName("org.jaudiotagger.tag.id3.framebody.FrameBody" + identifier);
            Class<?>[] constructorParameterTypes = { Class.forName("java.nio.ByteBuffer"), Integer.TYPE };
            Object[] constructorParameterValues = { byteBuffer, frameSize };
            Constructor<AbstractID3v2FrameBody> construct = c.getConstructor(constructorParameterTypes);
            frameBody = (construct.newInstance(constructorParameterValues));
        } catch (ClassNotFoundException cex) {
            logger.info(getLoggingFilename() + ":" + "Identifier not recognised:" + identifier + " using FrameBodyUnsupported");
            try {
                frameBody = new FrameBodyUnsupported(byteBuffer, frameSize);
            } catch (InvalidFrameException ife) {
                throw ife;
            } catch (InvalidTagException te) {
                throw new InvalidFrameException(te.getMessage());
            }
        } catch (InvocationTargetException ite) {
            logger.severe(getLoggingFilename() + ":" + "An error occurred within abstractID3v2FrameBody for identifier:" + identifier + ":" + ite.getCause().getMessage());
            if (ite.getCause() instanceof Error) {
                throw (Error) ite.getCause();
            } else if (ite.getCause() instanceof RuntimeException) {
                throw (RuntimeException) ite.getCause();
            } else if (ite.getCause() instanceof InvalidFrameException) {
                throw (InvalidFrameException) ite.getCause();
            } else if (ite.getCause() instanceof InvalidDataTypeException) {
                throw (InvalidDataTypeException) ite.getCause();
            } else {
                throw new InvalidFrameException(ite.getCause().getMessage());
            }
        } catch (NoSuchMethodException sme) {
            logger.log(Level.SEVERE, getLoggingFilename() + ":" + "No such method:" + sme.getMessage(), sme);
            throw new RuntimeException(sme.getMessage());
        } catch (InstantiationException ie) {
            logger.log(Level.SEVERE, getLoggingFilename() + ":" + "Instantiation exception:" + ie.getMessage(), ie);
            throw new RuntimeException(ie.getMessage());
        } catch (IllegalAccessException iae) {
            logger.log(Level.SEVERE, getLoggingFilename() + ":" + "Illegal access exception :" + iae.getMessage(), iae);
            throw new RuntimeException(iae.getMessage());
        }
        logger.finest(getLoggingFilename() + ":" + "Created framebody:end" + frameBody.getIdentifier());
        frameBody.setHeader(this);
        return frameBody;
    }
