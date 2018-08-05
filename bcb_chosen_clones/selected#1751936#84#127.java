    public void writeData(String name, int items, int mzmin, int mzmax, long tstart, long tdelta, int[] peaks) {
        PrintWriter file = getWriter(name + ".txt");
        file.print("Filename\t");
        file.print("Date\t");
        file.print("Acquisition #\t");
        file.print("�m Diameter\t");
        for (int i = mzmin; i <= mzmax; i++) file.print(i + "\t");
        file.println();
        int nothing = 0;
        String fileLoc = "C:/abcd/" + name + ".txt\t";
        Date tempDate;
        for (int i = 0; i < items; i++) {
            tempDate = new Date(tstart);
            tstart += tdelta;
            file.print(fileLoc);
            file.print(dateFormat.format(tempDate) + "\t");
            file.print(i + 1 + "\t");
            double t = (double) (i) / 10;
            file.print(t + "\t");
            boolean peaked = false;
            for (int k = mzmin; k <= mzmax; k++) {
                for (int j = 0; j < peaks.length && !peaked; j++) {
                    if (k == peaks[j]) {
                        file.print(peakVals[j % peakVals.length] + "\t");
                        peaked = true;
                    }
                }
                if (!peaked) {
                    if (k == mzmax) file.print(nothing); else file.print(nothing + "\t");
                }
                peaked = false;
            }
            file.println();
        }
        try {
            Scanner test = new Scanner(f);
            while (test.hasNext()) {
                System.out.println(test.nextLine());
            }
            System.out.println("test");
        } catch (Exception e) {
        }
        file.close();
    }
