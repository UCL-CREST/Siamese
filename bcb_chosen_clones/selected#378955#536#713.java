    @SuppressWarnings("unchecked")
    private int appendone(byte[] sigb, int sigofs, Object data) throws DBusException {
        try {
            int i = sigofs;
            if (Debug.debug) Debug.print(Debug.VERBOSE, (Object) bytecounter);
            if (Debug.debug) Debug.print(Debug.VERBOSE, "Appending type: " + ((char) sigb[i]) + " value: " + data);
            pad(sigb[i]);
            switch(sigb[i]) {
                case ArgumentType.BYTE:
                    appendByte(((Number) data).byteValue());
                    break;
                case ArgumentType.BOOLEAN:
                    appendint(((Boolean) data).booleanValue() ? 1 : 0, 4);
                    break;
                case ArgumentType.DOUBLE:
                    long l = Double.doubleToLongBits(((Number) data).doubleValue());
                    appendint(l, 8);
                    break;
                case ArgumentType.FLOAT:
                    int rf = Float.floatToIntBits(((Number) data).floatValue());
                    appendint(rf, 4);
                    break;
                case ArgumentType.UINT32:
                    appendint(((Number) data).longValue(), 4);
                    break;
                case ArgumentType.INT64:
                    appendint(((Number) data).longValue(), 8);
                    break;
                case ArgumentType.UINT64:
                    if (big) {
                        appendint(((UInt64) data).top(), 4);
                        appendint(((UInt64) data).bottom(), 4);
                    } else {
                        appendint(((UInt64) data).bottom(), 4);
                        appendint(((UInt64) data).top(), 4);
                    }
                    break;
                case ArgumentType.INT32:
                    appendint(((Number) data).intValue(), 4);
                    break;
                case ArgumentType.UINT16:
                    appendint(((Number) data).intValue(), 2);
                    break;
                case ArgumentType.INT16:
                    appendint(((Number) data).shortValue(), 2);
                    break;
                case ArgumentType.STRING:
                case ArgumentType.OBJECT_PATH:
                    String payload = data.toString();
                    byte[] payloadbytes = null;
                    try {
                        payloadbytes = payload.getBytes("UTF-8");
                    } catch (UnsupportedEncodingException UEe) {
                        if (AbstractConnection.EXCEPTION_DEBUG && Debug.debug) Debug.print(UEe);
                        throw new DBusException(_("System does not support UTF-8 encoding"));
                    }
                    if (Debug.debug) Debug.print(Debug.VERBOSE, "Appending String of length " + payloadbytes.length);
                    appendint(payloadbytes.length, 4);
                    appendBytes(payloadbytes);
                    appendBytes(padding[1]);
                    break;
                case ArgumentType.SIGNATURE:
                    if (data instanceof Type[]) payload = Marshalling.getDBusType((Type[]) data); else payload = (String) data;
                    byte[] pbytes = payload.getBytes();
                    preallocate(2 + pbytes.length);
                    appendByte((byte) pbytes.length);
                    appendBytes(pbytes);
                    appendByte((byte) 0);
                    break;
                case ArgumentType.ARRAY:
                    if (Debug.debug) {
                        if (data instanceof Object[]) Debug.print(Debug.VERBOSE, "Appending array: " + Arrays.deepToString((Object[]) data));
                    }
                    byte[] alen = new byte[4];
                    appendBytes(alen);
                    pad(sigb[++i]);
                    long c = bytecounter;
                    if (data.getClass().isArray() && data.getClass().getComponentType().isPrimitive()) {
                        byte[] primbuf;
                        int algn = getAlignment(sigb[i]);
                        int len = Array.getLength(data);
                        switch(sigb[i]) {
                            case ArgumentType.BYTE:
                                primbuf = (byte[]) data;
                                break;
                            case ArgumentType.INT16:
                            case ArgumentType.INT32:
                            case ArgumentType.INT64:
                                primbuf = new byte[len * algn];
                                for (int j = 0, k = 0; j < len; j++, k += algn) marshallint(Array.getLong(data, j), primbuf, k, algn);
                                break;
                            case ArgumentType.BOOLEAN:
                                primbuf = new byte[len * algn];
                                for (int j = 0, k = 0; j < len; j++, k += algn) marshallint(Array.getBoolean(data, j) ? 1 : 0, primbuf, k, algn);
                                break;
                            case ArgumentType.DOUBLE:
                                primbuf = new byte[len * algn];
                                if (data instanceof float[]) for (int j = 0, k = 0; j < len; j++, k += algn) marshallint(Double.doubleToRawLongBits(((float[]) data)[j]), primbuf, k, algn); else for (int j = 0, k = 0; j < len; j++, k += algn) marshallint(Double.doubleToRawLongBits(((double[]) data)[j]), primbuf, k, algn);
                                break;
                            case ArgumentType.FLOAT:
                                primbuf = new byte[len * algn];
                                for (int j = 0, k = 0; j < len; j++, k += algn) marshallint(Float.floatToRawIntBits(((float[]) data)[j]), primbuf, k, algn);
                                break;
                            default:
                                throw new MarshallingException(_("Primative array being sent as non-primative array."));
                        }
                        appendBytes(primbuf);
                    } else if (data instanceof List) {
                        Object[] contents = ((List) data).toArray();
                        int diff = i;
                        ensureBuffers(contents.length * 4);
                        for (Object o : contents) diff = appendone(sigb, i, o);
                        i = diff;
                    } else if (data instanceof Map) {
                        int diff = i;
                        ensureBuffers(((Map) data).size() * 6);
                        for (Map.Entry<Object, Object> o : ((Map<Object, Object>) data).entrySet()) diff = appendone(sigb, i, o);
                        if (i == diff) {
                            Vector<Type> temp = new Vector<Type>();
                            byte[] temp2 = new byte[sigb.length - diff];
                            System.arraycopy(sigb, diff, temp2, 0, temp2.length);
                            String temp3 = new String(temp2);
                            int temp4 = Marshalling.getJavaType(temp3, temp, 1);
                            diff += temp4;
                        }
                        i = diff;
                    } else {
                        Object[] contents = (Object[]) data;
                        ensureBuffers(contents.length * 4);
                        int diff = i;
                        for (Object o : contents) diff = appendone(sigb, i, o);
                        i = diff;
                    }
                    if (Debug.debug) Debug.print(Debug.VERBOSE, "start: " + c + " end: " + bytecounter + " length: " + (bytecounter - c));
                    marshallint(bytecounter - c, alen, 0, 4);
                    break;
                case ArgumentType.STRUCT1:
                    Object[] contents;
                    if (data instanceof Container) contents = ((Container) data).getParameters(); else contents = (Object[]) data;
                    ensureBuffers(contents.length * 4);
                    int j = 0;
                    for (i++; sigb[i] != ArgumentType.STRUCT2; i++) i = appendone(sigb, i, contents[j++]);
                    break;
                case ArgumentType.DICT_ENTRY1:
                    if (data instanceof Map.Entry) {
                        i++;
                        i = appendone(sigb, i, ((Map.Entry) data).getKey());
                        i++;
                        i = appendone(sigb, i, ((Map.Entry) data).getValue());
                        i++;
                    } else {
                        contents = (Object[]) data;
                        j = 0;
                        for (i++; sigb[i] != ArgumentType.DICT_ENTRY2; i++) i = appendone(sigb, i, contents[j++]);
                    }
                    break;
                case ArgumentType.VARIANT:
                    if (data instanceof Variant) {
                        Variant var = (Variant) data;
                        appendone(new byte[] { ArgumentType.SIGNATURE }, 0, var.getSig());
                        appendone((var.getSig()).getBytes(), 0, var.getValue());
                    } else if (data instanceof Object[]) {
                        contents = (Object[]) data;
                        appendone(new byte[] { ArgumentType.SIGNATURE }, 0, contents[0]);
                        appendone(((String) contents[0]).getBytes(), 0, contents[1]);
                    } else {
                        String sig = Marshalling.getDBusType(data.getClass())[0];
                        appendone(new byte[] { ArgumentType.SIGNATURE }, 0, sig);
                        appendone((sig).getBytes(), 0, data);
                    }
                    break;
            }
            return i;
        } catch (ClassCastException CCe) {
            if (AbstractConnection.EXCEPTION_DEBUG && Debug.debug) Debug.print(Debug.ERR, CCe);
            throw new MarshallingException(MessageFormat.format(_("Trying to marshall to unconvertable type (from {0} to {1})."), new Object[] { data.getClass().getName(), sigb[sigofs] }));
        }
    }
