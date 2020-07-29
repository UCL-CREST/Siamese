    @SuppressWarnings("unchecked")
    public final <T> T readObject(Class<T> obj_class, final Class<?>... deep_classes) throws IOException, DeserializingException, InstantiationException {
        assert deep_classes != null;
        switch(readByte()) {
            case DIRECT:
                final int object_id = readInteger();
                if (obj_class == null) {
                    switch(readByte()) {
                        case DIRECT:
                            final int class_id = readInteger();
                            obj_class = (Class<T>) getClass(readString());
                            class_map.put(class_id, obj_class);
                            break;
                        case REFERENCE:
                            try {
                                obj_class = (Class<T>) class_map.get(readInteger());
                            } catch (final ClassCastException ex) {
                                throw new DeserializingException.CorruptStream(null);
                            }
                            if (obj_class == null) throw new DeserializingException.CorruptStream(null);
                            break;
                        default:
                            throw new DeserializingException.CorruptStream(null);
                    }
                }
                Object o;
                if (obj_class.isArray()) {
                    final int len = readInteger();
                    final Class<?> c = obj_class.getComponentType();
                    if (!c.isPrimitive()) {
                        final Object[] a = (Object[]) Array.newInstance(c, len);
                        if (deep_classes.length == 0) for (int i = 0; i < len; ++i) a[i] = readObject(null); else {
                            final Class<?>[] deeper_classes = new Class<?>[deep_classes.length - 1];
                            System.arraycopy(deep_classes, 1, deeper_classes, 0, deeper_classes.length);
                            for (int i = 0; i < len; ++i) a[i] = readObject(deep_classes[0], deeper_classes);
                        }
                        o = a;
                    } else if (c == Boolean.TYPE) {
                        final boolean[] a = (boolean[]) Array.newInstance(c, len);
                        for (int i = 0; i < len; ++i) a[i] = readBoolean();
                        o = a;
                    } else if (c == Byte.TYPE) {
                        final byte[] a = (byte[]) Array.newInstance(c, len);
                        for (int i = 0; i < len; ++i) a[i] = readByte();
                        o = a;
                    } else if (c == Short.TYPE) {
                        final short[] a = (short[]) Array.newInstance(c, len);
                        for (int i = 0; i < len; ++i) a[i] = readShort();
                        o = a;
                    } else if (c == Character.TYPE) {
                        final char[] a = (char[]) Array.newInstance(c, len);
                        for (int i = 0; i < len; ++i) a[i] = readCharacter();
                        o = a;
                    } else if (c == Integer.TYPE) {
                        final int[] a = (int[]) Array.newInstance(c, len);
                        for (int i = 0; i < len; ++i) a[i] = readInteger();
                        o = a;
                    } else if (c == Long.TYPE) {
                        final long[] a = (long[]) Array.newInstance(c, len);
                        for (int i = 0; i < len; ++i) a[i] = readLong();
                        o = a;
                    } else if (c == Float.TYPE) {
                        final float[] a = (float[]) Array.newInstance(c, len);
                        for (int i = 0; i < len; ++i) a[i] = readFloat();
                        o = a;
                    } else {
                        final double[] a = (double[]) Array.newInstance(c, len);
                        for (int i = 0; i < len; ++i) a[i] = readDouble();
                        o = a;
                    }
                } else if (obj_class == Boolean.class) o = Boolean.valueOf(readBoolean()); else if (obj_class == Byte.class) o = Byte.valueOf(readByte()); else if (obj_class == Short.class) o = Short.valueOf(readShort()); else if (obj_class == Character.class) o = Character.valueOf(readCharacter()); else if (obj_class == Integer.class) o = Integer.valueOf(readInteger()); else if (obj_class == Long.class) o = Long.valueOf(readLong()); else if (obj_class == Float.class) o = Float.valueOf(readFloat()); else if (obj_class == Double.class) o = Double.valueOf(readDouble()); else if (obj_class == String.class) o = readString(); else if (obj_class.getSuperclass() == Enum.class) {
                    try {
                        o = obj_class.getEnumConstants()[readInteger()];
                    } catch (final IndexOutOfBoundsException ex) {
                        throw new DeserializingException.CorruptStream(obj_class.getName());
                    }
                } else if (Collection.class.isAssignableFrom(obj_class)) {
                    o = newInstance(getConstructor(obj_class));
                    final int s = readInteger();
                    if (deep_classes.length == 0) for (int i = 0; i < s; ++i) ((Collection) o).add(readObject(null)); else {
                        final Class<?>[] deeper_classes = new Class<?>[deep_classes.length - 1];
                        System.arraycopy(deep_classes, 1, deeper_classes, 0, deeper_classes.length);
                        for (int i = 0; i < s; ++i) ((Collection) o).add(readObject(deep_classes[0], deeper_classes));
                    }
                } else if (Map.class.isAssignableFrom(obj_class)) {
                    o = newInstance(getConstructor(obj_class));
                    final int s = readInteger();
                    if (deep_classes.length == 0) for (int i = 0; i < s; ++i) ((Map) o).put(readObject(null), readObject(null)); else if (deep_classes.length == 1) for (int i = 0; i < s; ++i) ((Map) o).put(readObject(deep_classes[0]), readObject(null)); else {
                        final Class<?>[] deeper_classes = new Class<?>[deep_classes.length - 2];
                        System.arraycopy(deep_classes, 2, deeper_classes, 0, deeper_classes.length);
                        for (int i = 0; i < s; ++i) ((Map) o).put(readObject(deep_classes[0], deeper_classes), readObject(deep_classes[1], deeper_classes));
                    }
                } else if (Serializable.class.isAssignableFrom(obj_class)) {
                    final Method method = getStaticMethod(obj_class);
                    if (method != null) o = getObject(method); else o = newInstance(getConstructor(obj_class, DeserializingStream.class), this);
                } else throw new DeserializingException.ClassNotDeserializable(obj_class.getName());
                object_map.put(object_id, o);
                return (T) o;
            case REFERENCE:
                try {
                    return (T) object_map.get(readInteger());
                } catch (final ClassCastException ex) {
                    throw new DeserializingException.CorruptStream(obj_class == null ? null : obj_class.getName());
                }
            case NULL:
                return null;
            default:
                throw new DeserializingException.CorruptStream(obj_class == null ? null : obj_class.getName());
        }
    }
