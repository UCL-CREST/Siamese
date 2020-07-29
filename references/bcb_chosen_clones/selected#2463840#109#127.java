    public TraceWriter getTraceWriter(String fileAddress, File input, Map<String, String> inputParams, String[] dataLabels) {
        java.util.List<StringDataParameter> params = new LinkedList<StringDataParameter>();
        for (String key : inputParams.keySet()) {
            params.add(new StringDataParameter(key, inputParams.get(key)));
        }
        try {
            return fileFilter.getTraceWriterClass(input).getConstructor(Collection.class, String[].class, BufferOutputWriter.class, String.class).newInstance(params, dataLabels, BufferOutputWriter.createRandomWriter(), fileAddress);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        assert false : "should not reach here";
        return null;
    }
