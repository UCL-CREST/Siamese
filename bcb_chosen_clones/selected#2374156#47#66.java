            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception e) {
                    LOGGER.warning("Could not set cross platform look and feel.");
                    e.printStackTrace();
                }
                if (monitorDialogClassName == null) {
                    dialog = new MonitorDialog(null);
                } else {
                    try {
                        Class<?> dialogClass = Class.forName(monitorDialogClassName);
                        Constructor<?> dialogCtor = dialogClass.getConstructor(Frame.class);
                        Frame temp = null;
                        dialog = (MonitorDialog) dialogCtor.newInstance(temp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
