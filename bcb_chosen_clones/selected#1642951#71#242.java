    public void startElement(QName element, XMLAttributes attrs, Augmentations augs) throws XNIException {
        if ("esis".equals(element.localpart)) {
            String version = attrs.getValue("version");
            if (version == null) throw new IllegalArgumentException("esis tag should specify a version (eg 1.0)");
            if (currentFilename == null) throw new IllegalStateException("Do not know originating filename");
            Matcher mVer = pVersion.matcher(version);
            if (mVer.matches()) {
                try {
                    currentFile = new FileInformation(currentFilename, Integer.parseInt(mVer.group(1)), Integer.parseInt(mVer.group(2)));
                } catch (Exception e) {
                    IllegalArgumentException iae = new IllegalArgumentException("esis tag's version invalid [" + version + ']');
                    iae.initCause(e);
                    throw iae;
                }
            } else throw new IllegalArgumentException("esis tag's version invalid [" + version + ']');
            cfg.setFileInformation(currentFile);
        } else if ("metric-group".equals(element.localpart)) {
            String groupName = attrs.getValue("name");
            if (groupName == null) throw new IllegalArgumentException("metric-group tag must specify a name");
            currentGroup = new MetricGroup(groupName);
            cfg.setMetricGroup(currentGroup);
        } else if ("foreignkey".equals(element.localpart)) {
            String name = attrs.getValue("name");
            String shortName = attrs.getValue("short_name");
            String schema = attrs.getValue("schema");
            String table = attrs.getValue("table");
            String key = attrs.getValue("key");
            if (name == null) throw new IllegalArgumentException("Foreignkey tag must specify a name");
            if (shortName == null) throw new IllegalArgumentException("Foreignkey tag [" + name + "] must specify a short_name");
            if (schema == null) throw new IllegalArgumentException("Foreignkey tag [" + name + "] must specify a schema");
            if (table == null) throw new IllegalArgumentException("Foreignkey tag [" + name + "] must specify a table");
            if (key == null) throw new IllegalArgumentException("Foreignkey tag [" + name + "] must specify a key");
            ForeignKey fkey = new ForeignKey(shortName, schema, table, key);
            cfg.setDimensionObject(name, fkey);
        } else if ("hierarchy".equals(element.localpart)) {
            String name = attrs.getValue("name");
            String shortName = attrs.getValue("short_name");
            int depth = Integer.parseInt(attrs.getValue("depth"));
            if (name == null) throw new IllegalArgumentException("Hierarchy tag must specify a name");
            if (shortName == null) throw new IllegalArgumentException("Hierarchy tag [" + name + "] must specify a short_name");
            Hierarchy h = new Hierarchy(shortName, name, depth);
            cfg.setDimensionObject(name, h);
        } else if ("string".equals(element.localpart)) {
            String name = attrs.getValue("name");
            String shortName = attrs.getValue("short_name");
            if (name == null) throw new IllegalArgumentException("String tag must specify a name");
            if (shortName == null) throw new IllegalArgumentException("String tag [" + name + "] must specify a short_name");
            PositionValueString str = new PositionValueString(shortName, name);
            cfg.setDimensionObject(name, str);
        } else if ("stringtable".equals(element.localpart)) {
            String name = attrs.getValue("name");
            String shortName = attrs.getValue("short_name");
            String schema = attrs.getValue("schema");
            String table = attrs.getValue("table");
            String idShortName = attrs.getValue("id_short_name");
            if (name == null) throw new IllegalArgumentException("Stringtable tag must specify a name");
            if (shortName == null) throw new IllegalArgumentException("Stringtable tag [" + name + "] must specify a short_name");
            if (schema == null) throw new IllegalArgumentException("Stringtable tag [" + name + "] must specify a schema");
            if (table == null) throw new IllegalArgumentException("Stringtable tag [" + name + "] must specify a table");
            if (idShortName == null) throw new IllegalArgumentException("Stringtable tag [" + name + "] must specify a id_short_name");
            boolean append = false;
            if ("true".equals(attrs.getValue("append"))) append = true;
            boolean precache = false;
            if ("true".equals(attrs.getValue("precache"))) precache = true;
            boolean unique = false;
            if ("true".equals(attrs.getValue("unique"))) unique = true;
            StringTable strtab = new StringTable(shortName, name, schema, table, idShortName, append, precache, unique);
            cfg.setDimensionObject(name, strtab);
        } else if ("link".equals(element.localpart)) {
            String name = attrs.getValue("name");
            String shortName = attrs.getValue("short_name");
            String with = attrs.getValue("with");
            String field = attrs.getValue("field");
            ForeignKey withObject = (ForeignKey) cfg.getDimensionObject(with);
            LinkDimension link = new LinkDimension(name, shortName, with, withObject, field);
            cfg.setDimensionObject(name, link);
        } else if ("join".equals(element.localpart)) {
            throw new IllegalArgumentException("<join /> is not yet supported");
        } else if ("metric".equals(element.localpart)) {
            if (currentGroup == null) throw new IllegalStateException("metric tag is only valid inside metric-group");
            String name = attrs.getValue("name");
            String type = attrs.getValue("type");
            String schema = attrs.getValue("schema");
            String table = attrs.getValue("table");
            String slice = attrs.getValue("slice");
            String value = attrs.getValue("value");
            String valueFactoryClass = attrs.getValue("value_factory");
            String intervalsName = attrs.getValue("intervals");
            if (name == null) throw new IllegalArgumentException("Metric must specify a name");
            if (schema == null) throw new IllegalArgumentException("Metric " + name + " must specify a schema");
            if (table == null) throw new IllegalArgumentException("Metric " + name + " must specify a table");
            if (type == null) throw new IllegalArgumentException("Metric " + name + " must specify a type");
            if (intervalsName == null) throw new IllegalArgumentException("Metric " + name + " must specify intervals");
            if (slice != null) {
                if ("delay".equals(slice)) currentSlice = new DelayValueSlice(); else if ("money".equals(slice)) currentSlice = new MoneyValueSlice();
            }
            if ("simple".equals(type)) {
                isMetricSimple = true;
            } else if ("multi".equals(type)) {
                isMetricMulti = true;
                currentAxes = new ArrayList<Axis>();
            } else if ("data".equals(type)) {
                currentAxes = new ArrayList<Axis>();
            } else throw new IllegalArgumentException("Metric " + name + " has invalid type " + type);
            if (!isMetricSimple) {
                if (value == null) {
                    if (valueFactoryClass == null) throw new IllegalArgumentException("Metric " + name + " must specify either a value or a value-factory");
                    try {
                        Class clValueFactory = Class.forName(valueFactoryClass);
                        Class clConstructor[] = {};
                        Constructor cValueFactory = clValueFactory.getConstructor(clConstructor);
                        Object oParams[] = {};
                        currentValueFactory = (CustomValueTypeFactory) cValueFactory.newInstance(oParams);
                        currentValueType = new CustomValueType(currentValueFactory);
                    } catch (Exception e) {
                        IllegalArgumentException iae = new IllegalArgumentException("Metric " + name + " has invalid value-factory [" + valueFactoryClass + "]");
                        iae.initCause(e);
                        throw iae;
                    }
                } else {
                    if (valueFactoryClass != null) throw new IllegalArgumentException("Metric " + name + " must not specify both value and value-factory");
                    if ("double".equals(value)) {
                        currentValueType = new DoubleValueType();
                    } else if ("number".equals(value)) {
                        currentValueType = new NumberValueType();
                    } else throw new IllegalArgumentException("Value type [" + value + "] is not defined.");
                }
            }
            metricName = name;
            metricSchema = schema;
            metricTable = table;
            metricIntervalsName = intervalsName;
        } else if ("axis".equals(element.localpart)) {
            if (metricName == null) throw new IllegalStateException("axis tag is only valid inside metric tag");
            if (currentAxes == null) throw new IllegalStateException("axis tag is not valid for simple events/metrics");
            String shortName = attrs.getValue("short_name");
            if (shortName == null) throw new IllegalArgumentException("Axis must specify a short_name");
            currentAxis = new ArrayList<Dimension>();
            currentAxisName = shortName;
        } else if ("crossproduct".equals(element.localpart)) {
            if (positionOperationValues != null) throw new IllegalStateException("do not nest crossproduct tags");
            positionOperationValues = new ArrayList<Dimension>();
            positionOperationShortName = attrs.getValue("short_name");
            if (positionOperationShortName == null) positionOperationShortName = "cp";
        } else if ("dim".equals(element.localpart)) {
            String name = attrs.getValue("name");
            Dimension d = cfg.getDimensionObject(name);
            if (d == null) throw new IllegalStateException("dim " + name + " is not found.");
            if (positionOperationValues != null) {
                positionOperationValues.add((StorablePositionValueType) d);
            } else if (currentAxis != null) {
                currentAxis.add(d);
            } else throw new IllegalStateException("dim tag is invalid ouside axis tag");
        } else if ("report-intervals".equals(element.localpart)) {
            String name = attrs.getValue("name");
            if (name == null) throw new IllegalArgumentException("<report-intervals> must specify a name attribute.");
            if (currentIntervals != null || currentIntervalsName != null) throw new IllegalStateException("Cannot nest <report-intervals> tags.");
            currentIntervalsName = name;
            currentIntervals = new ArrayList<Integer>();
        } else if ("day".equals(element.localpart)) {
            if (currentIntervals == null) throw new IllegalStateException("<day /> is only valid inside a <report-intervals> tag.");
            int n = 0;
            try {
                n = Integer.parseInt(attrs.getValue("n"));
            } catch (Exception e) {
                IllegalStateException ise = new IllegalStateException("Attribute n of tag <day /> is missing or invalid.");
                ise.initCause(e);
                throw ise;
            }
            if (n > 0) currentIntervals.add(Integer.valueOf(n));
        }
    }
