    public static void main(String[] args) {
        SimpleWrapper wrapper = new SimpleWrapper("mywrapper");
        log("main", " for (Object value : wrapper.callMe().andMeAlso().andFinally() )");
        for (Object value : wrapper.callMe().andMeAlso().andFinally()) {
            log("\tmain", "\t loop with value: " + value);
        }
        log("\n\nmain", "Class.forName() => " + SimpleWrapper.class.getName());
        try {
            Class klass = Class.forName("MethodChainTest$SimpleWrapper");
            Class[] constrpartypes = new Class[] { String.class };
            Constructor constr = klass.getConstructor(constrpartypes);
            Object dummyto = constr.newInstance(new String[] { "Java Programmer" });
            SimpleWrapper myWrapper = (SimpleWrapper) dummyto;
            myWrapper.setAge(28);
            log("myWrapper", myWrapper.getClass().getName() + " : " + myWrapper);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
    }
