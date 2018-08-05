    private void InitEventListener(org.jdom.Element eleEvent) {
        if (eleEvent == null) return;
        List events = eleEvent.getChildren();
        for (Object obj : events) {
            Element event = (Element) obj;
            String event_name = event.getName();
            String event_key = event.getAttributeValue("key");
            String event_classname = event.getTextTrim();
            if (event_name == null || event_classname == null) continue;
            if (!(event_name.equalsIgnoreCase("insert") || "update".equalsIgnoreCase(event_name) || "delete".equalsIgnoreCase(event_name) || "ready".equalsIgnoreCase(event_name) || "publish".equalsIgnoreCase(event_name))) continue;
            Object clazz_obj;
            try {
                Class clazz = Config.GetClassLoader().ReloadClass(event_classname);
                java.lang.reflect.Constructor clazz_constructor = clazz.getConstructor(new Class[] {});
                clazz_obj = clazz_constructor.newInstance(new Object[] {});
            } catch (Exception e) {
                nps.util.DefaultLog.error_noexception(e);
                continue;
            }
            if ("insert".equalsIgnoreCase(event_name)) {
                if (clazz_obj instanceof InsertEventListener) {
                    EventSubscriber.GetSubscriber().AddListener((InsertEventListener) clazz_obj, event_key);
                }
            } else if ("update".equalsIgnoreCase(event_name)) {
                if (clazz_obj instanceof UpdateEventListener) {
                    EventSubscriber.GetSubscriber().AddListener((UpdateEventListener) clazz_obj, event_key);
                }
            } else if ("delete".equalsIgnoreCase(event_name)) {
                if (clazz_obj instanceof DeleteEventListener) {
                    EventSubscriber.GetSubscriber().AddListener((DeleteEventListener) clazz_obj, event_key);
                }
            } else if ("ready".equalsIgnoreCase(event_name)) {
                if (clazz_obj instanceof Ready2PublishEventListener) {
                    EventSubscriber.GetSubscriber().AddListener((Ready2PublishEventListener) clazz_obj, event_key);
                }
            } else if ("publish".equalsIgnoreCase(event_name)) {
                if (clazz_obj instanceof PublishEventListener) {
                    EventSubscriber.GetSubscriber().AddListener((PublishEventListener) clazz_obj, event_key);
                }
            } else if ("cancel".equalsIgnoreCase(event_name)) {
                if (clazz_obj instanceof CancelEventListener) {
                    EventSubscriber.GetSubscriber().AddListener((CancelEventListener) clazz_obj, event_key);
                }
            }
        }
    }
