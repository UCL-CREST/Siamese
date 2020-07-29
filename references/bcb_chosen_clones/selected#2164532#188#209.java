    private DataSink _createDataSink(Results r, SymbolDataSink sDataSink) {
        Class<?> c = DataSinks.getRegisteredClass(sDataSink.pRegisteredClassId);
        if (c == null) {
            r.addError("data-sink class '" + sDataSink.pRegisteredClassId + "' is not registered");
            return null;
        } else {
            Object[] args = new Object[sDataSink.pParameterNames.size()];
            Class<?>[] cArgs = new Class[sDataSink.pParameterNames.size()];
            int i = 0;
            for (Iterator it = sDataSink.pParameterValues.iterator(); it.hasNext(); i++) {
                args[i] = it.next().toString();
                cArgs[i] = String.class;
            }
            try {
                Constructor co = c.getConstructor(cArgs);
                return (DataSink) co.newInstance(args);
            } catch (Exception ex) {
                r.addError("could not create data sink with class-id '" + sDataSink.pRegisteredClassId + "': " + ex.getMessage());
                return null;
            }
        }
    }
