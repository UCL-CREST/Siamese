    public static Throwable createWithCause(Class clazz, String message, Throwable cause) {
        Throwable re = null;
        if (causesAllowed) {
            try {
                Constructor constructor = clazz.getConstructor(new Class[] { String.class, Throwable.class });
                re = (Throwable) constructor.newInstance(new Object[] { message, cause });
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                causesAllowed = false;
            }
        }
        if (re == null) {
            try {
                Constructor constructor = clazz.getConstructor(new Class[] { String.class });
                re = (Throwable) constructor.newInstance(new Object[] { message + " caused by " + cause });
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException("Error caused " + e);
            }
        }
        return re;
    }
