    public static <T extends IParser<?>> T createParser(Class<T> type, String text) {
        try {
            return type.getConstructor(Reader.class).newInstance(new StringReader(text));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
