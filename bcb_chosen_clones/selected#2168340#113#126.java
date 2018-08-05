            @SuppressWarnings("unchecked")
            @Override
            protected T wrapValue(Object value) {
                if (type == null) return (T) value;
                try {
                    Constructor<T> constructor = getConstructor();
                    if (constructor == null) {
                        throw new OntopolyModelRuntimeException("Couldn't find constructor for the class: " + type);
                    }
                    return constructor.newInstance(new Object[] { value, TopicMap.this });
                } catch (Exception e) {
                    throw new OntopolyModelRuntimeException(e);
                }
            }
