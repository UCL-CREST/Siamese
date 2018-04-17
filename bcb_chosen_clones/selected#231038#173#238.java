            _yticklabels = new Vector();
        }
        _yticks.addElement(new Double(position));
        _yticklabels.addElement(label);
    }

    /** If the argument is true, clear the axes.  I.e., set all parameters
     *  controlling the axes to their initial conditions.
     *  For the change to take effect, call repaint().  If the argument
     *  is false, do nothing.
     *  @param axes If true, clear the axes parameters.
     */
    public synchronized void clear(boolean axes) {
        _xBottom = Double.MAX_VALUE;
        _xTop = -Double.MAX_VALUE;
        _yBottom = Double.MAX_VALUE;
        _yTop = -Double.MAX_VALUE;
        if (axes) {
            _yMax = 0;
            _yMin = 0;
            _xMax = 0;
            _xMin = 0;
            _xRangeGiven = false;
            _yRangeGiven = false;
            _xlog = false;
            _ylog = false;
            _grid = true;
            _usecolor = true;
            _filespec = null;
            _xlabel = null;
            _ylabel = null;
            _title = null;
            _legendStrings = new Vector();
            _legendDatasets = new Vector();
            _xticks = null;
            _xticklabels = null;
            _yticks = null;
            _yticklabels = null;
        }
    }

    /** Rescale so that the data that is currently plotted just fits.
     *  This is done based on the protected variables _xBottom, _xTop,
     *  _yBottom, and _yTop.  It is up to derived classes to ensure that
     *  variables are valid.
     *  This method calls repaint(), which eventually causes the display
     *  to be updated.
     */
    public synchronized void fillPlot() {
        _setXRange(_xBottom, _xTop);
        _setYRange(_yBottom, _yTop);
        if (this.useSync) PlotRegistry.instance().changeRange(_xBottom, _xTop);
        repaint();
    }

    /** Convert a color name into a Color. Currently, only a very limited
     *  set of color names is supported: black, white, red, green, and blue.
     *  @param name A color name, or null if not found.
     *  @return An instance of Color.
     */
    public static Color getColorByName(String name) {
        try {
            Color col = new Color(Integer.parseInt(name, 16));
            return col;
        } catch (NumberFormatException e) {
        }
