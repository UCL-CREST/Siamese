    public static JFreeChart buildChart(Map params, Font font) {
        if (params == null) {
            throw new IllegalArgumentException("Null 'params' argument.");
        }
        JFreeChart chart = null;
        String[] chartType = (String[]) params.get("cht");
        int dataType = -1;
        if (chartType[0].equals("p")) {
            chart = createPieChart();
            dataType = 0;
        } else if (chartType[0].equals("p3")) {
            chart = createPieChart3D();
            dataType = 0;
        } else if (chartType[0].equals("lc")) {
            chart = createLineChart();
            dataType = 1;
        } else if (chartType[0].equals("ls")) {
            chart = createSparklineChart();
            dataType = 1;
        } else if (chartType[0].equals("lxy")) {
            chart = createLineChart();
            dataType = 3;
        } else if (chartType[0].equals("bhs")) {
            chart = createStackedBarChart(PlotOrientation.HORIZONTAL);
            dataType = 2;
        } else if (chartType[0].equals("bhg")) {
            chart = createBarChart(PlotOrientation.HORIZONTAL);
            dataType = 2;
        } else if (chartType[0].equals("bvs")) {
            chart = createStackedBarChart(PlotOrientation.VERTICAL);
            dataType = 2;
        } else if (chartType[0].equals("bvg")) {
            chart = createBarChart(PlotOrientation.VERTICAL);
            dataType = 2;
        } else if (chartType[0].equals("bhs3")) {
            chart = createStackedBarChart3D(PlotOrientation.HORIZONTAL);
            dataType = 2;
        } else if (chartType[0].equals("bhg3")) {
            chart = createBarChart3D(PlotOrientation.HORIZONTAL);
            dataType = 2;
        } else if (chartType[0].equals("bvs3")) {
            chart = createStackedBarChart3D(PlotOrientation.VERTICAL);
            dataType = 2;
        } else if (chartType[0].equals("bvg3")) {
            chart = createBarChart3D(PlotOrientation.VERTICAL);
            dataType = 2;
        } else if (chartType[0].equals("s")) {
            chart = createScatterChart();
            dataType = 4;
        } else if (chartType[0].equals("dial")) {
            chart = createDialChart();
            dataType = 5;
        } else if (chartType[0].equals("v")) {
            throw new RuntimeException("Venn diagrams not implemented.");
        } else {
            throw new RuntimeException("Unknown chart type: " + chartType[0]);
        }
        chart.getPlot().setOutlineVisible(false);
        if (chart.getPlot() instanceof XYPlot) {
            XYPlot plot = (XYPlot) chart.getPlot();
            plot.getDomainAxis().setLabelFont(font);
            plot.getDomainAxis().setTickLabelFont(font);
            plot.getRangeAxis().setLabelFont(font);
            plot.getRangeAxis().setTickLabelFont(font);
        } else if (chart.getPlot() instanceof CategoryPlot) {
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            plot.getDomainAxis().setLabelFont(font);
            plot.getDomainAxis().setTickLabelFont(font);
            plot.getRangeAxis().setLabelFont(font);
            plot.getRangeAxis().setTickLabelFont(font);
        }
        List axes = new java.util.ArrayList();
        String[] axisStr = (String[]) params.get("chxt");
        if (axisStr != null) {
            if (chart.getPlot() instanceof XYPlot) {
                XYPlot plot = (XYPlot) chart.getPlot();
                processAxisStr(plot, axisStr[0], axes);
            } else if (chart.getPlot() instanceof CategoryPlot) {
                CategoryPlot plot = (CategoryPlot) chart.getPlot();
                if (plot.getOrientation() == PlotOrientation.VERTICAL) {
                    processAxisStrV(plot, axisStr[0], axes);
                } else {
                    processAxisStrH(plot, axisStr[0], axes);
                }
            }
        }
        String[] axisLabelOrientationStr = (String[]) params.get("ewlo");
        if (axisLabelOrientationStr != null) {
            String[] orientations = breakString(axisLabelOrientationStr[0], '|');
            for (int i = 0; i < orientations.length; i++) {
                int comma = orientations[i].indexOf(',');
                int axisIndex = Integer.parseInt(orientations[i].substring(0, comma));
                String orientation = orientations[i].substring(comma + 1);
                Axis axis = (Axis) axes.get(axisIndex);
                if (axis instanceof GCategoryAxis) {
                    GCategoryAxis gaxis = (GCategoryAxis) axis;
                    gaxis.setCategoryLabelPositions(getCategoryLabelPositions(orientation));
                }
            }
        }
        String[] axisRangeStr = (String[]) params.get("chxr");
        if (axisRangeStr != null) {
            String[] ranges = breakString(axisRangeStr[0], '|');
            for (int i = 0; i < ranges.length; i++) {
                int comma1 = ranges[i].indexOf(',');
                int comma2 = ranges[i].indexOf(',', comma1 + 1);
                int axisIndex = Integer.parseInt(ranges[i].substring(0, comma1));
                float lowerBound = Float.parseFloat(ranges[i].substring(comma1 + 1, comma2));
                float upperBound = Float.parseFloat(ranges[i].substring(comma2 + 1));
                Axis axis = (Axis) axes.get(axisIndex);
                if (axis instanceof GValueAxis) {
                    GValueAxis gaxis = (GValueAxis) axis;
                    gaxis.setLabelAxisStart(lowerBound);
                    gaxis.setLabelAxisEnd(upperBound);
                } else if (axis instanceof GValueAxis3D) {
                    GValueAxis3D gaxis3 = (GValueAxis3D) axis;
                    gaxis3.setLabelAxisStart(lowerBound);
                    gaxis3.setLabelAxisEnd(upperBound);
                }
            }
        }
        String[] axisLabelStr = (String[]) params.get("chxl");
        if (axisLabelStr != null) {
            Pattern p = Pattern.compile("\\d+:\\|");
            Matcher m = p.matcher(axisLabelStr[0]);
            if (m.find()) {
                int keyStart = m.start();
                int labelStart = m.end();
                while (m.find(labelStart)) {
                    String keyStr = axisLabelStr[0].substring(keyStart, labelStart - 2);
                    int axisIndex = Integer.parseInt(keyStr);
                    keyStart = m.start();
                    String labelStr = axisLabelStr[0].substring(labelStart, keyStart - 1);
                    String[] labels = breakString(labelStr, '|');
                    GLabelledAxis axis = (GLabelledAxis) axes.get(axisIndex);
                    axis.setTickLabels(Arrays.asList(labels));
                    labelStart = m.end();
                }
                String keyStr = axisLabelStr[0].substring(keyStart, labelStart - 2);
                String labelStr = axisLabelStr[0].substring(labelStart);
                int axisIndex = Integer.parseInt(keyStr);
                if (labelStr.endsWith("|")) {
                    labelStr = labelStr.substring(0, labelStr.length() - 1);
                }
                String[] labels = breakString(labelStr, '|');
                GLabelledAxis axis = (GLabelledAxis) axes.get(axisIndex);
                axis.setTickLabels(Arrays.asList(labels));
            } else {
                throw new RuntimeException("No matching pattern!");
            }
        }
        String[] axisPositionStr = (String[]) params.get("chxp");
        if (axisPositionStr != null) {
            String[] positions = breakString(axisPositionStr[0], '|');
            for (int i = 0; i < positions.length; i++) {
                int c1 = positions[i].indexOf(',');
                int axisIndex = Integer.parseInt(positions[i].substring(0, c1));
                String remainingStr = positions[i].substring(c1 + 1);
                String[] valueStr = breakString(remainingStr, ',');
                List tickValues = new java.util.ArrayList(valueStr.length);
                Axis axis = (Axis) axes.get(axisIndex);
                if (axis instanceof GValueAxis) {
                    GValueAxis gaxis = (GValueAxis) axes.get(axisIndex);
                    for (int j = 0; j < valueStr.length; j++) {
                        float pos = Float.parseFloat(valueStr[j]);
                        tickValues.add(new Float(pos));
                    }
                    gaxis.setTickLabelPositions(tickValues);
                }
            }
        }
        String[] axisStyleStr = (String[]) params.get("chxs");
        if (axisStyleStr != null) {
            String[] styles = breakString(axisStyleStr[0], '|');
            for (int i = 0; i < styles.length; i++) {
                String[] styleSpec = breakString(styles[i], ',');
                int axisIndex = Integer.parseInt(styleSpec[0]);
                Axis axis = (Axis) axes.get(axisIndex);
                Color axisColor = parseColor(styleSpec[1]);
                axis.setTickLabelPaint(axisColor);
                if (styleSpec.length > 2) {
                    int fontSize = Integer.parseInt(styleSpec[2]);
                    if (fontSize > 0) {
                        Font oldFont = axis.getTickLabelFont();
                        Font newFont = oldFont.deriveFont((float) fontSize);
                        axis.setTickLabelFont(newFont);
                    } else {
                        axis.setTickLabelsVisible(false);
                    }
                }
                if (styleSpec.length > 3) {
                }
                if (styleSpec.length > 4) {
                    String drawingControl = styleSpec[4];
                    if ("l".equals(drawingControl)) {
                        axis.setTickMarksVisible(false);
                    } else if ("t".equals(drawingControl)) {
                        axis.setAxisLineVisible(false);
                    } else if ("_".equals(drawingControl)) {
                        axis.setVisible(false);
                    } else if (!"lt".equals(drawingControl)) {
                        throw new RuntimeException("Unknown drawing control " + drawingControl);
                    }
                }
                if (styleSpec.length > 5) {
                    Color tickColor = parseColor(styleSpec[5]);
                    axis.setTickMarkPaint(tickColor);
                }
            }
        }
        String[] titleStr = (String[]) params.get("chtt");
        if (titleStr != null) {
            String[] s = breakString(titleStr[0], '|');
            for (int i = 0; i < s.length; i++) {
                TextTitle t = new TextTitle(s[i].replace('+', ' '));
                t.setPaint(Color.gray);
                t.setFont(font.deriveFont(font.getSize2D() * 14f / 12f));
                chart.addSubtitle(t);
            }
            String[] fontStr = (String[]) params.get("chts");
            if (fontStr != null) {
                int c1 = fontStr[0].indexOf(',');
                String colorStr = null;
                String fontSizeStr = null;
                if (c1 != -1) {
                    colorStr = fontStr[0].substring(0, c1);
                    fontSizeStr = fontStr[0].substring(c1 + 1);
                } else {
                    colorStr = fontStr[0];
                }
                Color color = parseColor(colorStr);
                int size = 12;
                if (fontSizeStr != null) {
                    size = Integer.parseInt(fontSizeStr);
                }
                for (int i = 0; i < chart.getSubtitleCount(); i++) {
                    Title t = chart.getSubtitle(i);
                    if (t instanceof TextTitle) {
                        TextTitle tt = (TextTitle) t;
                        tt.setPaint(color);
                        tt.setFont(font.deriveFont((float) size));
                    }
                }
            }
        }
        String[] dataStr = (String[]) params.get("chd");
        String scalingStr = null;
        if (dataStr.length > 0 && dataStr[0].startsWith("t:")) {
            String[] chds = (String[]) params.get("chds");
            if (chds != null && chds.length > 0) {
                scalingStr = chds[0];
            }
        }
        String[] d2Str = (String[]) params.get("ewd2");
        if (dataType == 0) {
            PieDataset dataset = DataUtilities.parsePieDataset(dataStr[0], scalingStr);
            PiePlot plot = (PiePlot) chart.getPlot();
            plot.setDataset(dataset);
        } else if (dataType == 1) {
            XYPlot plot = (XYPlot) chart.getPlot();
            XYDataset dataset = DataUtilities.parseXYDataset(dataStr[0], scalingStr);
            plot.setDataset(dataset);
            if (d2Str != null) {
                XYDataset d2 = DataUtilities.parseXYDataset(d2Str[0], scalingStr);
                plot.setDataset(1, d2);
            }
        } else if (dataType == 2) {
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            CategoryDataset dataset = DataUtilities.parseCategoryDataset(dataStr[0], scalingStr);
            plot.setDataset(dataset);
            if (d2Str != null) {
                CategoryDataset d2 = DataUtilities.parseCategoryDataset(d2Str[0], scalingStr);
                plot.setDataset(1, d2);
            }
        } else if (dataType == 3) {
            XYPlot plot = (XYPlot) chart.getPlot();
            XYDataset dataset = DataUtilities.parseXYDataset2(dataStr[0], scalingStr);
            plot.setDataset(dataset);
            if (d2Str != null) {
                XYDataset d2 = DataUtilities.parseXYDataset2(d2Str[0], scalingStr);
                plot.setDataset(1, d2);
            }
        } else if (dataType == 4) {
            XYPlot plot = (XYPlot) chart.getPlot();
            XYSeriesCollection dataset = DataUtilities.parseScatterDataset(dataStr[0], scalingStr);
            if (dataset.getSeriesCount() > 1) {
                dataset.removeSeries(1);
            }
            plot.setDataset(dataset);
            if (d2Str != null) {
                XYDataset d2 = DataUtilities.parseXYDataset2(d2Str[0], scalingStr);
                plot.setDataset(1, d2);
            }
        } else if (dataType == 5) {
            DialPlot plot = (DialPlot) chart.getPlot();
            String[] data = dataStr[0].substring(dataStr[0].indexOf(":") + 1).split(",");
            if (data.length == 0) {
                throw new RuntimeException("Dial chart needs data points");
            }
            float value = Float.parseFloat(data[0]);
            plot.setDataset(new DefaultValueDataset(value));
            double lowerBound = 0;
            double upperBound = 100;
            double majorTickIncrement = 10.0;
            int minorTickIncrement = 4;
            if (data.length > 1) {
                for (int i = 1; i < data.length; i++) {
                    if (data[i].indexOf("dr=") == 0) {
                        String[] rd = data[i].substring(3).split(":");
                        if (rd.length == 3) {
                            StandardDialRange range = new StandardDialRange(Float.parseFloat(rd[0]), Float.parseFloat(rd[1]), new Color(Integer.parseInt(rd[2], 16)));
                            range.setInnerRadius(0.52);
                            range.setOuterRadius(0.55);
                            plot.addLayer(range);
                        }
                    } else if (data[i].indexOf("lb=") == 0) {
                        lowerBound = Double.parseDouble(data[i].substring(3));
                    } else if (data[i].indexOf("ub=") == 0) {
                        upperBound = Double.parseDouble(data[i].substring(3));
                    } else if (data[i].indexOf("mt=") == 0) {
                        minorTickIncrement = Integer.parseInt(data[i].substring(3));
                    } else if (data[i].indexOf("mjt=") == 0) {
                        majorTickIncrement = Double.parseDouble(data[i].substring(4));
                    }
                }
            }
            StandardDialScale scale = new StandardDialScale(lowerBound, upperBound, -120, -300, majorTickIncrement, minorTickIncrement);
            scale.setTickRadius(0.90);
            scale.setTickLabelOffset(0.17);
            scale.setTickLabelFont(font);
            plot.addScale(0, scale);
            plot.removePointer(0);
            DialPointer.Pointer p = new DialPointer.Pointer();
            p.setFillPaint(Color.gray);
            plot.addPointer(p);
        }
        String[] colorStr = (String[]) params.get("chco");
        if (colorStr != null) {
            Color[] colors = parseColors(colorStr[0]);
            if (dataType == 0) {
                PiePlot plot = (PiePlot) chart.getPlot();
                applyColorsToPiePlot(plot, colors);
            } else {
                AbstractRenderer renderer = null;
                if (chart.getPlot() instanceof CategoryPlot) {
                    CategoryPlot plot = (CategoryPlot) chart.getPlot();
                    renderer = (AbstractRenderer) plot.getRenderer();
                    renderer.setBasePaint(colors[0]);
                } else if (chart.getPlot() instanceof XYPlot) {
                    XYPlot plot = (XYPlot) chart.getPlot();
                    renderer = (AbstractRenderer) plot.getRenderer();
                    renderer.setBasePaint(colors[colors.length - 1]);
                }
                for (int i = 0; i < colors.length; i++) {
                    renderer.setSeriesPaint(i, colors[i]);
                }
            }
        } else {
            Plot plot = chart.getPlot();
            if (plot instanceof PiePlot) {
                applyColorsToPiePlot((PiePlot) chart.getPlot(), new Color[] { new Color(255, 153, 0) });
            }
        }
        String[] lineStr = (String[]) params.get("chls");
        if (lineStr != null && chart.getPlot() instanceof XYPlot) {
            Stroke[] strokes = parseLineStyles(lineStr[0]);
            XYPlot plot = (XYPlot) chart.getPlot();
            XYItemRenderer renderer = plot.getRenderer();
            for (int i = 0; i < strokes.length; i++) {
                renderer.setSeriesStroke(i, strokes[i]);
            }
            renderer.setBaseStroke(strokes[strokes.length - 1]);
        }
        if (dataType != 0) {
            String[] gridStr = (String[]) params.get("chg");
            if (gridStr != null) {
                processGridLinesSpec(gridStr[0], chart);
            }
        }
        if (dataType == 0) {
            String[] labelStr = (String[]) params.get("chl");
            if (labelStr != null) {
                String[] s = breakString(labelStr[0], '|');
                List labels = Arrays.asList(s);
                PiePlot plot = (PiePlot) chart.getPlot();
                if (labels.size() > 0) {
                    plot.setLabelGenerator(new GPieSectionLabelGenerator(labels));
                    plot.setLabelFont(font);
                    plot.setLabelPaint(Color.gray);
                }
            }
        } else if (dataType == 5) {
            DialPlot plot = (DialPlot) chart.getPlot();
            String[] labelStr = (String[]) params.get("chl");
            if (labelStr != null) {
                DialTextAnnotation annotation1 = new DialTextAnnotation(labelStr[0]);
                annotation1.setFont(font);
                annotation1.setRadius(0.7);
                plot.addLayer(annotation1);
            }
        } else {
            String[] legendStr = (String[]) params.get("chdl");
            if (legendStr != null) {
                String[] s = breakString(legendStr[0], '|');
                List labels = Arrays.asList(s);
                if (labels.size() > 0) {
                    Plot p = chart.getPlot();
                    if (p instanceof CategoryPlot) {
                        CategoryPlot plot = (CategoryPlot) chart.getPlot();
                        BarRenderer renderer = (BarRenderer) plot.getRenderer();
                        renderer.setLegendItemLabelGenerator(new GSeriesLabelGenerator(labels));
                        renderer.setBaseSeriesVisibleInLegend(false);
                        for (int i = 0; i < labels.size(); i++) {
                            renderer.setSeriesVisibleInLegend(i, Boolean.TRUE);
                        }
                    } else if (p instanceof XYPlot) {
                        XYPlot plot = (XYPlot) chart.getPlot();
                        XYItemRenderer renderer = plot.getRenderer();
                        renderer.setLegendItemLabelGenerator(new GSeriesLabelGenerator(labels));
                        renderer.setBaseSeriesVisibleInLegend(false);
                        for (int i = 0; i < labels.size(); i++) {
                            renderer.setSeriesVisibleInLegend(i, Boolean.TRUE);
                        }
                    }
                    LegendTitle legend = new LegendTitle(chart.getPlot());
                    RectangleEdge pos = RectangleEdge.RIGHT;
                    String[] chdlp = (String[]) params.get("chdlp");
                    if (chdlp != null) {
                        if ("b".equals(chdlp[0])) {
                            pos = RectangleEdge.BOTTOM;
                        } else if ("t".equals(chdlp[0])) {
                            pos = RectangleEdge.TOP;
                        } else if ("l".equals(chdlp[0])) {
                            pos = RectangleEdge.LEFT;
                        }
                    }
                    legend.setPosition(pos);
                    legend.setItemFont(font);
                    legend.setItemPaint(Color.gray);
                    chart.addSubtitle(legend);
                }
            }
        }
        String[] markerStr = (String[]) params.get("chm");
        if (markerStr != null) {
            String[] markers = breakString(markerStr[0], '|');
            for (int i = 0; i < markers.length; i++) {
                addMarker(markers[i], chart);
            }
        }
        String[] fillStr = (String[]) params.get("chf");
        if (fillStr != null) {
            int i = fillStr[0].indexOf('|');
            if (i == -1) {
                processFillSpec(fillStr[0], chart);
            } else {
                String fs1 = fillStr[0].substring(0, i);
                String fs2 = fillStr[0].substring(i + 1);
                processFillSpec(fs1, chart);
                processFillSpec(fs2, chart);
            }
        }
        String[] pieRotation = (String[]) params.get("chp");
        if (pieRotation != null) {
            double rotation = -Math.toDegrees(Double.parseDouble(pieRotation[0]));
            Plot plot = chart.getPlot();
            if (plot instanceof PiePlot) {
                ((PiePlot) plot).setStartAngle(rotation);
            } else if (plot instanceof PiePlot3D) {
                ((PiePlot3D) plot).setStartAngle(rotation);
            }
        }
        processEWTR(params, chart);
        return chart;
    }
