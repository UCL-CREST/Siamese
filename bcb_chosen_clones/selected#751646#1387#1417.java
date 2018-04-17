    @SuppressWarnings("unchecked")
    protected AbstractEdgeShapeTransformer<Node, Edge> makeEdgeShapeInstance(final Class<? extends AbstractEdgeShapeTransformer<Node, Edge>> thisEdgeShapeClass) {
        try {
            final Constructor<? extends AbstractEdgeShapeTransformer<Node, Edge>> thisConstructor = thisEdgeShapeClass.getConstructor(new Class[] {});
            final Object[] theseArgs = {};
            final Object thisEdgeShapeObject = thisConstructor.newInstance(theseArgs);
            final AbstractEdgeShapeTransformer<Node, Edge> thisEdgeShapeTransformer = (AbstractEdgeShapeTransformer<Node, Edge>) thisEdgeShapeObject;
            thisEdgeShapeTransformer.setControlOffsetIncrement(this.theSettings.theEdgeControlOffsetIncrement);
            if (thisEdgeShapeTransformer instanceof Bracket) {
                final Bracket<Node, Edge> thisBracket = (Bracket<Node, Edge>) thisEdgeShapeTransformer;
                thisBracket.setEdgeIndexFunction(this.theBracketEdgeIndexFunction);
                thisBracket.setEdgeOffsetFunctions(this.theBracketEdgeOffsetFunctions);
                Bracket.setXOffsetIncrement(this.theSettings.theEdgeXOffsetIncrement);
                Bracket.setYOffsetIncrement(this.theSettings.theEdgeYOffsetIncrement);
            }
            return thisEdgeShapeTransformer;
        } catch (final SecurityException e) {
            e.printStackTrace();
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
