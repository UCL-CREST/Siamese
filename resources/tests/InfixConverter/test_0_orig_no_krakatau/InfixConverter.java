class InfixConverter {
    InfixConverter() {
        super();
    }
    public static String ReadString() {
        String s = null;
        try {
            s = new java.io.BufferedReader ( ( java.io.Reader ) new java.io.InputStreamReader ( System.in ) ).readLine();
        } catch ( Exception a ) {
            a.printStackTrace();
            return "";
        }
        return s;
    }
    public static int ReadInteger() {
        int i = 0;
        try {
            i = Integer.parseInt ( new java.io.BufferedReader ( ( java.io.Reader ) new java.io.InputStreamReader ( System.in ) ).readLine() );
        } catch ( Exception a ) {
            a.printStackTrace();
            return 0;
        }
        return i;
    }
    public static String InfixToPostfixConvert ( String s ) {
        java.util.Stack a = new java.util.Stack();
        String s0 = "";
        int i = 0;
        while ( i < s.length() ) {
            String s1 = null;
            int i0 = 0;
            int i1 = s.charAt ( i );
            label7: {
                label8: {
                    if ( i1 == 43 ) {
                        break label8;
                    }
                    if ( i1 == 45 ) {
                        break label8;
                    }
                    if ( i1 == 42 ) {
                        break label8;
                    }
                    if ( i1 == 47 ) {
                        break label8;
                    }
                    s1 = new StringBuilder().append ( s0 ).append ( ( char ) i1 ).toString();
                    i0 = i;
                    break label7;
                }
                if ( a.size() > 0 ) {
                    int i2 = 0;
                    String s2 = null;
                    int i3 = 0;
                    Character a0 = ( Character ) a.peek();
                    int i4 = a0.charValue();
                    label4: {
                        label5: {
                            label6: {
                                if ( i4 == 42 ) {
                                    break label6;
                                }
                                int i5 = a0.charValue();
                                if ( i5 != 47 ) {
                                    break label5;
                                }
                            }
                            i2 = 1;
                            break label4;
                        }
                        i2 = 0;
                    }
                    label0: if ( i2 != 1 ) {
                        label3: {
                            if ( i1 == 43 ) {
                                break label3;
                            }
                            if ( i1 == 45 ) {
                                break label3;
                            }
                            a.push ( ( Object ) Character.valueOf ( ( char ) i1 ) );
                            s2 = s0;
                            i3 = i;
                            break label0;
                        }
                        String s3 = new StringBuilder().append ( s0 ).append ( a.pop() ).toString();
                        a.push ( ( Object ) Character.valueOf ( ( char ) i1 ) );
                        s2 = s3;
                        i3 = i;
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
                            String s4 = new StringBuilder().append ( s0 ).append ( a.pop() ).toString();
                            int i6 = i + -1;
                            s2 = s4;
                            i3 = i6;
                            break label0;
                        }
                        String s5 = new StringBuilder().append ( s0 ).append ( a.pop() ).toString();
                        int i7 = i + -1;
                        s2 = s5;
                        i3 = i7;
                    }
                    s1 = s2;
                    i0 = i3;
                } else {
                    a.push ( ( Object ) Character.valueOf ( ( char ) i1 ) );
                    s1 = s0;
                    i0 = i;
                }
            }
            int i8 = i0 + 1;
            s0 = s1;
            i = i8;
        }
        int i9 = a.size();
        String s6 = s0;
        int i10 = 0;
        while ( i10 < i9 ) {
            String s7 = new StringBuilder().append ( s6 ).append ( a.pop() ).toString();
            int i11 = i10 + 1;
            s6 = s7;
            i10 = i11;
        }
        return s6;
    }
    public static void main ( String[] a ) {
        if ( a.length != 1 ) {
            String s = InfixConverter.InfixToPostfixConvert ( "a+b*c" );
            System.out.println ( new StringBuilder().append ( "InFix  :\t" ).append ( "a+b*c" ).toString() );
            System.out.println ( new StringBuilder().append ( "PostFix:\t" ).append ( s ).toString() );
            System.out.println();
            String s0 = InfixConverter.InfixToPostfixConvert ( "a+b*c/d-e" );
            System.out.println ( new StringBuilder().append ( "InFix  :\t" ).append ( "a+b*c/d-e" ).toString() );
            System.out.println ( new StringBuilder().append ( "PostFix:\t" ).append ( s0 ).toString() );
            System.out.println();
            String s1 = InfixConverter.InfixToPostfixConvert ( "a+b*c/d-e+f*h/i+j-k" );
            System.out.println ( new StringBuilder().append ( "InFix  :\t" ).append ( "a+b*c/d-e+f*h/i+j-k" ).toString() );
            System.out.println ( new StringBuilder().append ( "PostFix:\t" ).append ( s1 ).toString() );
            System.out.println();
        } else {
            String s2 = a[0];
            String s3 = InfixConverter.InfixToPostfixConvert ( s2 );
            System.out.println ( new StringBuilder().append ( "InFix  :\t" ).append ( s2 ).toString() );
            System.out.println ( new StringBuilder().append ( "PostFix:\t" ).append ( s3 ).toString() );
            System.out.println();
        }
    }
}
