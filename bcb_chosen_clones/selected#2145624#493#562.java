                        public void run() {
                            try {
                                for (int i = 0; i < imageVals.length; i++) {
                                    ImagePlus imp = WindowManager.getImage(imageVals[i]);
                                    reader.updateMetadata(imp);
                                    LSMFileInfo openLSM = (LSMFileInfo) imp.getOriginalFileInfo();
                                    CZLSMInfoExtended cz = (CZLSMInfoExtended) ((ImageDirectory) openLSM.imageDirectories.get(0)).TIF_CZ_LSMINFO;
                                    Class i5Dc = null;
                                    if (imp == null || imp.getStackSize() == 0) {
                                        IJ.error("Could not open file.");
                                        return;
                                    }
                                    try {
                                        i5Dc = Class.forName("i5d.Image5D");
                                    } catch (ClassNotFoundException e1) {
                                        try {
                                            i5Dc = Class.forName("Image5D");
                                        } catch (ClassNotFoundException e2) {
                                            e2.printStackTrace();
                                        }
                                    }
                                    Constructor i5Dcon = null;
                                    Object o = null;
                                    try {
                                        i5Dcon = i5Dc.getConstructor(new Class[] { String.class, int.class, int.class, int.class, int.class, int.class, int.class, boolean.class });
                                        o = i5Dcon.newInstance(new Object[] { openLSM.fileName, new Integer(imp.getType()), new Integer(imp.getWidth()), new Integer(imp.getHeight()), new Integer((int) cz.DimensionChannels), new Integer((int) cz.DimensionZ), new Integer((int) cz.DimensionTime), new Boolean(false) });
                                        Method i5DsetCurrentPosition = o.getClass().getMethod("setCurrentPosition", new Class[] { int.class, int.class, int.class, int.class, int.class });
                                        Method i5DsetPixels = o.getClass().getMethod("setPixels", new Class[] { Object.class });
                                        Method i5DsetCalibration = o.getClass().getMethod("setCalibration", new Class[] { Calibration.class });
                                        Method i5Dshow = o.getClass().getMethod("show", new Class[] {});
                                        Method i5DgetWindow = o.getClass().getMethod("getWindow", new Class[] {});
                                        Method i5DsetChannelColorModel = o.getClass().getMethod("setChannelColorModel", new Class[] { int.class, ColorModel.class });
                                        Method i5DsetFileInfo = o.getClass().getMethod("setFileInfo", new Class[] { FileInfo.class });
                                        int position = 1;
                                        for (int t = 0; t < cz.DimensionTime; t++) {
                                            for (int z = 0; z < cz.DimensionZ; z++) {
                                                for (int c = 0; c < cz.DimensionChannels; c++) {
                                                    i5DsetCurrentPosition.invoke(o, new Object[] { new Integer(0), new Integer(0), new Integer(c), new Integer(z), new Integer(t) });
                                                    imp.setSlice(position++);
                                                    i5DsetPixels.invoke(o, new Object[] { imp.getProcessor().getPixels() });
                                                }
                                            }
                                            for (int c = 0; c < cz.DimensionChannels; c++) {
                                                i5DsetChannelColorModel.invoke(o, new Object[] { new Integer(c + 1), imp.getProcessor().getColorModel() });
                                            }
                                        }
                                        i5DsetCalibration.invoke(o, new Object[] { imp.getCalibration().copy() });
                                        i5DsetFileInfo.invoke(o, new Object[] { (LSMFileInfo) imp.getOriginalFileInfo() });
                                        i5Dshow.invoke(o, new Object[] {});
                                        ((ImageWindow) i5DgetWindow.invoke(o, new Object[] {})).addWindowFocusListener(new ImageFocusListener());
                                        ServiceMediator.getInfoFrame().updateInfoFrame();
                                        ServiceMediator.getDetailsFrame().updateTreeAndLabels();
                                    } catch (IllegalArgumentException ex) {
                                        ex.printStackTrace();
                                    } catch (InstantiationException ex) {
                                        ex.printStackTrace();
                                    } catch (IllegalAccessException ex) {
                                        ex.printStackTrace();
                                    } catch (InvocationTargetException ex) {
                                        ex.printStackTrace();
                                    } catch (SecurityException ex) {
                                        ex.printStackTrace();
                                    } catch (NoSuchMethodException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            } catch (OutOfMemoryError e) {
                                IJ.outOfMemory("Could not load lsm image.");
                            }
                        }
