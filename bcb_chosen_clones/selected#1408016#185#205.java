    private static NgramLanguageModel getSQLiteLanguageModel(String lmURL) throws IOException, PhramerException {
        try {
            Class c = Class.forName("org.phramer.v1.decoder.lm.ngram.SQLiteBackOffLM");
            Constructor constr = c.getConstructors()[0];
            Object[] params = { lmURL };
            try {
                return (NgramLanguageModel) constr.newInstance(params);
            } catch (InstantiationException e) {
                throw new Error(e);
            } catch (IllegalAccessException e) {
                throw new Error(e);
            } catch (IllegalArgumentException e) {
                throw new Error(e);
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof IOException) throw (IOException) e.getTargetException();
                throw new PhramerException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new Error("phramer sqlite edition is not in classpath");
        }
    }
