    public DataAdapter[] load() throws DataAdapterException {
        try {
            policyFilesByID.clear();
            Vector<DataAdapter> adapters = new Vector<DataAdapter>();
            File dir = new File(path);
            if (dir.isDirectory()) {
                File[] policyFiles = dir.listFiles(new FilenameFilter() {

                    public boolean accept(File dir, String name) {
                        if (pattern == null || pattern.length() == 0) {
                            return true;
                        } else {
                            return name.matches(pattern);
                        }
                    }
                });
                System.out.println("Loading policies from '" + dir.toString() + "' ...");
                PDP currentPDP = DataStoreHelper.getPDP(this);
                String defaultSchema = verifySchemaFile(getPolicyDefaultSchema());
                InputStream in = null;
                int actualPolicyNum = 0;
                boolean warn = false;
                boolean error = false;
                System.gc();
                long memBegin = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                long begin = System.currentTimeMillis();
                for (int i = 0; i < policyFiles.length; i++) {
                    try {
                        in = new FileInputStream(policyFiles[i]);
                        Element root = parse(in, defaultSchema);
                        URI policyId = getPolicyOrPolicySetId(root);
                        if (policyFilesByID.get(policyId) != null) {
                            throw new DataAdapterException("The policy loaded from '" + policyFiles[i] + "' with ID<" + policyId + "> already exists.");
                        }
                        policyFilesByID.put(policyId, policyFiles[i]);
                        DataAdapter da = createPolicyDataAdapterFromXMLElement(root);
                        ((AbstractPolicy) da.getEngineElement()).setOwnerPDP(currentPDP);
                        adapters.add(da);
                        actualPolicyNum++;
                        if (actualPolicyNum % 10 == 0) {
                            System.out.print(".");
                        }
                        if (actualPolicyNum % 1000 == 0 && i < policyFiles.length - 1) {
                            System.out.println();
                        }
                        if (logger.isDebugEnabled()) {
                            try {
                                XACMLElement engineElem = da.getEngineElement();
                                Constructor<?> cons = da.getClass().getConstructor(XACMLElement.class);
                                DataAdapter daGenFromEngineElem = (DataAdapter) cons.newInstance(engineElem);
                                logger.debug("Dump policy loaded from '" + policyFiles[i] + "': " + getNodeXMLText((Element) daGenFromEngineElem.getDataStoreObject()));
                            } catch (Exception debugEx) {
                                logger.debug("Dump policy failed due to: ", debugEx);
                            }
                        }
                    } catch (Exception e) {
                        if (e instanceof XMLGeneralException || e instanceof PolicySyntaxException) {
                            logger.warn("Could not load file '" + policyFiles[i] + "' since it should not be a valid policy file.", e);
                            warn = true;
                        } else if (e instanceof DataAdapterException) {
                            throw e;
                        } else {
                            logger.error("Error occurs when parsing policy file : " + policyFiles[i], e);
                            error = true;
                        }
                    } finally {
                        try {
                            in.close();
                        } catch (Exception ex) {
                        }
                    }
                }
                if (actualPolicyNum > 0) {
                    long end = System.currentTimeMillis();
                    System.gc();
                    long memEnd = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                    System.out.println("\n" + actualPolicyNum + " policies loaded. " + "Time elapsed " + (end - begin) / 1000 + " second. " + "Memory used " + (memEnd - memBegin) / 1024 / 1024 + " MB.");
                    if (error || warn) {
                        System.out.println("There are " + (error ? "errors" : "warnings") + " occur while loading policies, please check log file for details.");
                    }
                }
            }
            return adapters.toArray(new DataAdapter[0]);
        } catch (DataAdapterException daEx) {
            throw daEx;
        } catch (Exception ex) {
            throw new DataAdapterException("Failed to get default policy schema from configuration.", ex);
        }
    }
