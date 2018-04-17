    public void load(boolean isOrdered) throws ResourceInstantiationException {
        try {
            if (null == url) {
                throw new ResourceInstantiationException("URL not specified (null).");
            }
            BufferedReader listReader;
            listReader = new BomStrippingInputStreamReader((url).openStream(), encoding);
            String line;
            int linenr = 0;
            while (null != (line = listReader.readLine())) {
                linenr++;
                GazetteerNode node = null;
                try {
                    node = new GazetteerNode(line, separator, isOrdered);
                } catch (Exception ex) {
                    throw new GateRuntimeException("Could not read gazetteer entry " + linenr + " from URL " + getURL() + ": " + ex.getMessage(), ex);
                }
                entries.add(new GazetteerNode(line, separator, isOrdered));
            }
            listReader.close();
        } catch (Exception x) {
            throw new ResourceInstantiationException(x.getClass() + ":" + x.getMessage());
        }
        isModified = false;
    }
