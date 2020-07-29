        public <Type> Type getInstanceOf(Class<Type> type) {
            try {
                Constructor<Type> constructor = type.getConstructor(SitFactory.class);
                return constructor.newInstance(InternalSitFactory.this);
            } catch (Exception e) {
                throw new SeafInstantiationException(e);
            }
        }
