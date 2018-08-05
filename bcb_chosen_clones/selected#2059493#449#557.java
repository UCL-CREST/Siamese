    @Override
    public void save(File folder) {
        actInstance = instance;
        this.setProperty(EsomMapper.PROPERTY_INSTANCE, String.valueOf(actInstance));
        log.debug("instance: " + this.getProperty(EsomMapper.PROPERTY_INSTANCE));
        if (this.getProperty(EsomMapper.PROPERTY_LRN_RADIO_SELECTED) == EsomMapper.RADIO_LOAD_SELECTED) {
            File src = new File(this.getProperty(EsomMapper.PROPERTY_LRN_FILE));
            if (src.getParent() != folder.getPath()) {
                log.debug("saving lrn file in save folder " + folder.getPath());
                File dst = new File(folder.getAbsolutePath() + File.separator + src.getName() + String.valueOf(actInstance));
                try {
                    FileReader fr = new FileReader(src);
                    BufferedReader br = new BufferedReader(fr);
                    dst.createNewFile();
                    FileWriter fw = new FileWriter(dst);
                    BufferedWriter bw = new BufferedWriter(fw);
                    int i = 0;
                    while ((i = br.read()) != -1) bw.write(i);
                    bw.flush();
                    bw.close();
                    br.close();
                    fr.close();
                } catch (FileNotFoundException e) {
                    log.error("Error while opening lrn sourcefile! Saving wasn't possible!!!");
                    e.printStackTrace();
                } catch (IOException e) {
                    log.error("Error while creating lrn destfile! Creating wasn't possible!!!");
                    e.printStackTrace();
                }
                this.setProperty(EsomMapper.PROPERTY_LRN_FILE, dst.getName());
                log.debug("done saving lrn file");
            }
        }
        if (this.getProperty(EsomMapper.PROPERTY_WTS_RADIO_SELECTED) == EsomMapper.RADIO_LOAD_SELECTED) {
            File src = new File(this.getProperty(EsomMapper.PROPERTY_WTS_FILE));
            if (src.getParent() != folder.getPath()) {
                log.debug("saving wts file in save folder " + folder.getPath());
                File dst = new File(folder.getAbsolutePath() + File.separator + src.getName() + String.valueOf(actInstance));
                try {
                    FileReader fr = new FileReader(src);
                    BufferedReader br = new BufferedReader(fr);
                    dst.createNewFile();
                    FileWriter fw = new FileWriter(dst);
                    BufferedWriter bw = new BufferedWriter(fw);
                    int i = 0;
                    while ((i = br.read()) != -1) bw.write(i);
                    bw.flush();
                    bw.close();
                    br.close();
                    fr.close();
                } catch (FileNotFoundException e) {
                    log.error("Error while opening wts sourcefile! Saving wasn't possible!!!");
                    e.printStackTrace();
                } catch (IOException e) {
                    log.error("Error while creating wts destfile! Creating wasn't possible!!!");
                    e.printStackTrace();
                }
                this.setProperty(EsomMapper.PROPERTY_WTS_FILE, dst.getName());
                log.debug("done saving wts file");
            }
        }
        if (this.getProperty(EsomMapper.PROPERTY_LRN_RADIO_SELECTED) == EsomMapper.RADIO_SELECT_FROM_DATANAV_SELECTED) {
            this.setProperty(EsomMapper.PROPERTY_LRN_FILE, "EsomMapper" + this.actInstance + ".lrn");
            File dst = new File(folder + File.separator + this.getProperty(EsomMapper.PROPERTY_LRN_FILE));
            try {
                FileWriter fw = new FileWriter(dst);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("# EsomMapper LRN save file\n");
                bw.write("% " + this.inputVectors.getNumRows() + "\n");
                bw.write("% " + this.inputVectors.getNumCols() + "\n");
                bw.write("% 9");
                for (IColumn col : this.inputVectors.getColumns()) {
                    if (col.getType() == IClusterNumber.class) bw.write("\t2"); else if (col.getType() == String.class) bw.write("\t8"); else bw.write("\t1");
                }
                bw.write("\n% Key");
                for (IColumn col : this.inputVectors.getColumns()) {
                    bw.write("\t" + col.getLabel());
                }
                bw.write("\n");
                int keyIterator = 0;
                for (Vector<Object> row : this.inputVectors.getGrid()) {
                    bw.write(this.inputVectors.getKey(keyIterator++).toString());
                    for (Object point : row) bw.write("\t" + point.toString());
                    bw.write("\n");
                }
                bw.flush();
                fw.flush();
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.setProperty(EsomMapper.PROPERTY_LRN_RADIO_SELECTED, EsomMapper.RADIO_LOAD_SELECTED);
        }
        if (this.getProperty(EsomMapper.PROPERTY_WTS_RADIO_SELECTED) == EsomMapper.RADIO_SELECT_FROM_DATANAV_SELECTED) {
            this.setProperty(EsomMapper.PROPERTY_WTS_FILE, "EsomMapper" + this.actInstance + ".wts");
            MyRetina tempRetina = new MyRetina(this.outputRetina.getNumRows(), this.outputRetina.getNumCols(), this.outputRetina.getDim(), this.outputRetina.getDistanceFunction(), this.outputRetina.isToroid());
            for (int row = 0; row < this.outputRetina.getNumRows(); row++) {
                for (int col = 0; col < this.outputRetina.getNumCols(); col++) {
                    for (int dim = 0; dim < this.outputRetina.getDim(); dim++) {
                        tempRetina.setNeuron(row, col, dim, this.outputRetina.getPointasDoubleArray(row, col)[dim]);
                    }
                }
            }
            EsomIO.writeWTSFile(folder + File.separator + this.getProperty(EsomMapper.PROPERTY_WTS_FILE), tempRetina);
            this.setProperty(EsomMapper.PROPERTY_WTS_RADIO_SELECTED, EsomMapper.RADIO_LOAD_SELECTED);
        }
        EsomMapper.instance++;
    }
