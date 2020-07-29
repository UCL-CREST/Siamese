    public synchronized int addTrigger(String className, String data) {
        int id = triggerID++;
        try {
            Constructor triggerConstructor = Class.forName(className).getConstructor(new Class[] { int.class });
            Trigger newTrigger = (Trigger) triggerConstructor.newInstance(new Object[] { id });
            Log.event(Log.INFO, "Registering new trigger type: " + newTrigger.getType());
            triggers.put(id, newTrigger);
            if (data != null) newTrigger.setProperties(data);
            Triggable queue = queues.get(newTrigger.getType());
            queue.RegisterTrigger(id, newTrigger);
            return id;
        } catch (ClassNotFoundException cnfnde) {
            Log.exception(Log.ERROR, "Triggger class not found", cnfnde);
        } catch (NoSuchMethodException nmtde) {
            Log.exception(Log.ERROR, "Trigger class has not requied constructor", nmtde);
        } catch (InstantiationException ie) {
            Log.exception(Log.ERROR, ie);
        } catch (IllegalAccessException ilglacce) {
            Log.exception(Log.ERROR, "Illegal access to constructor", ilglacce);
        } catch (RemoteException re) {
            Log.exception(Log.ERROR, re);
        } catch (InvocationTargetException inve) {
            Log.exception(Log.ERROR, "Cannot invocate target constructor", inve);
        } catch (InvalidDataException invdatae) {
            Log.exception(Log.ERROR, invdatae);
        }
        return -1;
    }
