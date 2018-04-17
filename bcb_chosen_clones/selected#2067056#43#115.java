    public boolean ReadFile() {
        boolean ret = false;
        FilenameFilter FileFilter = null;
        File dir = new File(fDir);
        String[] FeeFiles;
        int Lines = 0;
        BufferedReader FeeFile = null;
        PreparedStatement DelSt = null, InsSt = null;
        String Line = null, Term = null, CurTerm = null, TermType = null, Code = null;
        double[] Fee = new double[US_D + 1];
        double FeeAm = 0;
        String UpdateSt = "INSERT INTO reporter.term_fee (TERM, TERM_TYPE, THEM_VC,	THEM_VE, THEM_EC, THEM_EE, THEM_D," + "BA_VC, BA_VE, BA_EC, BA_EE, BA_D," + "US_VC, US_VE, US_EC, US_EE, US_D)" + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            FileFilter = new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    if ((new File(dir, name)).isDirectory()) return false; else return (name.matches(fFileMask));
                }
            };
            FeeFiles = dir.list(FileFilter);
            java.util.Arrays.sort(FeeFiles);
            System.out.println(FeeFiles[FeeFiles.length - 1] + " " + (new SimpleDateFormat("dd.MM.yy HH:mm:ss")).format(new Date()));
            Log.info(String.format("Load = %1s", fDir + FeeFiles[FeeFiles.length - 1]));
            FeeFile = new BufferedReader(new FileReader(fDir + FeeFiles[FeeFiles.length - 1]));
            FeeZero(Fee);
            DelSt = cnProd.prepareStatement("delete from reporter.term_fee");
            DelSt.executeUpdate();
            InsSt = cnProd.prepareStatement(UpdateSt);
            WriteTerm(FeeFiles[FeeFiles.length - 1] + " " + (new SimpleDateFormat("dd.MM.yy HH:mm:ss")).format(new Date()), "XXX", Fee, InsSt);
            while ((Line = FeeFile.readLine()) != null) {
                Lines++;
                if (!Line.matches("\\d{15}\\s+��������.+")) continue;
                Term = Line.substring(7, 15);
                if ((CurTerm == null) || !Term.equals(CurTerm)) {
                    if (CurTerm != null) {
                        WriteTerm(CurTerm, TermType, Fee, InsSt);
                    }
                    CurTerm = Term;
                    if (Line.indexOf("���") > 0) TermType = "���"; else TermType = "���";
                    FeeZero(Fee);
                }
                Code = Line.substring(64, 68).trim().toUpperCase();
                if (Code.equals("ST") || Code.equals("AC") || Code.equals("8110") || Code.equals("8160")) continue;
                FeeAm = new Double(Line.substring(140, 160)).doubleValue();
                if (Line.indexOf("�� ����� ������") > 0) SetFee(Fee, CARD_THEM, Code, FeeAm); else if (Line.indexOf("�� ������ �����") > 0) SetFee(Fee, CARD_BA, Code, FeeAm); else if (Line.indexOf("�� ������ ��") > 0) SetFee(Fee, CARD_US, Code, FeeAm); else throw new Exception("������ ���� ����.:" + Line);
            }
            WriteTerm(CurTerm, TermType, Fee, InsSt);
            cnProd.commit();
            ret = true;
        } catch (Exception e) {
            System.out.printf("Err = %1s\r\n", e.getMessage());
            Log.error(String.format("Err = %1s", e.getMessage()));
            Log.error(String.format("Line = %1s", Line));
            try {
                cnProd.rollback();
            } catch (Exception ee) {
            }
            ;
        } finally {
            try {
                if (FeeFile != null) FeeFile.close();
            } catch (Exception ee) {
            }
        }
        try {
            if (DelSt != null) DelSt.close();
            if (InsSt != null) InsSt.close();
            cnProd.setAutoCommit(true);
        } catch (Exception ee) {
        }
        Log.info(String.format("Lines = %1d", Lines));
        return (ret);
    }
