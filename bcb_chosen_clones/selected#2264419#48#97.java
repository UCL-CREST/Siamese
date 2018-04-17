    public Object process(Atom oAtm) throws IOException {
        File oFile;
        FileReader oFileRead;
        String sPathHTML;
        char cBuffer[];
        Object oReplaced;
        final String sSep = System.getProperty("file.separator");
        if (DebugFile.trace) {
            DebugFile.writeln("Begin FileDumper.process([Job:" + getStringNull(DB.gu_job, "") + ", Atom:" + String.valueOf(oAtm.getInt(DB.pg_atom)) + "])");
            DebugFile.incIdent();
        }
        if (bHasReplacements) {
            sPathHTML = getProperty("workareasput");
            if (!sPathHTML.endsWith(sSep)) sPathHTML += sSep;
            sPathHTML += getParameter("gu_workarea") + sSep + "apps" + sSep + "Mailwire" + sSep + "html" + sSep + getParameter("gu_pageset") + sSep;
            sPathHTML += getParameter("nm_pageset").replace(' ', '_') + ".html";
            if (DebugFile.trace) DebugFile.writeln("PathHTML = " + sPathHTML);
            oReplaced = oReplacer.replace(sPathHTML, oAtm.getItemMap());
            bHasReplacements = (oReplacer.lastReplacements() > 0);
        } else {
            oReplaced = null;
            if (null != oFileStr) oReplaced = oFileStr.get();
            if (null == oReplaced) {
                sPathHTML = getProperty("workareasput");
                if (!sPathHTML.endsWith(sSep)) sPathHTML += sSep;
                sPathHTML += getParameter("gu_workarea") + sSep + "apps" + sSep + "Mailwire" + sSep + "html" + sSep + getParameter("gu_pageset") + sSep + getParameter("nm_pageset").replace(' ', '_') + ".html";
                if (DebugFile.trace) DebugFile.writeln("PathHTML = " + sPathHTML);
                oFile = new File(sPathHTML);
                cBuffer = new char[new Long(oFile.length()).intValue()];
                oFileRead = new FileReader(oFile);
                oFileRead.read(cBuffer);
                oFileRead.close();
                if (DebugFile.trace) DebugFile.writeln(String.valueOf(cBuffer.length) + " characters readed");
                oReplaced = new String(cBuffer);
                oFileStr = new SoftReference(oReplaced);
            }
        }
        String sPathJobDir = getProperty("storage");
        if (!sPathJobDir.endsWith(sSep)) sPathJobDir += sSep;
        sPathJobDir += "jobs" + sSep + getParameter("gu_workarea") + sSep + getString(DB.gu_job) + sSep;
        FileWriter oFileWrite = new FileWriter(sPathJobDir + getString(DB.gu_job) + "_" + String.valueOf(oAtm.getInt(DB.pg_atom)) + ".html", true);
        oFileWrite.write((String) oReplaced);
        oFileWrite.close();
        iPendingAtoms--;
        if (DebugFile.trace) {
            DebugFile.writeln("End FileDumper.process([Job:" + getStringNull(DB.gu_job, "") + ", Atom:" + String.valueOf(oAtm.getInt(DB.pg_atom)) + "])");
            DebugFile.decIdent();
        }
        return oReplaced;
    }
