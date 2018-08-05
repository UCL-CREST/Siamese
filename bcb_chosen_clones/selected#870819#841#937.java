    public void convertToCryptoTypes(Symbol targetsym) throws Exception {
        if (this.isCryptoType(targetsym.getValue())) {
            return;
        }
        String _alg = (String) targetsym.getProperty(SymbolProperties.AlgorithmName);
        String _type = targetsym.getType();
        if (null == _alg) {
            Object _v = targetsym.getValue();
            if (_v instanceof PublicKey || _v instanceof PrivateKey) {
                _alg = ((PublicKey) targetsym.getValue()).getAlgorithm();
                targetsym.setProperty(SymbolProperties.AlgorithmName, _alg);
            } else {
                throw new FormatException(__me + ".convertToCryptoTypes(): Symbol \"" + targetsym + "\" property-list entry \"" + SymbolProperties.AlgorithmName + "\" missing - cannot convert \"" + _type + "\" type of class " + _v.getClass().getName() + " to " + "internal JCE equivalent!");
            }
        }
        Class _vc = targetsym.getValue().getClass();
        if (KeySpec.class.isAssignableFrom(_vc)) {
            KeySpec _spec = (KeySpec) targetsym.getValue();
            if (ObolTypes.SharedKey.equals(_type)) {
                targetsym.setValue(SecretKeyFactory.getInstance(_alg).generateSecret(_spec));
                return;
            }
            KeyFactory _factory = KeyFactory.getInstance(_alg);
            if (ObolTypes.PublicKey.equals(_type)) {
                targetsym.setValue(_factory.generatePublic(_spec));
            } else if (ObolTypes.PrivateKey.equals(_type)) {
                targetsym.setValue(_factory.generatePrivate(_spec));
            } else {
                throw new FormatException(__me + ".convertToCryptoTypes(): Symbol \"" + targetsym + "\"'s value is an KeySpec (" + _spec.getClass().getName() + "), but type is \"" + _type + "\", not shared-key, public- or private-key!");
            }
            return;
        }
        if (false == (_vc.isArray() && _vc.getComponentType().isAssignableFrom(byte.class))) {
            throw new FormatException(__me + ".convertToCryptoTypes(): expected byte [] as Symbol \"" + targetsym + "\"'s value, not " + _vc.toString() + " - " + "cannot convert to internal JCE equivalent!");
        }
        byte[] _ba = (byte[]) targetsym.getValue();
        int _newsize = -1;
        if (targetsym.existProperty(SymbolProperties.NumberOfBits)) {
            _newsize = Integer.parseInt((String) targetsym.getProperty(SymbolProperties.NumberOfBits));
        } else if (targetsym.existProperty(SymbolProperties.NumberOfBytes)) {
            _newsize = Integer.parseInt((String) targetsym.getProperty(SymbolProperties.NumberOfBytes)) * 8;
        }
        if (-1 != _newsize) {
            _newsize = (_newsize >> 3) + ((_newsize & 03) != 0 ? 1 : 0);
            if (_newsize < _ba.length) {
                byte[] _trunc = new byte[_newsize];
                System.arraycopy(_ba, 0, _trunc, 0, _newsize);
                _ba = _trunc;
            } else if (_newsize > _ba.length) {
                byte[] _expand = new byte[_newsize];
                for (int _i = _ba.length; _i < _expand.length; _i++) {
                    _expand[_i] = (byte) 0;
                }
                System.arraycopy(_ba, 0, _expand, 0, _ba.length);
                _ba = _expand;
            }
        }
        if (targetsym.existProperty(":serialized")) {
            try {
                ObjectInputStream _oin = new ObjectInputStream(new ByteArrayInputStream(_ba));
                Object _o = _oin.readObject();
                _oin.close();
                Class _oc = _o.getClass();
                if (false == (ObolTypes.PublicKey.equals(_type) && PublicKey.class.isAssignableFrom(_oc)) || (ObolTypes.PrivateKey.equals(_type) && PrivateKey.class.isAssignableFrom(_oc)) || (ObolTypes.SharedKey.equals(_type) && Key.class.isAssignableFrom(_oc))) {
                    throw new FormatException(__me + ".convertToCryptoTypes(): cannot obtain type \"" + _type + "\" from serialized data of type " + _o.getClass().toString());
                }
                targetsym.setValue(_o);
                return;
            } catch (Exception e) {
                throw (FormatException) new FormatException(__me + ".convertToCryptoTypes(): unable to convert " + "serialized type \"" + _type + "\" to decerialized object!").initCause(e);
            }
        }
        if (ObolTypes.SharedKey.equals(_type)) {
            targetsym.setValue(new SecretKeySpec(_ba, _alg));
            return;
        } else if (ObolTypes.PublicKey.equals(_type)) {
            KeyFactory _factory = KeyFactory.getInstance(_alg);
            KeySpec _spec = null;
            String[] _try = new String[] { "java.security.spec." + _alg + "PublicKeySpec", "java.security.spec." + _alg + "EncodedKeySpec", "java.security.spec." + _alg + "KeySpec", "javax.crypto.spec." + _alg + "PublicKeySpec", "javax.crypto.spec." + _alg + "EncodedKeySpec", "javax.crypto.spec." + _alg + "KeySpec" };
            _spec = findAppropriateKeySpec(_alg, _type, _try, _ba);
            if (null == _spec) {
                throw new FormatException(__me + ".convertToCryptoTypes(): cannot find publickey-" + "type KeySpec for algorithm \"" + _alg + "\"!");
            }
            targetsym.setValue(_factory.generatePublic(_spec));
        } else if (ObolTypes.PrivateKey.equals(_type)) {
            KeyFactory _factory = KeyFactory.getInstance(_alg);
            KeySpec _spec = null;
            String[] _try = new String[] { "java.security.spec." + _alg + "PrivateKeySpec", "java.security.spec." + _alg + "EncodedKeySpec", "java.security.spec." + _alg + "KeySpec", "javax.crypto.spec." + _alg + "PrivateKeySpec", "javax.crypto.spec." + _alg + "EncodedKeySpec", "javax.crypto.spec." + _alg + "KeySpec" };
            _spec = findAppropriateKeySpec(_alg, _type, _try, _ba);
            if (null == _spec) {
                throw new FormatException(__me + ".convertToCryptoTypes(): cannot find privatekey-" + "type KeySpec for algorithm \"" + _alg + "\"!");
            }
            targetsym.setValue(_factory.generatePrivate(_spec));
        } else {
            throw new FormatException(__me + ".convertToCryptoTypes(): Symbol " + targetsym + " contains value of unknown type \"" + _type + "\"!");
        }
    }
