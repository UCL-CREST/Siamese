    public static SplineMesh subdivideMesh(SplineMesh mesh, double tol) {
        SplineMesh newmesh = new SplineMesh();
        int usize = mesh.usize, vsize = mesh.vsize;
        MeshVertex v[][] = new MeshVertex[vsize][usize], newv[][], temp;
        int numParam = (mesh.texParam == null ? 0 : mesh.texParam.length);
        double param[][][] = new double[vsize][usize][numParam], newparam[][][];
        float newus[];
        Object output[];
        for (int i = 0; i < usize; i++) for (int j = 0; j < vsize; j++) v[j][i] = new MeshVertex(mesh.vertex[i + usize * j]);
        for (int k = 0; k < numParam; k++) if (mesh.paramValue[k] instanceof VertexParameterValue) {
            double val[] = ((VertexParameterValue) mesh.paramValue[k]).getValue();
            for (int i = 0; i < usize; i++) for (int j = 0; j < vsize; j++) param[j][i][k] = val[i + usize * j];
        }
        if (usize == 2) output = new Object[] { v, mesh.usmoothness, param }; else if (mesh.smoothingMethod == INTERPOLATING) output = interpOneAxis(v, mesh.usmoothness, param, mesh.uclosed, tol); else output = approxOneAxis(v, mesh.usmoothness, param, mesh.uclosed, tol);
        newv = (MeshVertex[][]) output[0];
        newus = (float[]) output[1];
        newparam = (double[][][]) output[2];
        v = new MeshVertex[newv[0].length][newv.length];
        for (int i = 0; i < newv.length; i++) for (int j = 0; j < newv[0].length; j++) v[j][i] = newv[i][j];
        param = new double[newparam[0].length][newparam.length][newparam[0][0].length];
        for (int i = 0; i < newparam.length; i++) for (int j = 0; j < newparam[0].length; j++) for (int k = 0; k < newparam[0][0].length; k++) param[j][i][k] = newparam[i][j][k];
        if (vsize == 2) output = new Object[] { v, mesh.vsmoothness, param }; else if (mesh.smoothingMethod == INTERPOLATING) output = interpOneAxis(v, mesh.vsmoothness, param, mesh.vclosed, tol); else output = approxOneAxis(v, mesh.vsmoothness, param, mesh.vclosed, tol);
        v = (MeshVertex[][]) output[0];
        newmesh.usize = v.length;
        newmesh.vsize = v[0].length;
        newmesh.vertex = new MeshVertex[newmesh.usize * newmesh.vsize];
        for (int i = 0; i < newmesh.usize; i++) for (int j = 0; j < newmesh.vsize; j++) newmesh.vertex[i + newmesh.usize * j] = v[i][j];
        newmesh.usmoothness = newus;
        newmesh.vsmoothness = (float[]) output[1];
        newmesh.uclosed = mesh.uclosed;
        newmesh.vclosed = mesh.vclosed;
        newmesh.smoothingMethod = mesh.smoothingMethod;
        newmesh.skeleton = mesh.skeleton.duplicate();
        newmesh.copyTextureAndMaterial(mesh);
        param = (double[][][]) output[2];
        for (int k = 0; k < numParam; k++) if (newmesh.paramValue[k] instanceof VertexParameterValue) {
            double val[] = new double[newmesh.usize * newmesh.vsize];
            for (int i = 0; i < newmesh.usize; i++) for (int j = 0; j < newmesh.vsize; j++) val[i + newmesh.usize * j] = param[i][j][k];
            newmesh.paramValue[k] = new VertexParameterValue(val);
        }
        return newmesh;
    }
