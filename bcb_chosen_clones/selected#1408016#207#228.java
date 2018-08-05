    private TranslationTable getSQLiteTranslationTable(String dbName, boolean directionFE, TokenBuilder tokenBuilder, int ttLimit, double ttThreshold, double ttTresholdWeights[]) throws PhramerException, IOException {
        try {
            Class c = Class.forName("org.phramer.v1.decoder.table.SQLiteTranslationTable");
            Constructor constr = c.getConstructors()[0];
            Object[] params = { dbName, directionFE, Constants.TYPE_PHARAOH, new EFProcessorSimple(tokenBuilder), ttLimit, ttThreshold, ttTresholdWeights };
            try {
                return (TranslationTable) constr.newInstance(params);
            } catch (InstantiationException e) {
                throw new Error(e);
            } catch (IllegalAccessException e) {
                throw new Error(e);
            } catch (IllegalArgumentException e) {
                throw new Error(e);
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof IOException) throw (IOException) e.getTargetException();
                if (e.getTargetException() instanceof PhramerException) throw (PhramerException) e.getTargetException();
                throw new PhramerException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new Error("phramer sqlite edition is not in classpath");
        }
    }
