            public void run() {
                Importer importer = new Importer();
                ImporterProgress ip = new ImporterProgress();
                try {
                    if (checkImportPQfromGC.isChecked()) {
                        ip.addStep(ip.new Step("importGC", 4));
                    }
                    if (checkBoxImportGPX.isChecked()) {
                        ip.addStep(ip.new Step("ExtractZip", 1));
                        ip.addStep(ip.new Step("AnalyseGPX", 1));
                        ip.addStep(ip.new Step("ImportGPX", 4));
                    }
                    if (checkBoxGcVote.isChecked()) {
                        ip.addStep(ip.new Step("sendGcVote", 1));
                        ip.addStep(ip.new Step("importGcVote", 4));
                    }
                    if (checkBoxPreloadImages.isChecked()) {
                        ip.addStep(ip.new Step("importImages", 4));
                    }
                    if (downloadPQList != null && downloadPQList.size() > 0) {
                        Iterator<PQ> iterator = downloadPQList.iterator();
                        ip.setJobMax("importGC", downloadPQList.size());
                        do {
                            PQ pq = iterator.next();
                            if (pq.downloadAvible) {
                                ip.ProgressInkrement("importGC", "Download: " + pq.Name, false);
                                try {
                                    PocketQuery.DownloadSinglePocketQuery(pq);
                                } catch (OutOfMemoryError e) {
                                    Logger.Error("PQ-download", "OutOfMemoryError-" + pq.Name, e);
                                    e.printStackTrace();
                                }
                            }
                        } while (iterator.hasNext());
                        if (downloadPQList.size() == 0) {
                            ip.ProgressInkrement("importGC", "", true);
                        }
                    }
                    if (checkBoxImportGPX.isChecked() && directory.exists()) {
                        System.gc();
                        long startTime = System.currentTimeMillis();
                        Database.Data.beginTransaction();
                        try {
                            importer.importGpx(directoryPath, ip);
                            Database.Data.setTransactionSuccessful();
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                        Database.Data.endTransaction();
                        Log.i("Import", "GPX Import took " + (System.currentTimeMillis() - startTime) + "ms");
                        System.gc();
                        File[] filelist = directory.listFiles();
                        for (File tmp : filelist) {
                            if (tmp.isDirectory()) {
                                ArrayList<File> ordnerInhalt = FileIO.recursiveDirectoryReader(tmp, new ArrayList<File>());
                                for (File tmp2 : ordnerInhalt) {
                                    tmp2.delete();
                                }
                            }
                            tmp.delete();
                        }
                    }
                    if (checkBoxGcVote.isChecked()) {
                        Database.Data.beginTransaction();
                        try {
                            importer.importGcVote(Global.LastFilter.getSqlWhere(), ip);
                            Database.Data.setTransactionSuccessful();
                        } catch (Exception exc) {
                            exc.printStackTrace();
                        }
                        Database.Data.endTransaction();
                    }
                    if (checkBoxPreloadImages.isChecked()) {
                        importer.importImages(Global.LastFilter.getSqlWhere(), ip);
                    }
                    Thread.sleep(1000);
                    if (checkBoxImportMaps.isChecked()) importer.importMaps();
                    if (importCancel) Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!importCancel) {
                    ProgressDialog.Ready();
                    ProgressHandler.post(ProgressReady);
                }
            }
