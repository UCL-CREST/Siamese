class InfixConverter {
    InfixConverter() {
        super();
    }
    private static String a ( String s ) {
        java.util.Stack a = new java.util.Stack();
        String s0 = "";
        int i = 0;
        while ( i < s.length() ) {
            String s1 = null;
            int i0 = 0;
            int i1 = s.charAt ( i );
            label0: {
                label7: {
                    if ( i1 == 43 ) {
                        break label7;
                    }
                    if ( i1 == 45 ) {
                        break label7;
                    }
                    if ( i1 == 42 ) {
                        break label7;
                    }
                    if ( i1 == 47 ) {
                        break label7;
                    }
                    s1 = new StringBuilder().append ( s0 ).append ( ( char ) i1 ).toString();
                    i0 = i;
                    break label0;
                }
                if ( a.size() > 0 ) {
                    int i2 = 0;
                    Character a0 = ( Character ) a.peek();
                    int i3 = a0.charValue();
                    label4: {
                        label5: {
                            label6: {
                                if ( i3 == 42 ) {
                                    break label6;
                                }
                                int i4 = a0.charValue();
                                if ( i4 != 47 ) {
                                    break label5;
                                }
                            }
                            i2 = 1;
                            break label4;
                        }
                        i2 = 0;
                    }
                    if ( i2 != 1 ) {
                        label3: {
                            if ( i1 == 43 ) {
                                break label3;
                            }
                            if ( i1 == 45 ) {
                                break label3;
                            }
                            a.push ( ( Object ) Character.valueOf ( ( char ) i1 ) );
                            s1 = s0;
                            i0 = i;
                            break label0;
                        }
                        String s2 = new StringBuilder().append ( s0 ).append ( a.pop() ).toString();
                        a.push ( ( Object ) Character.valueOf ( ( char ) i1 ) );
                        s1 = s2;
                        i0 = i;
                    } else {
                        label1: {
                            label2: {
                                if ( i1 == 43 ) {
                                    break label2;
                                }
                                if ( i1 != 45 ) {
                                    break label1;
                                }
                            }
                            String s3 = new StringBuilder().append ( s0 ).append ( a.pop() ).toString();
                            int i5 = i + -1;
                            s1 = s3;
                            i0 = i5;
                            break label0;
                        }
                        String s4 = new StringBuilder().append ( s0 ).append ( a.pop() ).toString();
                        int i6 = i + -1;
                        s1 = s4;
                        i0 = i6;
                    }
                } else {
                    a.push ( ( Object ) Character.valueOf ( ( char ) i1 ) );
                    s1 = s0;
                    i0 = i;
                }
            }
            int i7 = i0 + 1;
            s0 = s1;
            i = i7;
        }
        int i8 = a.size();
        int i9 = 0;
        String s5 = s0;
        while ( i9 < i8 ) {
            String s6 = new StringBuilder().append ( s5 ).append ( a.pop() ).toString();
            i9 = i9 + 1;
            s5 = s6;
        }
        return s5;
    }
    public static void main ( String[] a ) {
        if ( a.length != 1 ) {
            String s = InfixConverter.a ( "a+b*c" );
            System.out.println ( new StringBuilder ( "InFix  :\t" ).append ( "a+b*c" ).toString() );
            System.out.println ( new StringBuilder ( "PostFix:\t" ).append ( s ).toString() );
            System.out.println();
            String s0 = InfixConverter.a ( "a+b*c/d-e" );
            System.out.println ( new StringBuilder ( "InFix  :\t" ).append ( "a+b*c/d-e" ).toString() );
            System.out.println ( new StringBuilder ( "PostFix:\t" ).append ( s0 ).toString() );
            System.out.println();
            String s1 = InfixConverter.a ( "a+b*c/d-e+f*h/i+j-k" );
            System.out.println ( new StringBuilder ( "InFix  :\t" ).append ( "a+b*c/d-e+f*h/i+j-k" ).toString() );
            System.out.println ( new StringBuilder ( "PostFix:\t" ).append ( s1 ).toString() );
            System.out.println();
            return;
        } else {
            String s2 = a[0];
            String s3 = InfixConverter.a ( s2 );
            System.out.println ( new StringBuilder ( "InFix  :\t" ).append ( s2 ).toString() );
            System.out.println ( new StringBuilder ( "PostFix:\t" ).append ( s3 ).toString() );
            System.out.println();
            return;
        }
    }
}
