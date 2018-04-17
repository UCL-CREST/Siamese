    public void readData(int choice) throws IOException {
        for (i = 0; i < max; i++) for (j = 0; j < max; j++) {
            phase_x[i][j] = 0.0;
            phase_y[i][j] = 0.0;
        }
        URL url;
        InputStream is;
        InputStreamReader isr;
        if (choice == 0) {
            url = getClass().getResource("resources/Phase_623_620_Achromat.dat");
            is = url.openStream();
            isr = new InputStreamReader(is);
        } else {
            url = getClass().getResource("resources/Phase_623_620_NoAchromat.dat");
            is = url.openStream();
            isr = new InputStreamReader(is);
        }
        BufferedReader br = new BufferedReader(isr);
        s = br.readLine();
        StringTokenizer st = new StringTokenizer(s);
        i = 0;
        j = 0;
        phase_x[i][j] = 4 * Double.parseDouble(st.nextToken());
        phase_y[i][j] = 4 * Double.parseDouble(st.nextToken());
        xgridmin = phase_x[i][j];
        ygridmin = phase_y[i][j];
        temp_prev = phase_x[i][j];
        kd[i][j] = Double.parseDouble(st.nextToken());
        kfs[i][j] = Double.parseDouble(st.nextToken());
        kfl[i][j] = Double.parseDouble(st.nextToken());
        kdee[i][j] = Double.parseDouble(st.nextToken());
        kdc[i][j] = Double.parseDouble(st.nextToken());
        kfc[i][j] = Double.parseDouble(st.nextToken());
        j++;
        int k = 0;
        while ((s = br.readLine()) != null) {
            st = new StringTokenizer(s);
            temp_new = 4 * Double.parseDouble(st.nextToken());
            if (temp_new != temp_prev) {
                temp_prev = temp_new;
                i++;
                j = 0;
            }
            phase_x[i][j] = temp_new;
            phase_y[i][j] = 4 * Double.parseDouble(st.nextToken());
            kd[i][j] = Double.parseDouble(st.nextToken());
            kfs[i][j] = Double.parseDouble(st.nextToken());
            kfl[i][j] = Double.parseDouble(st.nextToken());
            kdee[i][j] = Double.parseDouble(st.nextToken());
            kdc[i][j] = Double.parseDouble(st.nextToken());
            kfc[i][j] = Double.parseDouble(st.nextToken());
            imax = i;
            jmax = j;
            j++;
            k++;
        }
        xgridmax = phase_x[i][j - 1];
        ygridmax = phase_y[i][j - 1];
    }
