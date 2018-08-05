    public void generatePackage(String location, boolean exportInstances, boolean exportSolvers, boolean exportClient, boolean exportRunsolver, boolean exportConfig, boolean exportVerifier, File clientBinary, File verifierBinary, Tasks task) throws FileNotFoundException, IOException, NoConnectionToDBException, SQLException, ClientBinaryNotFoundException, InstanceNotInDBException, TaskCancelledException, InterruptedException {
        File tmpDir = new File("tmp");
        tmpDir.mkdir();
        task.setCancelable(true);
        Calendar cal = Calendar.getInstance();
        String dateStr = cal.get(Calendar.YEAR) + "" + (cal.get(Calendar.MONTH) < 9 ? "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1)) + "" + (cal.get(Calendar.DATE) < 10 ? "0" + cal.get(Calendar.DATE) : cal.get(Calendar.DATE));
        ArrayList<ExperimentHasGridQueue> eqs = ExperimentHasGridQueueDAO.getExperimentHasGridQueueByExperiment(activeExperiment);
        int count = 0;
        for (ExperimentHasGridQueue eq : eqs) {
            GridQueue queue = GridQueueDAO.getById(eq.getIdGridQueue());
            File zipFile = new File(location + Util.getFilename(activeExperiment.getName() + "_" + queue.getName() + "_" + dateStr + ".zip"));
            if (zipFile.exists()) {
                zipFile.delete();
            }
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
            ZipEntry entry;
            task.setOperationName("Generating Package " + (++count) + " of " + eqs.size());
            ArrayList<SolverBinaries> solverBinaries;
            if (exportSolvers) {
                solverBinaries = SolverBinariesDAO.getSolverBinariesInExperiment(activeExperiment);
            } else {
                solverBinaries = new ArrayList<SolverBinaries>();
            }
            LinkedList<Instance> instances;
            if (exportInstances) {
                instances = InstanceDAO.getAllByExperimentId(activeExperiment.getId());
            } else {
                instances = new LinkedList<Instance>();
            }
            int total = solverBinaries.size() + instances.size();
            int done = 0;
            if (!task.isCancelled() && exportSolvers) {
                for (SolverBinaries binary : solverBinaries) {
                    done++;
                    task.setTaskProgress((float) done / (float) total);
                    if (task.isCancelled()) {
                        task.setStatus("Cancelled");
                        break;
                    }
                    task.setStatus("Writing solver " + done + " of " + solverBinaries.size());
                    ZipInputStream zis = new ZipInputStream(SolverBinariesDAO.getZippedBinaryFile(binary));
                    ZipEntry entryIn;
                    byte[] buffer = new byte[4 * 1024];
                    while ((entryIn = zis.getNextEntry()) != null) {
                        if (entryIn.isDirectory()) {
                            continue;
                        }
                        entry = new ZipEntry("solvers" + System.getProperty("file.separator") + binary.getMd5() + System.getProperty("file.separator") + entryIn.getName());
                        zos.putNextEntry(entry);
                        int read;
                        while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
                            zos.write(buffer, 0, read);
                        }
                        zos.closeEntry();
                    }
                }
            }
            if (!task.isCancelled() && exportInstances) {
                for (Instance i : instances) {
                    done++;
                    task.setTaskProgress((float) done / (float) total);
                    if (task.isCancelled()) {
                        task.setStatus("Cancelled");
                        break;
                    }
                    task.setStatus("Writing instance " + (done - solverBinaries.size()) + " of " + instances.size());
                    File f = InstanceDAO.getBinaryFileOfInstance(i);
                    entry = new ZipEntry("instances" + System.getProperty("file.separator") + i.getMd5() + "_" + i.getName());
                    addFileToZIP(f, entry, zos);
                }
            }
            if (!task.isCancelled()) {
                task.setStatus("Writing client");
                if (exportConfig) {
                    String verifierFilename = verifierBinary == null ? null : "verifiers/" + verifierBinary.getName();
                    addConfigurationFile(zos, queue, verifierFilename);
                }
                if (exportClient) {
                    addClient(zos, clientBinary);
                }
                if (exportRunsolver) {
                    addRunsolver(zos);
                }
                if (exportVerifier) {
                    addVerifier(zos, verifierBinary);
                }
            }
            zos.close();
            deleteDirectory(new File("tmp"));
            if (task.isCancelled()) {
                throw new TaskCancelledException("Cancelled");
            }
        }
    }
