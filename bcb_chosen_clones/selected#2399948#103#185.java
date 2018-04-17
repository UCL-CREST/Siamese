    private void save(int pid, Data data, int level, int index) throws BadFormException, RemoteException, VisADException, HDF5Exception {
        if (data instanceof Tuple) {
            int g_idx = 0, new_pid = 0;
            Data d = null;
            Tuple tuple = (Tuple) data;
            String gname = "Group" + index + "at" + level;
            if (level == 0) {
                new_pid = pid;
                g_idx = -1;
            } else {
                new_pid = H5.H5Gcreate(pid, gname, -1);
            }
            int n = tuple.getDimension();
            for (int i = 0; i < n; i++) {
                d = tuple.getComponent(i);
                save(new_pid, d, level + 1, g_idx++);
            }
        } else if (data instanceof Field) {
            Field field = (Field) data;
            RealType[] rTypes = ((FunctionType) field.getType()).getRealComponents();
            Set dset = field.getDomainSet();
            if (!(dset instanceof GriddedSet) || rTypes == null) return;
            GriddedSet domain = (GriddedSet) dset;
            RealType rangeType = (RealType) rTypes[0];
            int sid = 0, did = 0, tid = 0;
            int l = domain.getLength();
            int[] ddims = domain.getLengths();
            int rank = ddims.length;
            long[] dims = new long[rank];
            for (int i = 0; i < rank; i++) dims[i] = ddims[i];
            sid = H5.H5Screate_simple(rank, dims, null);
            int number_of_range_components = 1;
            if (field.isFlatField()) number_of_range_components = ((FlatField) field).getRangeDimension(); else number_of_range_components = ((Unit[]) field.getDefaultRangeUnits()).length;
            float[] rangeValues = new float[l];
            float[][] rValue = field.getFloats(false);
            if (number_of_range_components == 1) {
                for (int i = 0; i < l; i++) rangeValues[i] = rValue[0][i];
                try {
                    did = H5.H5Dcreate(pid, rangeType.getName(), H5.J2C(HDF5CDataTypes.JH5T_NATIVE_FLOAT), sid, HDF5Constants.H5P_DEFAULT);
                    H5.H5Dwrite(did, H5.J2C(HDF5CDataTypes.JH5T_NATIVE_FLOAT), HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, rangeValues);
                } finally {
                    H5.H5Dclose(did);
                    H5.H5Sclose(sid);
                }
            } else {
                float[][] fValue = new float[l][number_of_range_components];
                for (int i = 0; i < l; i++) {
                    for (int j = 0; j < number_of_range_components; j++) fValue[i][j] = rValue[j][i];
                }
                try {
                    tid = H5.H5Tcreate(HDF5Constants.H5T_COMPOUND, number_of_range_components * 4);
                    for (int j = 0; j < number_of_range_components; j++) {
                        rangeType = (RealType) rTypes[j];
                        H5.H5Tinsert(tid, rangeType.getName(), j * 4, H5.J2C(HDF5CDataTypes.JH5T_NATIVE_FLOAT));
                    }
                    String dname = "Compound" + index + "at" + level;
                    did = H5.H5Dcreate(pid, dname, tid, sid, HDF5Constants.H5P_DEFAULT);
                    H5.H5Dwrite(did, tid, sid, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, fValue);
                } finally {
                    H5.H5Tclose(tid);
                    H5.H5Dclose(did);
                    H5.H5Sclose(sid);
                }
            }
        } else if (data instanceof Text) {
            Text text = (Text) data;
            String text_value = text.getValue();
            TextType tt = (TextType) text.getType();
            String text_name = tt.getName();
            int max_length = text_value.length();
            long[] dims_str = { 1 };
            int dataspace = H5.H5Screate_simple(1, dims_str, null);
            int datatype = H5.H5Tcopy(H5.J2C(HDF5CDataTypes.JH5T_C_S1));
            H5.H5Tset_size(datatype, max_length);
            H5.H5Tset_strpad(datatype, HDF5Constants.H5T_STR_NULLPAD);
            int dataset = H5.H5Dcreate(pid, text_name, datatype, dataspace, HDF5Constants.H5P_DEFAULT);
            byte[][] bnotes = new byte[1][max_length];
            bnotes[0] = text_value.getBytes();
            H5.H5Dwrite(dataset, datatype, HDF5Constants.H5S_ALL, HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, bnotes);
            H5.H5Dclose(dataset);
            bnotes = null;
        }
    }
