    private void processar() {
        boolean bOK = false;
        String sSQL = "DELETE FROM FNSALDOLANCA WHERE CODEMP=? AND CODFILIAL=?";
        try {
            state("Excluindo base atual de saldos...");
            PreparedStatement ps = con.prepareStatement(sSQL);
            ps.setInt(1, Aplicativo.iCodEmp);
            ps.setInt(2, ListaCampos.getMasterFilial("FNSALDOLANCA"));
            ps.executeUpdate();
            ps.close();
            state("Base excluida...");
            bOK = true;
        } catch (SQLException err) {
            Funcoes.mensagemErro(this, "Erro ao excluir os saldos!\n" + err.getMessage(), true, con, err);
            err.printStackTrace();
        }
        if (bOK) {
            bOK = false;
            sSQL = "SELECT CODPLAN,DATASUBLANCA,SUM(VLRSUBLANCA) VLRSUBLANCA FROM " + "FNSUBLANCA WHERE CODEMP=? AND CODFILIAL=? GROUP BY CODPLAN,DATASUBLANCA " + "ORDER BY CODPLAN,DATASUBLANCA";
            try {
                state("Iniciando reconstru��o...");
                PreparedStatement ps = con.prepareStatement(sSQL);
                ps.setInt(1, Aplicativo.iCodEmp);
                ps.setInt(2, ListaCampos.getMasterFilial("FNLANCA"));
                ResultSet rs = ps.executeQuery();
                String sPlanAnt = "";
                double dSaldo = 0;
                bOK = true;
                int iFilialPlan = ListaCampos.getMasterFilial("FNPLANEJAMENTO");
                int iFilialSaldo = ListaCampos.getMasterFilial("FNSALDOLANCA");
                while (rs.next() && bOK) {
                    if ("1010100000004".equals(rs.getString("CodPlan"))) {
                        System.out.println("Debug");
                    }
                    if (sPlanAnt.equals(rs.getString("CodPlan"))) {
                        dSaldo += rs.getDouble("VLRSUBLANCA");
                    } else dSaldo = rs.getDouble("VLRSUBLANCA");
                    bOK = insereSaldo(iFilialSaldo, iFilialPlan, rs.getString("CodPlan"), rs.getDate("DataSubLanca"), dSaldo);
                    sPlanAnt = rs.getString("CodPlan");
                    if ("1010100000004".equals(sPlanAnt)) {
                        System.out.println("Debug");
                    }
                }
                ps.close();
                state("Aguardando grava��o final...");
            } catch (SQLException err) {
                bOK = false;
                Funcoes.mensagemErro(this, "Erro ao excluir os lan�amentos!\n" + err.getMessage(), true, con, err);
                err.printStackTrace();
            }
        }
        try {
            if (bOK) {
                con.commit();
                state("Registros processados com sucesso!");
            } else {
                state("Registros antigos restaurados!");
                con.rollback();
            }
        } catch (SQLException err) {
            Funcoes.mensagemErro(this, "Erro ao relizar precedimento!\n" + err.getMessage(), true, con, err);
            err.printStackTrace();
        }
        bRunProcesso = false;
        btProcessar.setEnabled(true);
    }
