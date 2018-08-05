    public void plot(Point me) {
        cont.clear();
        cont.bbox.setBounds(getBounds());
        aTfrqG.setToIdentity();
        aTphase.setToIdentity();
        String strResult;
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("0.#E0");
        try {
            MathAnalog.evaluate("<<Graphics`Graphics`", false);
            if (!acSweep.isEmpty()) MathAnalog.evaluate("v =" + aiSignal + "/." + acSweep + "[[1]]", false);
            MathAnalog.evaluate("nv = InterpolatingFunctionToList[v]", false);
            MathAnalog.evaluate("vAbs = Interpolation[Abs[nv]]", false);
            String deb = MathAnalog.evaluateToOutputForm("v", 0, false);
            if (!deb.startsWith("v")) {
                if (linearY) {
                    if (logarithmic) strResult = MathAnalog.evaluateToInputForm("FullGraphics[LogLinearPlot[{Abs[" + function + "[f]], vAbs[f]}, {f," + minValue + "," + maxValue + "}" + ",PlotRange->All, GridLines -> Automatic]]", 0, false); else strResult = MathAnalog.evaluateToInputForm("FullGraphics[Plot[{Abs[" + function + "[f]], vAbs[f]}, {f," + minValue + "," + maxValue + "}" + ",PlotRange->All, GridLines -> Automatic]]", 0, false);
                } else {
                    if (logarithmic) strResult = MathAnalog.evaluateToInputForm("FullGraphics[LogLinearPlot[{20*Log[10, Abs[" + function + "[f]]], 20*Log[10, vAbs[f]]}, {f," + minValue + "," + maxValue + "}" + ",PlotRange->All, GridLines -> Automatic]]", 0, false); else strResult = MathAnalog.evaluateToInputForm("FullGraphics[Plot[{20*Log[10, Abs[" + function + "[f]]], 20*Log[10, vAbs[f]]}, {f," + minValue + "," + maxValue + "}" + ",PlotRange->All, GridLines -> Automatic]]", 0, false);
                }
            } else {
                if (linearY) {
                    if (logarithmic) strResult = MathAnalog.evaluateToInputForm("FullGraphics[LogLinearPlot[{Abs[" + function + "[f]]}, {f," + minValue + "," + maxValue + "}" + ",PlotRange->All, GridLines -> Automatic]]", 0, false); else strResult = MathAnalog.evaluateToInputForm("FullGraphics[Plot[{Abs[" + function + "[f]]}, {f," + minValue + "," + maxValue + "}" + ",PlotRange->All, GridLines -> Automatic]]", 0, false);
                } else {
                    if (logarithmic) strResult = MathAnalog.evaluateToInputForm("FullGraphics[LogLinearPlot[{20*Log[10, Abs[" + function + "[f]]]}, {f," + minValue + "," + maxValue + "}" + ",PlotRange->All, GridLines -> Automatic]]", 0, false); else strResult = MathAnalog.evaluateToInputForm("FullGraphics[Plot[{20*Log[10, Abs[" + function + "[f]]]}, {f," + minValue + "," + maxValue + "}" + ",PlotRange->All, GridLines -> Automatic]]", 0, false);
                }
            }
            Pattern pattern = Pattern.compile("Line\\[[^\\]]*\\]");
            Matcher matcher = pattern.matcher(strResult);
            boolean found = false;
            CdgPolyline cdgPL = new CdgPolyline();
            GraphicsContainer frqG = new GraphicsContainer();
            GraphicsContainer phase = new GraphicsContainer();
            GraphicsContainer labelMY = new GraphicsContainer();
            GraphicsContainer labelPhX = new GraphicsContainer();
            GraphicsContainer labelPhY = new GraphicsContainer();
            txtPhaseY = new GraphicsContainer();
            txtFrqGY = new GraphicsContainer();
            txtPhaseX = new GraphicsContainer();
            txtFrqGX = new GraphicsContainer();
            double[] xpoints = null;
            double[] ypoints = null;
            while (matcher.find()) {
                found = true;
                String str = strResult.substring(matcher.start() + 7, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                String[] lines = str.split("[}], [{]");
                xpoints = new double[lines.length];
                ypoints = new double[lines.length];
                for (int i = 0; i < lines.length; i++) {
                    xpoints[i] = Double.valueOf(lines[i].split(", ")[0]);
                    ypoints[i] = Double.valueOf(lines[i].split(", ")[1]);
                }
                cdgPL = new CdgPolyline(xpoints, ypoints, xpoints.length, (byte) 0, (byte) 0, axis);
                frqG.add(cdgPL);
            }
            int index = 0;
            if (matcher.find(0)) {
                found = true;
                index = matcher.end();
                String str = strResult.substring(matcher.start() + 7, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                String[] lines = str.split("[}], [{]");
                xpoints = new double[lines.length];
                ypoints = new double[lines.length];
                for (int i = 0; i < lines.length; i++) {
                    xpoints[i] = Double.valueOf(lines[i].split(", ")[0]);
                    ypoints[i] = Double.valueOf(lines[i].split(", ")[1]);
                }
                cdgPL = new CdgPolyline(xpoints, ypoints, xpoints.length, (byte) 0, (byte) 0, simPlot);
                frqG.add(cdgPL);
            }
            if (matcher.find(index) && !deb.startsWith("v")) {
                found = true;
                String str = strResult.substring(matcher.start() + 7, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                String[] lines = str.split("[}], [{]");
                xpoints = new double[lines.length];
                ypoints = new double[lines.length];
                for (int i = 0; i < lines.length; i++) {
                    xpoints[i] = Double.valueOf(lines[i].split(", ")[0]);
                    ypoints[i] = Double.valueOf(lines[i].split(", ")[1]);
                }
                cdgPL = new CdgPolyline(xpoints, ypoints, xpoints.length, (byte) 0, (byte) 0, aiPlot);
                frqG.add(cdgPL);
            }
            frqG.calculateBounds();
            aTfrqG.scale(cont.getBounds().getWidth() / frqG.getBounds().getWidth(), 0.5 * cont.getBounds().getHeight() / frqG.getBounds().getHeight());
            ginstF = new CdgInstance(0, 0, aTfrqG, frqG);
            ginstF.setClipRegion(frqG.bbox);
            cont.add(ginstF);
            byte layerVar = 0x00B;
            pattern = Pattern.compile("Text\\[[^\\]]*0\\Q.\\E[}]\\]");
            matcher = pattern.matcher(strResult);
            while (matcher.find()) {
                found = true;
                String str = strResult.substring(matcher.start() + 5, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                str = str.split(",")[0];
                try {
                    float f = -(float) (Float.valueOf(str) * aTfrqG.getScaleY()) - (float) (ginstF.trf.getTranslateY());
                    CdgText cdgT = new CdgText((float) (frqG.getBounds().getMinX() * aTfrqG.getScaleX() + ginstF.trf.getTranslateX()), f, str, layerVar, (byte) 0, fontColor);
                    txtFrqGY.add(cdgT);
                } catch (NumberFormatException e) {
                }
            }
            pattern = Pattern.compile("Text\\[[^\\]]*1\\Q.\\E[}]\\]");
            matcher = pattern.matcher(strResult);
            while (matcher.find()) {
                found = true;
                String str = strResult.substring(matcher.start() + 5, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                str = str.split(",")[0];
                str = str.replace("0, ", "E");
                try {
                    float f;
                    if (logarithmic) {
                        if (version.startsWith("6")) f = (float) (Math.log(Float.valueOf(str)) * aTfrqG.getScaleX()); else f = (float) (Math.log10(Float.valueOf(str)) * aTfrqG.getScaleX());
                    } else f = (float) (Float.valueOf(str) * aTfrqG.getScaleX() / 1);
                    CdgText cdgT = new CdgText(f, -(float) (ginstF.trf.getTranslateY() + frqG.getBounds().getMinY() * aTfrqG.getScaleY()), str, layerVar, (byte) 0, fontColor);
                    txtFrqGX.add(cdgT);
                } catch (NumberFormatException e) {
                }
            }
            pattern = Pattern.compile("Text\\[Superscript\\[[^\\]]*\\][^\\]]*1\\Q.\\E[}]\\]");
            matcher = pattern.matcher(strResult);
            while (matcher.find()) {
                found = true;
                String str = strResult.substring(matcher.start() + 17, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                str = str.split("]")[0];
                str = str.replace("0, ", "E");
                try {
                    float f;
                    if (logarithmic) {
                        if (version.startsWith("6")) f = (float) (Math.log(Float.valueOf(str)) * aTfrqG.getScaleX()); else f = (float) (Math.log10(Float.valueOf(str)) * aTfrqG.getScaleX());
                    } else f = (float) (Float.valueOf(str) * aTfrqG.getScaleX() / 1);
                    CdgText cdgT = new CdgText(f, -(float) (ginstF.trf.getTranslateY() + frqG.getBounds().getMinY() * aTfrqG.getScaleY()), str, layerVar, (byte) 0, fontColor);
                    txtFrqGX.add(cdgT);
                } catch (NumberFormatException e) {
                }
            }
            pattern = Pattern.compile("Text\\[Superscript\\[[^\\]]*\\][^\\]]*0\\Q.\\E[}]\\]");
            matcher = pattern.matcher(strResult);
            while (matcher.find()) {
                found = true;
                String str = strResult.substring(matcher.start() + 17, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                str = str.split("]")[0];
                str = str.replace("0, ", "E");
                try {
                    float f = -(float) (Float.valueOf(str) * aTfrqG.getScaleY()) - (float) (ginstF.trf.getTranslateY());
                    CdgText cdgT = new CdgText((float) (frqG.getBounds().getMinX() * aTfrqG.getScaleX() + ginstF.trf.getTranslateX()), f, str, layerVar, (byte) 0, fontColor);
                    txtFrqGY.add(cdgT);
                } catch (NumberFormatException e) {
                }
            }
            pattern = Pattern.compile("Text\\[NumberForm\\[[^\\]]*\\][^\\]]*1\\Q.\\E[}]\\]");
            matcher = pattern.matcher(strResult);
            while (matcher.find()) {
                found = true;
                String str = strResult.substring(matcher.start() + 16, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                str = str.split("]")[0];
                str = str.replace("0, ", "E");
                try {
                    float f;
                    if (logarithmic) {
                        if (version.startsWith("6")) f = (float) (Math.log(Float.valueOf(str)) * aTfrqG.getScaleX()); else f = (float) (Math.log10(Float.valueOf(str)) * aTfrqG.getScaleX());
                    } else f = (float) (Float.valueOf(str) * aTfrqG.getScaleX() / 1);
                    CdgText cdgT = new CdgText(f, -(float) (ginstF.trf.getTranslateY() + frqG.getBounds().getMinY() * aTfrqG.getScaleY()), str, layerVar, (byte) 0, fontColor);
                    txtFrqGX.add(cdgT);
                } catch (NumberFormatException e) {
                }
            }
            pattern = Pattern.compile("Text\\[NumberForm\\[[^\\]]*\\][^\\]]*0\\Q.\\E[}]\\]");
            matcher = pattern.matcher(strResult);
            while (matcher.find()) {
                found = true;
                String str = strResult.substring(matcher.start() + 16, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                str = str.split("]")[0];
                str = str.replace("0, ", "E");
                try {
                    float f = -(float) (Float.valueOf(str) * aTfrqG.getScaleY()) - (float) (ginstF.trf.getTranslateY());
                    CdgText cdgT = new CdgText((float) (frqG.getBounds().getMinX() * aTfrqG.getScaleX() + ginstF.trf.getTranslateX()), f, str, layerVar, (byte) 0, fontColor);
                    txtFrqGY.add(cdgT);
                } catch (NumberFormatException e) {
                }
            }
            CdgText cdgTl = new CdgText((float) (ginstF.trf.getTranslateY() + frqG.getBounds().getMinY() * aTfrqG.getScaleY()), (float) (frqG.getBounds().getMinX() * aTfrqG.getScaleX() + ginstF.trf.getTranslateX()) - 10, "Magnitude", layerVar, (byte) 0, fontColor);
            labelMY.add(cdgTl);
            MathAnalog.evaluate("x1 = InterpolatingFunctionToList[" + function + "]", false);
            MathAnalog.evaluate("x2 = {{x1[[1, 1]], 180/Pi*Arg[x1[[1, 2]]]}}", false);
            MathAnalog.evaluate("offset = 0", false);
            MathAnalog.evaluate("Do[ If[(Abs[180/Pi*(Arg[x1[[i, 2]]] - Arg[x1[[i - 1, 2]]])] > 0.9*360), If[(Arg[x1[[i, 2]]] - Arg[x1[[i - 1, 2]]]) < 0, offset += 360; AppendTo[x2, { x1[[i, 1]], 180/Pi*Arg[x1[[i, 2]]] + offset}], offset -= 360; AppendTo[x2, {x1[[i, 1]], 180/Pi*Arg[x1[[i, 2]]] + offset}]], AppendTo[x2, {x1[[i, 1]], 180/Pi*Arg[x1[[i, 2]]] + offset}]], {i, 2, Length[x1]}]", false);
            MathAnalog.evaluate("ifun = Interpolation[x2]", false);
            MathAnalog.evaluate("x1 = InterpolatingFunctionToList[v]", false);
            MathAnalog.evaluate("x2 = {{x1[[1, 1]], 180/Pi*Arg[x1[[1, 2]]]}}", false);
            MathAnalog.evaluate("offset = 0", false);
            MathAnalog.evaluate("Do[ If[(Abs[180/Pi*(Arg[x1[[i, 2]]] - Arg[x1[[i - 1, 2]]])] > 0.9*360), If[(Arg[x1[[i, 2]]] - Arg[x1[[i - 1, 2]]]) < 0, offset += 360; AppendTo[x2, { x1[[i, 1]], 180/Pi*Arg[x1[[i, 2]]] + offset}], offset -= 360; AppendTo[x2, {x1[[i, 1]], 180/Pi*Arg[x1[[i, 2]]] + offset}]], AppendTo[x2, {x1[[i, 1]], 180/Pi*Arg[x1[[i, 2]]] + offset}]], {i, 2, Length[x1]}]", false);
            MathAnalog.evaluate("iv = Interpolation[x2]", false);
            if (!deb.startsWith("v")) {
                if (logarithmic) strResult = MathAnalog.evaluateToInputForm("FullGraphics[LogLinearPlot[{ifun[f], iv[f]}, {f," + minValue + "," + maxValue + "}" + ", GridLines -> Automatic, PlotRange->All]]", 0, false); else strResult = MathAnalog.evaluateToInputForm("FullGraphics[Plot[{ifun[f], iv[f]}, {f," + minValue + "," + maxValue + "}" + ", GridLines -> Automatic, PlotRange->All]]", 0, false);
            } else {
                if (logarithmic) strResult = MathAnalog.evaluateToInputForm("FullGraphics[LogLinearPlot[{ifun[f]}, {f," + minValue + "," + maxValue + "}" + ", GridLines -> Automatic, PlotRange->All]]", 0, false); else strResult = MathAnalog.evaluateToInputForm("FullGraphics[Plot[{ifun[f]}, {f," + minValue + "," + maxValue + "}" + ", GridLines -> Automatic, PlotRange->All]]", 0, false);
            }
            pattern = Pattern.compile("Line\\[[^\\]]*\\]");
            matcher = pattern.matcher(strResult);
            found = false;
            layerVar = 0x00A;
            while (matcher.find()) {
                found = true;
                String str = strResult.substring(matcher.start() + 7, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                String[] lines = str.split("[}], [{]");
                xpoints = new double[lines.length];
                ypoints = new double[lines.length];
                for (int i = 0; i < lines.length; i++) {
                    xpoints[i] = Double.valueOf(lines[i].split(", ")[0]);
                    ypoints[i] = Double.valueOf(lines[i].split(", ")[1]);
                }
                cdgPL = new CdgPolyline(xpoints, ypoints, xpoints.length, layerVar, (byte) 0, axis);
                phase.add(cdgPL);
            }
            index = 0;
            if (matcher.find(0)) {
                found = true;
                index = matcher.end();
                String str = strResult.substring(matcher.start() + 7, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                String[] lines = str.split("[}], [{]");
                xpoints = new double[lines.length];
                ypoints = new double[lines.length];
                for (int i = 0; i < lines.length; i++) {
                    xpoints[i] = Double.valueOf(lines[i].split(", ")[0]);
                    ypoints[i] = Double.valueOf(lines[i].split(", ")[1]);
                }
                cdgPL = new CdgPolyline(xpoints, ypoints, xpoints.length, layerVar, (byte) 0, simPlot);
                phase.add(cdgPL);
            }
            if (matcher.find(index) && !deb.startsWith("v")) {
                found = true;
                String str = strResult.substring(matcher.start() + 7, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                String[] lines = str.split("[}], [{]");
                xpoints = new double[lines.length];
                ypoints = new double[lines.length];
                for (int i = 0; i < lines.length; i++) {
                    xpoints[i] = Double.valueOf(lines[i].split(", ")[0]);
                    ypoints[i] = Double.valueOf(lines[i].split(", ")[1]);
                }
                cdgPL = new CdgPolyline(xpoints, ypoints, xpoints.length, layerVar, (byte) 0, aiPlot);
                phase.add(cdgPL);
            }
            phase.calculateBounds();
            aTphase.scale(cont.getBounds().getWidth() / phase.getBounds().getWidth(), 0.5 * cont.getBounds().getHeight() / phase.getBounds().getHeight());
            ginstPh = new CdgInstance(0, (int) (aTfrqG.getScaleY() * frqG.bbox.getMinY() - aTphase.getScaleY() * phase.bbox.getMaxY()) - 10, aTphase, phase);
            ginstPh.setClipRegion(phase.bbox);
            cont.add(ginstPh);
            layerVar = 0x00B;
            pattern = Pattern.compile("Text\\[[^\\]]*0\\Q.\\E[}]\\]");
            matcher = pattern.matcher(strResult);
            while (matcher.find()) {
                found = true;
                String str = strResult.substring(matcher.start() + 5, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                str = str.split(",")[0];
                try {
                    float f = -(float) (Float.valueOf(str) * aTphase.getScaleY()) - (float) (ginstPh.trf.getTranslateY());
                    CdgText cdgT = new CdgText((float) (phase.getBounds().getMinX() * aTphase.getScaleX() + ginstPh.trf.getTranslateX()), f, str + "�", layerVar, (byte) 0, fontColor);
                    txtPhaseY.add(cdgT);
                } catch (NumberFormatException e) {
                }
            }
            pattern = Pattern.compile("Text\\[[^\\]]*1\\Q.\\E[}]\\]");
            matcher = pattern.matcher(strResult);
            while (matcher.find()) {
                found = true;
                String str = strResult.substring(matcher.start() + 5, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                str = str.split(",")[0];
                str = str.replace("0, ", "E");
                try {
                    float f;
                    if (logarithmic) {
                        if (version.startsWith("6")) f = (float) (Math.log(Float.valueOf(str)) * aTphase.getScaleX()); else f = (float) (Math.log10(Float.valueOf(str)) * aTphase.getScaleX());
                    } else f = (float) (Float.valueOf(str) * aTphase.getScaleX() / 1);
                    CdgText cdgT = new CdgText(f, -(float) (ginstPh.trf.getTranslateY() + phase.getBounds().getMinY() * aTphase.getScaleY()), str, layerVar, (byte) 0, fontColor);
                    txtPhaseX.add(cdgT);
                } catch (NumberFormatException e) {
                }
            }
            pattern = Pattern.compile("Text\\[Superscript\\[[^\\]]*\\][^\\]]*1\\Q.\\E[}]\\]");
            matcher = pattern.matcher(strResult);
            while (matcher.find()) {
                found = true;
                String str = strResult.substring(matcher.start() + 17, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                str = str.split("]")[0];
                str = str.replace("0, ", "E");
                try {
                    float f;
                    if (logarithmic) {
                        if (version.startsWith("6")) f = (float) (Math.log(Float.valueOf(str)) * aTphase.getScaleX()); else f = (float) (Math.log10(Float.valueOf(str)) * aTphase.getScaleX());
                    } else f = (float) (Float.valueOf(str) * aTphase.getScaleX() / 1);
                    CdgText cdgT = new CdgText(f, -(float) (ginstPh.trf.getTranslateY() + phase.getBounds().getMinY() * aTphase.getScaleY()), str, layerVar, (byte) 0, fontColor);
                    txtPhaseX.add(cdgT);
                } catch (NumberFormatException e) {
                }
            }
            pattern = Pattern.compile("Text\\[Superscript\\[[^\\]]*\\][^\\]]*0\\Q.\\E[}]\\]");
            matcher = pattern.matcher(strResult);
            while (matcher.find()) {
                found = true;
                String str = strResult.substring(matcher.start() + 17, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                str = str.split("]")[0];
                str = str.replace("0, ", "E");
                try {
                    float f = -(float) (Float.valueOf(str) * aTphase.getScaleY()) - (float) (ginstPh.trf.getTranslateY());
                    CdgText cdgT = new CdgText((float) (phase.getBounds().getMinX() * aTphase.getScaleX() + ginstPh.trf.getTranslateX()), f, str + "�", layerVar, (byte) 0, fontColor);
                    txtPhaseY.add(cdgT);
                } catch (NumberFormatException e) {
                }
            }
            pattern = Pattern.compile("Text\\[NumberForm\\[[^\\]]*\\][^\\]]*1\\Q.\\E[}]\\]");
            matcher = pattern.matcher(strResult);
            while (matcher.find()) {
                found = true;
                String str = strResult.substring(matcher.start() + 16, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                str = str.split("]")[0];
                str = str.replace("0, ", "E");
                try {
                    float f;
                    if (logarithmic) {
                        if (version.startsWith("6")) f = (float) (Math.log(Float.valueOf(str)) * aTphase.getScaleX()); else f = (float) (Math.log10(Float.valueOf(str)) * aTphase.getScaleX());
                    } else f = (float) (Float.valueOf(str) * aTphase.getScaleX() / 1);
                    CdgText cdgT = new CdgText(f, -(float) (ginstPh.trf.getTranslateY() + phase.getBounds().getMinY() * aTphase.getScaleY()), str, layerVar, (byte) 0, fontColor);
                    txtPhaseX.add(cdgT);
                } catch (NumberFormatException e) {
                }
            }
            pattern = Pattern.compile("Text\\[NumberForm\\[[^\\]]*\\][^\\]]*0\\Q.\\E[}]\\]");
            matcher = pattern.matcher(strResult);
            while (matcher.find()) {
                found = true;
                String str = strResult.substring(matcher.start() + 16, matcher.end() - 3).replaceAll("\\Q.\\E*[*][\\Q^\\E]", "E");
                str = str.split("]")[0];
                str = str.replace("0, ", "E");
                try {
                    float f = -(float) (Float.valueOf(str) * aTphase.getScaleY()) - (float) (ginstPh.trf.getTranslateY());
                    CdgText cdgT = new CdgText((float) (phase.getBounds().getMinX() * aTphase.getScaleX() + ginstPh.trf.getTranslateX()), f, str + "�", layerVar, (byte) 0, fontColor);
                    txtPhaseY.add(cdgT);
                } catch (NumberFormatException e) {
                }
            }
            AffineTransform atLabel = new AffineTransform(1, 0, 0, -1, 0, 0);
            cdgTl = new CdgText((float) (phase.getBounds().getMinX() * aTphase.getScaleX() + ginstPh.trf.getTranslateX()), -(float) (ginstPh.trf.getTranslateY() + phase.getBounds().getMinY() * aTphase.getScaleY()) + 20, "Frequency", layerVar, (byte) 0, fontColor);
            labelPhX.add(cdgTl);
            labelPhX.calculateBounds();
            CdgInstance ginstlabelPhX = new CdgInstance(0, 0, atLabel, labelPhX);
            cont.add(ginstlabelPhX);
            atLabel.concatenate(new AffineTransform(0, -1, 1, 0, 0, 0));
            cdgTl = new CdgText((float) (ginstPh.trf.getTranslateY() + phase.getBounds().getMinY() * aTphase.getScaleY()), (float) (phase.getBounds().getMinX() * aTphase.getScaleX() + ginstPh.trf.getTranslateX()) - 10, "Phase", layerVar, (byte) 0, fontColor);
            labelPhY.add(cdgTl);
            labelPhY.calculateBounds();
            CdgInstance ginstlabelPhY = new CdgInstance(0, 0, atLabel, labelPhY);
            cont.add(ginstlabelPhY);
            labelMY.calculateBounds();
            CdgInstance ginstlabelMY = new CdgInstance(0, 0, atLabel, labelMY);
            cont.add(ginstlabelMY);
            txtFrqGY.calculateBounds();
            AffineTransform atTxt = new AffineTransform(1, 0, 0, -1, 0, 0);
            CdgInstance ginstTxtFrqGY = new CdgInstance(0, 0, atTxt, txtFrqGY);
            ginstTxtFrqGY.setClipRegion(txtFrqGY.bbox);
            cont.add(ginstTxtFrqGY);
            txtPhaseY.calculateBounds();
            CdgInstance ginstTxtPhaseY = new CdgInstance(0, 0, atTxt, txtPhaseY);
            ginstTxtPhaseY.setClipRegion(txtPhaseY.bbox);
            cont.add(ginstTxtPhaseY);
            txtFrqGX.calculateBounds();
            CdgInstance ginstTxtFrqGX = new CdgInstance(0, 0, atTxt, txtFrqGX);
            ginstTxtFrqGX.setClipRegion(txtFrqGX.bbox);
            cont.add(ginstTxtFrqGX);
            txtPhaseX.calculateBounds();
            CdgInstance ginstTxtPhaseX = new CdgInstance(0, 0, atTxt, txtPhaseX);
            ginstTxtPhaseX.setClipRegion(txtPhaseX.bbox);
            cont.add(ginstTxtPhaseX);
            cont.calculateBounds();
            for (int i = 0; i < pointCounter; i++) {
                try {
                    double im = (cPoint[i].im() / (2 * Math.PI));
                    if (logarithmic) if (version.startsWith("6")) paintLine(new Point2D.Double(Math.log(im), 0)); else paintLine(new Point2D.Double(Math.log10(im), 0)); else paintLine(new Point2D.Double(im, 0));
                } catch (Exception e) {
                }
            }
            Fit();
            if (!found) {
                System.out.println("wrong Graphics");
            }
        } catch (MathLinkException e) {
            e.printStackTrace();
        }
    }
