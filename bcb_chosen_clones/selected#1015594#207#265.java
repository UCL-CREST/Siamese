    private void DrawModel(Graphics offg, int obj_num, boolean object, float h, float s, int vt_num, int fc_num) {
        int px[] = new int[3];
        int py[] = new int[3];
        int count = 0;
        int tmp[] = new int[fc_num];
        double tmp_depth[] = new double[fc_num];
        rotate(vt_num);
        offg.setColor(Color.black);
        for (int i = 0; i < fc_num; i++) {
            double a1 = fc[i].vt1.x - fc[i].vt0.x;
            double a2 = fc[i].vt1.y - fc[i].vt0.y;
            double a3 = fc[i].vt1.z - fc[i].vt0.z;
            double b1 = fc[i].vt2.x - fc[i].vt1.x;
            double b2 = fc[i].vt2.y - fc[i].vt1.y;
            double b3 = fc[i].vt2.z - fc[i].vt1.z;
            fc[i].nx = a2 * b3 - a3 * b2;
            fc[i].ny = a3 * b1 - a1 * b3;
            fc[i].nz = a1 * b2 - a2 * b1;
            if (fc[i].nz < 0) {
                fc[i].nx = a2 * b3 - a3 * b2;
                fc[i].ny = a3 * b1 - a1 * b3;
                tmp[count] = i;
                tmp_depth[count] = fc[i].getDepth();
                count++;
            }
        }
        int lim = count - 1;
        do {
            int m = 0;
            for (int n = 0; n <= lim - 1; n++) {
                if (tmp_depth[n] < tmp_depth[n + 1]) {
                    double t = tmp_depth[n];
                    tmp_depth[n] = tmp_depth[n + 1];
                    tmp_depth[n + 1] = t;
                    int ti = tmp[n];
                    tmp[n] = tmp[n + 1];
                    tmp[n + 1] = ti;
                    m = n;
                }
            }
            lim = m;
        } while (lim != 0);
        for (int m = 0; m < count; m++) {
            int i = tmp[m];
            double l = Math.sqrt(fc[i].nx * fc[i].nx + fc[i].ny * fc[i].ny + fc[i].nz * fc[i].nz);
            test(offg, i, l, h, s);
            px[0] = (int) (fc[i].vt0.x * m_Scale + centerp.x);
            py[0] = (int) (-fc[i].vt0.y * m_Scale + centerp.y);
            px[1] = (int) (fc[i].vt1.x * m_Scale + centerp.x);
            py[1] = (int) (-fc[i].vt1.y * m_Scale + centerp.y);
            px[2] = (int) (fc[i].vt2.x * m_Scale + centerp.x);
            py[2] = (int) (-fc[i].vt2.y * m_Scale + centerp.y);
            offg.fillPolygon(px, py, 3);
        }
        if (labelFlag && object) {
            offg.setFont(Fonts.FONT_REAL);
            offg.drawString(d_con.getPointerData().getRealObjName(obj_num), (int) ((fc[0].vt0.x + 10) * m_Scale + centerp.x), (int) (-(fc[0].vt0.y + 10) * m_Scale + centerp.y));
        }
    }
