    void subdivideCommand() {
        SplineMesh theMesh = (SplineMesh) objInfo.object;
        MeshVertex vt[] = theMesh.getVertices();
        float us[] = theMesh.getUSmoothness(), vs[] = theMesh.getVSmoothness(), newus[], newvs[];
        boolean newsel[], splitu[], splitv[];
        MeshVertex v[][], newv[][];
        double param[][][], newparam[][][];
        int i, j, usplitcount = 0, vsplitcount = 0;
        int method = theMesh.getSmoothingMethod(), usize = theMesh.getUSize(), vsize = theMesh.getVSize();
        int numParam = (theMesh.getParameters() == null ? 0 : theMesh.getParameters().length);
        if (selectMode != CURVE_MODE) return;
        for (i = 0; !selected[i] && i < selected.length; i++) ;
        if (i == selected.length) return;
        if (theMesh.isUClosed()) splitu = new boolean[usize]; else splitu = new boolean[usize - 1];
        for (i = 0; i < splitu.length; i++) if (selected[i] && selected[(i + 1) % usize]) {
            splitu[i] = true;
            usplitcount++;
        }
        if (theMesh.isVClosed()) splitv = new boolean[vsize]; else splitv = new boolean[vsize - 1];
        for (i = 0; i < splitv.length; i++) if (selected[i + usize] && selected[(i + 1) % vsize + usize]) {
            splitv[i] = true;
            vsplitcount++;
        }
        newus = new float[usize + usplitcount];
        newvs = new float[vsize + vsplitcount];
        newsel = new boolean[selected.length + usplitcount + vsplitcount];
        v = new MeshVertex[vsize][usize];
        for (i = 0; i < usize; i++) for (j = 0; j < vsize; j++) v[j][i] = vt[i + j * usize];
        newv = new MeshVertex[vsize][usize + usplitcount];
        param = new double[vsize][usize][numParam];
        for (int k = 0; k < numParam; k++) if (theMesh.getParameterValues()[k] instanceof VertexParameterValue) {
            double val[] = ((VertexParameterValue) theMesh.getParameterValues()[k]).getValue();
            for (i = 0; i < usize; i++) for (j = 0; j < vsize; j++) param[j][i][k] = val[i + usize * j];
        }
        newparam = new double[vsize][usize + usplitcount][numParam];
        splitOneAxis(v, newv, us, newus, splitu, param, newparam, theMesh.isUClosed());
        v = new MeshVertex[usize + usplitcount][vsize];
        for (i = 0; i < v.length; i++) for (j = 0; j < v[i].length; j++) v[i][j] = newv[j][i];
        newv = new MeshVertex[usize + usplitcount][vsize + vsplitcount];
        param = new double[usize + usplitcount][vsize][numParam];
        for (i = 0; i < param.length; i++) for (j = 0; j < param[i].length; j++) for (int k = 0; k < param[i][j].length; k++) param[i][j][k] = newparam[j][i][k];
        newparam = new double[usize + usplitcount][vsize + vsplitcount][numParam];
        splitOneAxis(v, newv, vs, newvs, splitv, param, newparam, theMesh.isVClosed());
        for (i = 0, j = 0; i < usize; i++) {
            if (selected[i]) newsel[j] = true;
            if (i < splitu.length && splitu[i]) newsel[++j] = true;
            j++;
        }
        for (i = 0, j = 0; i < vsize; i++) {
            if (selected[i + usize]) newsel[j + usize + usplitcount] = true;
            if (i < splitv.length && splitv[i]) newsel[++j + usize + usplitcount] = true;
            j++;
        }
        setUndoRecord(new UndoRecord(this, false, UndoRecord.COPY_OBJECT, new Object[] { theMesh, theMesh.duplicate() }));
        theMesh.setShape(newv, newus, newvs);
        for (int k = 0; k < numParam; k++) if (theMesh.getParameterValues()[k] instanceof VertexParameterValue) {
            double val[] = new double[newus.length * newvs.length];
            for (i = 0; i < newus.length; i++) for (j = 0; j < newvs.length; j++) val[i + newus.length * j] = newparam[i][j][k];
            theMesh.setParameterValue(theMesh.getParameters()[k], new VertexParameterValue(val));
        }
        setMesh(theMesh);
        setSelection(newsel);
    }
