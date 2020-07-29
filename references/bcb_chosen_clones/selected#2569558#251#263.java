        private static Module createGbeInstanceFromClass(final Class<? extends Module> gbeClass) {
            Module result;
            try {
                result = gbeClass.getConstructor().newInstance();
            } catch (NoSuchMethodException e) {
                String msg = String.format("@%s class '%s' must have a public zero-arguments constructor", GuiceBerryEnv.class.getSimpleName(), gbeClass.getName());
                throw new IllegalArgumentException(msg, e);
            } catch (Exception e) {
                String msg = String.format("Error creating instance of @%s '%s'", GuiceBerryEnv.class.getSimpleName(), gbeClass.getName());
                throw new IllegalArgumentException(msg, e);
            }
            return result;
        }
