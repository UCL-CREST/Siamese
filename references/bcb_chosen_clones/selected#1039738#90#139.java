    public static boolean exportConfiguration(BpxlBPELProcess[] procs, java.io.File path) {
        HashMap<String, Boolean> filename = new HashMap<String, Boolean>();
        MetadataFactoryImpl mfi = new MetadataFactoryImpl();
        MetadataResourceFactoryImpl mrfi = new MetadataResourceFactoryImpl();
        Resource res = (MetadataResourceImpl) mrfi.createResource(null);
        for (BpxlBPELProcess process : procs) {
            int idx = 1;
            String processFilename = process.getName() + ".bpel";
            while (filename.containsKey(processFilename)) processFilename = process.getName() + "_conf" + (idx++) + ".bpel";
            filename.put(processFilename, Boolean.TRUE);
            ProcessType processType = mfi.createProcessType();
            processType.setFilename(processFilename);
            processType.setName(process.getName());
            BpxlType bpxlType = mfi.createBpxlType();
            bpxlType.setConfigurationName(process.getConfiguration().getLabel());
            bpxlType.setConfigurationType(process.getConfiguration().getClass().getSimpleName());
            bpxlType.setDate(new Date().getTime() + "");
            bpxlType.getProcess().add(processType);
            DocumentRoot root = mfi.createDocumentRoot();
            root.setBpxl(bpxlType);
            try {
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(path));
                BpxlProcessInstance[] pInstances = process.getConfiguration().getProcessInstances(process);
                for (int i = 0; i < pInstances.length; i++) {
                    String instanceFilename = processFilename + ".pid." + pInstances[i].getPid() + "";
                    DateType dateType = mfi.createDateType();
                    dateType.setStarted(pInstances[i].getStart().getTime() + "");
                    dateType.setEnded(pInstances[i].getStop().getTime() + "");
                    InstanceType instanceType = mfi.createInstanceType();
                    instanceType.setFilename(instanceFilename);
                    instanceType.setPid(pInstances[i].getPid() + "");
                    instanceType.setState(pInstances[i].getState() + "");
                    instanceType.setDate(dateType);
                    processType.getInstance().add(instanceType);
                    out.putNextEntry(new ZipEntry(instanceFilename));
                    out.write(pInstances[i].getLog().getBytes());
                    out.closeEntry();
                }
                out.putNextEntry(new ZipEntry("metadata.xml"));
                res.getContents().add(root);
                res.save(out, null);
                out.putNextEntry(new ZipEntry(processFilename));
                out.write(process.getBpel().getBytes());
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
