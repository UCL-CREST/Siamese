    private byte[] doSerializeObject(Object obj) throws SerializationException {
        _baos.reset();
        _outs = _baos;
        writeObject(obj);
        if (_def_level != null) {
            try {
                byte[] data = _baos.toByteArray();
                ByteArrayOutputStream baos = new ByteArrayOutputStream(_baos.size());
                DeflaterOutputStream defs = new DeflaterOutputStream(baos, new Deflater(_def_level));
                defs.write(data, 0, data.length);
                defs.flush();
                defs.finish();
                defs.close();
                baos.flush();
                _baos = baos;
            } catch (Throwable th) {
                throw new SerializationException("Failed to compress serialized data", th);
            }
        }
        if ((_cipher != null) && (_key != null)) {
            try {
                byte[] data = _baos.toByteArray();
                ByteArrayOutputStream baos = new ByteArrayOutputStream(_baos.size());
                _cipher.init(Cipher.ENCRYPT_MODE, _key, _iv);
                CipherOutputStream cifs = new CipherOutputStream(baos, _cipher);
                cifs.write(data, 0, data.length);
                cifs.flush();
                cifs.close();
                _baos = baos;
            } catch (Throwable th) {
                throw new SerializationException("Failed to init cryptographic cipher", th);
            }
        }
        return _baos.toByteArray();
    }
