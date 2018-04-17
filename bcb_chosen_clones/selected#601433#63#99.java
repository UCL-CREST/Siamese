    public ISOMsg filter(ISOChannel channel, ISOMsg m, LogEvent evt) throws VetoException {
        if (key == null || fields == null) throw new VetoException("MD5Filter not configured");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(getKey());
            int[] f = getFields(m);
            for (int i = 0; i < f.length; i++) {
                int fld = f[i];
                if (m.hasField(fld)) {
                    ISOComponent c = m.getComponent(fld);
                    if (c instanceof ISOBinaryField) md.update((byte[]) c.getValue()); else md.update(((String) c.getValue()).getBytes());
                }
            }
            byte[] digest = md.digest();
            if (m.getDirection() == ISOMsg.OUTGOING) {
                m.set(new ISOBinaryField(64, digest, 0, 8));
                m.set(new ISOBinaryField(128, digest, 8, 8));
            } else {
                byte[] rxDigest = new byte[16];
                if (m.hasField(64)) System.arraycopy((byte[]) m.getValue(64), 0, rxDigest, 0, 8);
                if (m.hasField(128)) System.arraycopy((byte[]) m.getValue(128), 0, rxDigest, 8, 8);
                if (!Arrays.equals(digest, rxDigest)) {
                    evt.addMessage(m);
                    evt.addMessage("MAC expected: " + ISOUtil.hexString(digest));
                    evt.addMessage("MAC received: " + ISOUtil.hexString(rxDigest));
                    throw new VetoException("invalid MAC");
                }
                m.unset(64);
                m.unset(128);
            }
        } catch (NoSuchAlgorithmException e) {
            throw new VetoException(e);
        } catch (ISOException e) {
            throw new VetoException(e);
        }
        return m;
    }
