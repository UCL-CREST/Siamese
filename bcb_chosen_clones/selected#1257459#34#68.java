    static CertificateRequest read(InputStream in) throws IOException {
        DataInputStream din = new DataInputStream(in);
        ClientType[] types = new ClientType[din.readUnsignedByte()];
        for (int i = 0; i < types.length; i++) {
            types[i] = ClientType.read(din);
        }
        LinkedList authorities = new LinkedList();
        byte[] buf = new byte[din.readUnsignedShort()];
        din.readFully(buf);
        ByteArrayInputStream bin = new ByteArrayInputStream(buf);
        try {
            String x500name = Util.getSecurityProperty("jessie.x500.class");
            if (x500name == null) {
                x500name = "org.metastatic.jessie.pki.X500Name";
            }
            Class x500class = null;
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            if (cl != null) {
                x500class = cl.loadClass(x500name);
            } else {
                x500class = Class.forName(x500name);
            }
            Constructor c = x500class.getConstructor(new Class[] { new byte[0].getClass() });
            while (bin.available() > 0) {
                buf = new byte[(bin.read() & 0xFF) << 8 | (bin.read() & 0xFF)];
                bin.read(buf);
                authorities.add(c.newInstance(new Object[] { buf }));
            }
        } catch (IOException ioe) {
            throw ioe;
        } catch (Exception ex) {
            throw new Error(ex.toString());
        }
        return new CertificateRequest(types, (Principal[]) authorities.toArray(new Principal[authorities.size()]));
    }
