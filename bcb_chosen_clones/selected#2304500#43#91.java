    public double[][] ComputeMatrix(String[] filesList) throws Exception {
        int n = filesList.length;
        fDistanceMatrix = new double[n][n];
        htCSize = new Hashtable();
        Random rand = new Random(100000);
        ArrayList alTempFiles = new ArrayList();
        for (int i = 0; i < n; i++) {
            File f1 = new File(filesList[i]);
            FileInputStream fis = new FileInputStream(f1);
            byte[] file_data = new byte[(int) f1.length()];
            fis.read(file_data);
            fis.close();
            File fCompX = comp.Compress(f1);
            alTempFiles.add(fCompX);
            htCSize.put("c:" + i, fCompX);
            for (int j = i; j < n; j++) {
                File f2 = new File(filesList[j]);
                fis = new FileInputStream(f2);
                byte[] file_data2 = new byte[(int) f2.length()];
                fis.read(file_data2);
                fis.close();
                String two_files_name = rand.nextInt() + "_two_files.dat";
                FileOutputStream fos = new FileOutputStream(two_files_name);
                fos.write(file_data);
                fos.write(file_data2);
                fos.close();
                File two_files = new File(two_files_name);
                File fCompXY = comp.Compress(two_files);
                alTempFiles.add(fCompXY);
                htCSize.put("c:" + i + ":" + j, fCompXY);
                alTempFiles.add(two_files);
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                File c_x = (File) htCSize.get("c:" + i);
                File c_y = (File) htCSize.get("c:" + j);
                File c_xy = (File) htCSize.get("c:" + i + ":" + j);
                fDistanceMatrix[i][j] = dmetric.Compute(c_x, c_y, c_xy);
                fDistanceMatrix[j][i] = fDistanceMatrix[i][j];
            }
        }
        bComputed = true;
        for (int i = 0; i < alTempFiles.size(); i++) {
            File f = (File) alTempFiles.get(i);
            f.delete();
        }
        return fDistanceMatrix;
    }
