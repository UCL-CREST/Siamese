    public static FieldParser constructFieldParser(Class<? extends FieldParser> clazz, Field field) {
        try {
            field.setAccessible(true);
            Constructor constructor = clazz.getConstructor(Field.class);
            return (FieldParser) constructor.newInstance(field);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
