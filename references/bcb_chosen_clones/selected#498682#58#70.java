    public TermValueList createTermList() {
        Class<? extends TermValueList> listClass = supportedTypes.get(_cls);
        if (TermCharList.class.equals(listClass)) {
            return new TermCharList();
        } else {
            try {
                Constructor<? extends TermValueList> constructor = listClass.getConstructor(String.class);
                return constructor.newInstance(_format);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
