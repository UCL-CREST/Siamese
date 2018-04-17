    public void saveFile(MainWindow window) {
        FileOutputStream stream = null;
        try {
            Document outputDoc = new Document();
            DocType dtd = new DocType(UI, DIRECTORY + File.separator + FILE_NAME_DTD);
            Element uiElement = new Element(UI);
            outputDoc.setDocType(dtd);
            outputDoc.addContent(uiElement);
            outputDoc.setRootElement(uiElement);
            uiElement.setAttribute(USE_DEFAULT, "false");
            uiElement.setAttribute(SHOW_TOOL_BAR, Boolean.toString(window.getToolToolBar().isVisible()));
            uiElement.setAttribute(SHOW_UNIT_BAR, Boolean.toString(window.getUnitToolBar().isVisible()));
            String currentLFClassName = UIManager.getLookAndFeel().getClass().getName();
            String systemLFClassName = UIManager.getSystemLookAndFeelClassName();
            if (currentLFClassName.equals(systemLFClassName)) uiElement.setAttribute(LOOK_AND_FEEL, "native"); else uiElement.setAttribute(LOOK_AND_FEEL, "default");
            Element mainWindowElement = new Element(MAIN_WINDOW);
            uiElement.addContent(mainWindowElement);
            mainWindowElement.setAttribute(LOCATION_X, Integer.toString(window.getFrame().getX()));
            mainWindowElement.setAttribute(LOCATION_Y, Integer.toString(window.getFrame().getY()));
            mainWindowElement.setAttribute(WIDTH, Integer.toString(window.getFrame().getWidth()));
            mainWindowElement.setAttribute(HEIGHT, Integer.toString(window.getFrame().getHeight()));
            Element volumeElement = new Element(VOLUME);
            uiElement.addContent(volumeElement);
            AudioPlayer player = window.getDesktop().getSoundPlayer();
            volumeElement.setAttribute(SOUND, Float.toString(player.getVolume()));
            volumeElement.setAttribute(MUTE, Boolean.toString(player.isMute()));
            Element internalWindowsElement = new Element(INTERNAL_WINDOWS);
            uiElement.addContent(internalWindowsElement);
            MainDesktopPane desktop = window.getDesktop();
            JInternalFrame[] windows = desktop.getAllFrames();
            for (JInternalFrame window1 : windows) {
                Element windowElement = new Element(WINDOW);
                internalWindowsElement.addContent(windowElement);
                windowElement.setAttribute(Z_ORDER, Integer.toString(desktop.getComponentZOrder(window1)));
                windowElement.setAttribute(LOCATION_X, Integer.toString(window1.getX()));
                windowElement.setAttribute(LOCATION_Y, Integer.toString(window1.getY()));
                windowElement.setAttribute(WIDTH, Integer.toString(window1.getWidth()));
                windowElement.setAttribute(HEIGHT, Integer.toString(window1.getHeight()));
                windowElement.setAttribute(DISPLAY, Boolean.toString(!window1.isClosed()));
                if (window1 instanceof ToolWindow) {
                    windowElement.setAttribute(TYPE, TOOL);
                    windowElement.setAttribute(NAME, ((ToolWindow) window1).getToolName());
                } else if (window1 instanceof UnitWindow) {
                    windowElement.setAttribute(TYPE, UNIT);
                    windowElement.setAttribute(NAME, ((UnitWindow) window1).getUnit().getName());
                } else {
                    windowElement.setAttribute(TYPE, "other");
                    windowElement.setAttribute(NAME, "other");
                }
            }
            Unit[] toolBarUnits = window.getUnitToolBar().getUnitsInToolBar();
            for (Unit toolBarUnit : toolBarUnits) {
                UnitWindow unitWindow = desktop.findUnitWindow(toolBarUnit);
                if ((unitWindow == null) || unitWindow.isIcon()) {
                    Element windowElement = new Element(WINDOW);
                    internalWindowsElement.addContent(windowElement);
                    windowElement.setAttribute(TYPE, UNIT);
                    windowElement.setAttribute(NAME, toolBarUnit.getName());
                    windowElement.setAttribute(DISPLAY, "false");
                }
            }
            File configFile = new File(DIRECTORY, FILE_NAME);
            if (!configFile.getParentFile().exists()) {
                configFile.getParentFile().mkdirs();
            }
            InputStream in = getClass().getResourceAsStream("/dtd/ui_settings.dtd");
            IOUtils.copy(in, new FileOutputStream(new File(DIRECTORY, "ui_settings.dtd")));
            XMLOutputter fmt = new XMLOutputter();
            fmt.setFormat(Format.getPrettyFormat());
            stream = new FileOutputStream(configFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");
            fmt.output(outputDoc, writer);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }
