    public void readData() throws IOException {
        i = 0;
        j = 0;
        URL url = getClass().getResource("resources/Chrom_623_620.dat");
        InputStream is = url.openStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        s = br.readLine();
        StringTokenizer st = new StringTokenizer(s);
        s = br.readLine();
        st = new StringTokenizer(s);
        chrom_x[i][j] = Double.parseDouble(st.nextToken());
        temp_prev = chrom_x[i][j];
        chrom_y[i][j] = Double.parseDouble(st.nextToken());
        gridmin = chrom_x[i][j];
        gridmax = chrom_x[i][j];
        sext1[i][j] = Double.parseDouble(st.nextToken());
        sext2[i][j] = Double.parseDouble(st.nextToken());
        sext3[i][j] = Double.parseDouble(st.nextToken());
        sext4[i][j] = Double.parseDouble(st.nextToken());
        j++;
        while ((s = br.readLine()) != null) {
            st = new StringTokenizer(s);
            temp_new = Double.parseDouble(st.nextToken());
            if (temp_new != temp_prev) {
                temp_prev = temp_new;
                i++;
                j = 0;
            }
            chrom_x[i][j] = temp_new;
            chrom_y[i][j] = Double.parseDouble(st.nextToken());
            sext1[i][j] = Double.parseDouble(st.nextToken());
            sext2[i][j] = Double.parseDouble(st.nextToken());
            sext3[i][j] = Double.parseDouble(st.nextToken());
            sext4[i][j] = Double.parseDouble(st.nextToken());
            imax = i;
            jmax = j;
            j++;
            if (chrom_x[i][j] <= gridmin) gridmin = chrom_x[i][j];
            if (chrom_x[i][j] >= gridmax) gridmax = chrom_x[i][j];
        }
    }
