    public DataImpl open(String id) throws BadFormException, IOException, VisADException {
        String header = getClass().getName() + ".open(String): ";
        DataImpl data;
        try {
            Class sourceClass = Class.forName(getClass().getPackage().getName() + ".DODSSource");
            DataInputStream source = (DataInputStream) sourceClass.getConstructor(new Class[0]).newInstance(new Object[0]);
            sourceClass.getMethod("open", new Class[] { String.class }).invoke(source, new Object[] { id });
            data = new Consolidator(new TimeFactorer(source)).readData();
        } catch (ClassNotFoundException e) {
            throw new VisADException(header + e + ".  " + sourceMessage);
        } catch (NoSuchMethodException e) {
            throw new VisADException(header + e + contactMessage);
        } catch (SecurityException e) {
            throw new VisADException(header + e + contactMessage);
        } catch (InstantiationException e) {
            throw new VisADException(header + e + contactMessage);
        } catch (IllegalAccessException e) {
            throw new VisADException(header + e + contactMessage);
        } catch (IllegalArgumentException e) {
            throw new VisADException(header + e + contactMessage);
        } catch (InvocationTargetException e) {
            throw new VisADException(e.getTargetException().getMessage());
        }
        return data;
    }
