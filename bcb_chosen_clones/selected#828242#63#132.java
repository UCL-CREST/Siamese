    public String statementExecuting(String sql) {
        StringBuffer buffer = new StringBuffer(sql);
        Map<String, String> cache = plugin.getCache();
        Map<String, String> currentCache = new HashMap<String, String>();
        Pattern p = Pattern.compile(":[a-zA-Z]\\w+");
        Matcher m = p.matcher(buffer);
        while (m.find()) {
            if (isQuoted(buffer, m.start())) continue;
            final String var = m.group();
            String value = null;
            if (currentCache.containsKey(var)) {
                value = currentCache.get(var);
            } else {
                final String oldValue = cache.get(var);
                if (SwingUtilities.isEventDispatchThread()) {
                    createParameterDialog(var, oldValue);
                    while (!dialog.isDone()) {
                        try {
                            AWTEvent event = Toolkit.getDefaultToolkit().getSystemEventQueue().getNextEvent();
                            Object source = event.getSource();
                            if (event instanceof ActiveEvent) {
                                ((ActiveEvent) event).dispatch();
                            } else if (source instanceof Component) {
                                ((Component) source).dispatchEvent(event);
                            } else if (source instanceof MenuComponent) {
                                ((MenuComponent) source).dispatchEvent(event);
                            } else {
                                System.err.println("Unable to dispatch: " + event);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {

                            public void run() {
                                createParameterDialog(var, oldValue);
                            }
                        });
                        while (!dialog.isDone()) {
                            wait();
                        }
                    } catch (InvocationTargetException ite) {
                        ite.printStackTrace();
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
                if (dialog.isCancelled()) {
                    dialog = null;
                    return null;
                }
                value = sanitizeValue(dialog.getValue(), dialog.isQuotingNeeded());
                cache.put(var, dialog.getValue());
                currentCache.put(var, value);
                dialog = null;
            }
            buffer.replace(m.start(), m.end(), value);
            m.reset();
        }
        GUIUtils.processOnSwingEventThread(new Runnable() {

            public void run() {
                new SelectWidgetCommand(session.getActiveSessionWindow()).execute();
            }
        });
        return buffer.toString();
    }
