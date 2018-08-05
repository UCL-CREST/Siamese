    protected void initCollection(Collection collection) {
        try {
            Constructor constructor = type.getConstructor(new Class[] { java.lang.String.class });
            zero = (Number) constructor.newInstance(new Object[] { "0" });
            one = (Number) constructor.newInstance(new Object[] { "1" });
            minusOne = (Number) constructor.newInstance(new Object[] { "-1" });
            theAnswer = (Number) constructor.newInstance(new Object[] { "42" });
            b++;
            if (b >= max.longValue()) {
                b = min.longValue() + 1;
            }
            uniqueAnswer = (Number) constructor.newInstance(new Object[] { Long.toString(b) });
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        collection.add(uniqueAnswer);
        collection.add(zero);
        collection.add(theAnswer);
        collection.add(one);
        collection.add(minusOne);
        collection.add(min);
        collection.add(max);
        collection.add(zero);
    }
