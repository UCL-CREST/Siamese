    protected Document makeDocument(String Filename, File f, InputStream in) {
        if (Filename == null || in == null) return null;
        String[] splitStr = Filename.split("\\.");
        String ext = splitStr[splitStr.length - 1].toLowerCase();
        Class reader = extension_DocumentClass.get(ext);
        Document rtr = null;
        if (reader == null) {
            reader = extension_DocumentClass.get("|DEFAULT|");
        }
        if (reader == null) {
            logger.warn("No available parser for file " + Filename + ", file is ignored.");
            return null;
        }
        logger.debug("Using " + reader.getName() + " to read " + Filename);
        try {
            Class[] params = { File.class, InputStream.class };
            Object[] params2 = { f, in };
            rtr = (Document) (reader.getConstructor(params).newInstance(params2));
            indexedFiles.add(thisFilename);
        } catch (OutOfMemoryError e) {
            logger.warn("Problem instantiating a document class; Out of memory error occured: ", e);
            System.gc();
        } catch (StackOverflowError e) {
            logger.warn("Problem instantiating a document class; Stack Overflow error occured: ", e);
        } catch (Exception e) {
            logger.warn("Problem instantiating a document class: ", e);
        }
        return rtr;
    }
