    private void initializeFormats() {
        if (formats == null) {
            formats = new ArrayList();
        }
        Iterator iterator = FormatType.iterator();
        List exceptions = new ArrayList();
        Object canvasArgs[] = { new CanvasLayout() };
        Object montageArgs[] = { new MontageLayout() };
        while (iterator.hasNext()) {
            FormatType type = (FormatType) iterator.next();
            Class formatClass = type.getFormatClass();
            Format format = null;
            Class[] argTypes;
            argTypes = new Class[] { CanvasLayout.class };
            try {
                Constructor constructor = formatClass.getConstructor(argTypes);
                format = (Format) constructor.newInstance(canvasArgs);
            } catch (NoSuchMethodException e) {
                trackException(exceptions, e);
            } catch (SecurityException e) {
                trackException(exceptions, e);
            } catch (InstantiationException e) {
                trackException(exceptions, e);
            } catch (IllegalAccessException e) {
                trackException(exceptions, e);
            } catch (IllegalArgumentException e) {
                trackException(exceptions, e);
            } catch (InvocationTargetException e) {
                trackException(exceptions, e);
            }
            if (format == null) {
                argTypes = new Class[] { MontageLayout.class };
                try {
                    Constructor constructor = formatClass.getConstructor(argTypes);
                    format = (Format) constructor.newInstance(montageArgs);
                } catch (NoSuchMethodException e) {
                    trackException(exceptions, e);
                } catch (SecurityException e) {
                    trackException(exceptions, e);
                } catch (InstantiationException e) {
                    trackException(exceptions, e);
                } catch (IllegalAccessException e) {
                    trackException(exceptions, e);
                } catch (IllegalArgumentException e) {
                    trackException(exceptions, e);
                } catch (InvocationTargetException e) {
                    trackException(exceptions, e);
                }
            }
            if (format != null) {
                formats.add(format);
            } else if (exceptions.size() > 0) {
                StringBuffer msgs = new StringBuffer();
                for (int i = 0; i < exceptions.size(); i++) {
                    Exception exception = (Exception) exceptions.get(i);
                    msgs.append(exception.getMessage()).append(Character.LINE_SEPARATOR);
                }
                Exception exception = new IllegalStateException(msgs.toString());
                EclipseCommonPlugin.handleError(ABPlugin.getDefault(), exception);
            }
        }
    }
