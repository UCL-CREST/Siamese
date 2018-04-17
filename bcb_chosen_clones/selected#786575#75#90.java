    public ResultSetDataInfo(Class<T> clazz) {
        this.clazz = clazz;
        Field[] fs = clazz.getDeclaredFields();
        for (Field f : fs) {
            this.analyze(f);
        }
        Table table = clazz.getAnnotation(Table.class);
        if (table != null) {
            this.tableName = table.name();
        }
        try {
            this.rowMapper = new RowMapperClassCreater().createRowMapperClass(this).getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
