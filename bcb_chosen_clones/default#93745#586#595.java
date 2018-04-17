    public void setFocusTraversalKeysEnabled(TextField tf, boolean b) {
        try {
            Class FieldClass = tf.getClass();
            Class args[] = { boolean.class };
            Method setfocus = FieldClass.getMethod("setFocusTraversalKeysEnabled", args);
            Object params[] = { new Boolean(b) };
            Object obj = setfocus.invoke(tf, params);
        } catch (Exception ex) {
        }
    }
