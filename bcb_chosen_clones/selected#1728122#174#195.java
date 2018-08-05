    public void load() throws ResourceInstantiationException {
        if (null == url) {
            throw new ResourceInstantiationException("URL not set (null).");
        }
        try {
            BufferedReader defReader = new BomStrippingInputStreamReader((url).openStream(), ENCODING);
            String line;
            LinearNode node;
            while (null != (line = defReader.readLine())) {
                node = new LinearNode(line);
                try {
                    this.add(node);
                } catch (GateRuntimeException ex) {
                    throw new ResourceInstantiationException(ex);
                }
            }
            defReader.close();
            isModified = false;
        } catch (Exception x) {
            throw new ResourceInstantiationException(x);
        }
    }
