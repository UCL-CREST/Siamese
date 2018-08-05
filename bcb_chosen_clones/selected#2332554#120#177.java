    @Override
    public void run() {
        EventType type = event.getEventType();
        IBaseObject field = event.getField();
        log.info("select----->" + field.getAttribute(IDatafield.URL));
        try {
            IParent parent = field.getParent();
            String name = field.getName();
            if (type == EventType.ON_BTN_CLICK) {
                invoke(parent, "eventRule_" + name);
                Object value = event.get(Event.ARG_VALUE);
                if (value != null && value instanceof String[]) {
                    String[] args = (String[]) value;
                    for (String arg : args) log.info("argument data: " + arg);
                }
            } else if (type == EventType.ON_BEFORE_DOWNLOAD) invoke(parent, "eventRule_" + name); else if (type == EventType.ON_VALUE_CHANGE) {
                String pattern = (String) event.get(Event.ARG_PATTERN);
                Object value = event.get(Event.ARG_VALUE);
                Class cls = field.getDataType();
                if (cls == null || value == null || value.getClass().equals(cls)) field.setValue(value); else if (pattern == null) field.setValue(ConvertUtils.convert(value.toString(), cls)); else if (Date.class.isAssignableFrom(cls)) field.setValue(new SimpleDateFormat(pattern).parse((String) value)); else if (Number.class.isAssignableFrom(cls)) field.setValue(new DecimalFormat(pattern).parse((String) value)); else field.setValue(new MessageFormat(pattern).parse((String) value));
                invoke(parent, "checkRule_" + name);
                invoke(parent, "defaultRule_" + name);
            } else if (type == EventType.ON_ROW_SELECTED) {
                log.info("table row selected.");
                Object selected = event.get(Event.ARG_ROW_INDEX);
                if (selected instanceof Integer) presentation.setSelectedRowIndex((IModuleList) field, (Integer) selected); else if (selected instanceof List) {
                    String s = "";
                    String conn = "";
                    for (Integer item : (List<Integer>) selected) {
                        s = s + conn + item;
                        conn = ",";
                    }
                    log.info("row " + s + " line(s) been selected.");
                }
            } else if (type == EventType.ON_ROW_DBLCLICK) {
                log.info("table row double-clicked.");
                presentation.setSelectedRowIndex((IModuleList) field, (Integer) event.get(Event.ARG_ROW_INDEX));
            } else if (type == EventType.ON_ROW_INSERT) {
                log.info("table row inserted.");
                listAdd((IModuleList) field, (Integer) event.get(Event.ARG_ROW_INDEX));
            } else if (type == EventType.ON_ROW_REMOVE) {
                log.info("table row removed.");
                listRemove((IModuleList) field, (Integer) event.get(Event.ARG_ROW_INDEX));
            } else if (type == EventType.ON_FILE_UPLOAD) {
                log.info("file uploaded.");
                InputStream is = (InputStream) event.get(Event.ARG_VALUE);
                String uploadFileName = (String) event.get(Event.ARG_FILE_NAME);
                log.info("<-----file name:" + uploadFileName);
                OutputStream os = (OutputStream) field.getValue();
                IOUtils.copy(is, os);
                is.close();
                os.close();
            }
        } catch (Exception e) {
            if (field != null) log.info("field type is :" + field.getDataType().getName());
            log.info("select", e);
        }
    }
