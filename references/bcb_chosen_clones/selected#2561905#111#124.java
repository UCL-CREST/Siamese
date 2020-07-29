        public static final java.lang.Object GetPropertyData(java.util.Properties properties, java.lang.String name, java.lang.Class value_class) {
            if (null == properties || null == name || null == value_class) throw new Error.Argument("Null argument."); else {
                java.lang.Object value = properties.get(name);
                if (value instanceof java.lang.String) {
                    try {
                        java.lang.reflect.Constructor ctor = value_class.getConstructor(CTOR_ARGS_D);
                        java.lang.Object class_value = ctor.newInstance(new java.lang.Object[] { value });
                        return class_value;
                    } catch (java.lang.Throwable ignore) {
                        return value;
                    }
                } else return value;
            }
        }
