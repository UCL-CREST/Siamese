    public static void copy(Context cx, Scriptable thisObj, Object[] args, Function funObj) throws IOException {
        InputStream input = (InputStream) ((NativeJavaObject) args[0]).unwrap();
        OutputStream output = (OutputStream) ((NativeJavaObject) args[1]).unwrap();
        IOUtils.copy(input, output);
    }
