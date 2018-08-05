    protected CommandProcessor newCommandProcessor(JSONArray array) throws JSONException {
        if (array.length() == 0) {
            return new NopProcessor();
        } else if (array.get(0) instanceof JSONArray) {
            return new CommandProcessorComposite(newCommandProcessors(array));
        } else {
            Class<?>[] paramTypes = new Class<?>[array.length() - 1];
            Object[] params = new Object[array.length() - 1];
            for (int i = 0; i < array.length() - 1; i++) {
                if (array.get(i + 1) instanceof JSONArray) {
                    paramTypes[i] = CommandProcessor.class;
                    params[i] = newCommandProcessor(array.getJSONArray(i + 1));
                } else {
                    paramTypes[i] = array.get(i + 1).getClass();
                    params[i] = array.get(i + 1);
                }
            }
            String cpClassName = PACKAGE + array.getString(0) + "Processor";
            try {
                Class<?> cpClass = Class.forName(cpClassName);
                Constructor<?> constructor = cpClass.getConstructor(paramTypes);
                return (CommandProcessor) constructor.newInstance(params);
            } catch (Exception e) {
                throw new RuntimeException("Could not instantiate " + cpClassName, e);
            }
        }
    }
