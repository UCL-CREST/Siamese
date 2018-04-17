    private void loadInformation(String fname, boolean useCtrl, int group1size) throws IOException {
        if (!useCtrl) {
            groupNames.add("A");
            groupNames.add("B");
            int c = 0;
            String[] fs = fname.split("\\s+");
            for (int i = 0; i < fs.length; i++) {
                String str = fs[i].trim();
                if (!str.equals("")) {
                    fNames.add(str);
                    if (c < group1size) nReplicates[0]++; else nReplicates[1]++;
                    c++;
                }
            }
        } else {
            BufferedReader inputStream = new BufferedReader(new FileReader(fname));
            StringTokenizer tokens;
            String str;
            int cgroup = 0;
            while ((str = inputStream.readLine()) != null && (tokens = new StringTokenizer(str)).hasMoreTokens()) {
                str = tokens.nextToken();
                if (str.equals("GROUP:")) {
                    groupNames.add(tokens.nextToken());
                    cgroup++;
                    if (cgroup > 2) System.exit(1);
                } else {
                    nReplicates[cgroup - 1]++;
                    fNames.add(str);
                }
            }
        }
        ProteinLoader pload = new ProteinLoader(fNames);
        this.proteinList = pload.getProteinList();
        for (int i = proteinList.size(); i-- > 0; ) {
            double[] counts = proteinList.get(i).getSpectralCounts();
            for (int rep = 0; rep < 2; rep++) {
                double avg = 0;
                for (int u = 0; u < nReplicates[rep]; u++) {
                    int indx = u;
                    if (rep == 1) indx += nReplicates[0];
                    avg += counts[indx];
                }
                avg /= nReplicates[rep];
                (proteinList.get(i).getProteinInfo()).add(Double.toString(avg));
            }
        }
        hdrNames.add(this.groupNames.get(0) + " avgSC");
        hdrNames.add(this.groupNames.get(1) + " avgSC");
    }
