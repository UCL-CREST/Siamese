class SqrtAlgorithm {
    SqrtAlgorithm() {
        super();
    }
    public static double SqrtByAlogorithm ( double d ) {
        long j = 0L;
        long j0 = 0L;
        int i = 0;
        int i0 = 0;
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        long j1 = ( long ) d;
        long j2 = ( long ) ( ( d - ( double ) j1 ) * 1000000.0 );
        long j3 = j1;
        int i4 = 0;
        while ( j3 >= 10L ) {
            long j4 = j3 / 10L;
            int i5 = i4 + 1;
            j3 = j4;
            i4 = i5;
        }
        int i6 = i4 + 1;
        long j5 = j2;
        int i7 = 0;
        while ( j5 >= 10L ) {
            long j6 = j5 / 10L;
            int i8 = i7 + 1;
            j5 = j6;
            i7 = i8;
        }
        int i9 = i7 + 1;
        if ( i6 % 2 != 1 ) {
            j = j1;
            j0 = j2;
            i = i6;
            i0 = i9;
            i1 = 0;
            i2 = 0;
            i3 = 0;
        } else {
            j = j1;
            j0 = j2;
            i = i6;
            i0 = i9;
            i1 = 0;
            i2 = 1;
            i3 = 0;
        }
        while ( true ) {
            int i10 = 0;
            int i11 = 0;
            if ( i2 != 1 ) {
                i10 = i - 2;
                i11 = 0;
            } else {
                i10 = i - 1;
                i11 = 1;
            }
            int i12 = 1;
            int i13 = 0;
            while ( i13 < i10 ) {
                int i14 = i12 * 10;
                int i15 = i13 + 1;
                i12 = i14;
                i13 = i15;
            }
            int i16 = ( int ) j / i12;
            int i17 = i1 * 2;
            int i18 = 1;
            int i19 = i17;
            while ( true ) {
                int i20 = 0;
                int i21 = 0;
                double d0 = 0.0;
                label3: {
                    label4: {
                        label5: {
                            int i22 = 0;
                            label6: {
                                label7: {
                                    if ( i19 == 0 ) {
                                        break label7;
                                    }
                                    if ( i16 - i18 * ( i19 * 10 + i18 ) >= 0 ) {
                                        i22 = i19;
                                        break label6;
                                    } else {
                                        break label5;
                                    }
                                }
                                if ( i16 - i18 * i18 >= 0 ) {
                                    i22 = 0;
                                } else {
                                    break label4;
                                }
                            }
                            i18 = i18 + 1;
                            i19 = i22;
                            continue;
                        }
                        i20 = i19 / 2 * 10 + i18 - 1;
                        break label3;
                    }
                    i20 = i18 - 1;
                }
                int i23 = i20 / 10;
                int i24 = i20 % 10;
                long j7 = ( i23 != 0 ) ? j - ( long ) ( ( i23 * 2 * 10 + i24 ) * i24 * i12 ) : j - ( long ) ( i20 * i20 * i12 );
                int i25 = ( j7 < 0L ) ? -1 : ( j7 == 0L ) ? 0 : 1;
                label0: {
                    label2: {
                        if ( i25 != 0 ) {
                            break label2;
                        }
                        if ( j0 != 0L ) {
                            break label2;
                        }
                        if ( i10 <= 0 ) {
                            i21 = i20;
                            break label0;
                        } else {
                            int i26 = i10 / 2;
                            int i27 = 1;
                            int i28 = 0;
                            while ( i28 < i26 ) {
                                int i29 = i27 * 10;
                                int i30 = i28 + 1;
                                i27 = i29;
                                i28 = i30;
                            }
                            i21 = i20 * i27;
                            break label0;
                        }
                    }
                    int i31 = ( i11 != 1 ) ? i + -2 : i + -1;
                    if ( i31 > 0 ) {
                        j = j7;
                        i = i31;
                        i1 = i20;
                        i2 = 0;
                        break;
                    }
                    int i32 = ( j7 < 0L ) ? -1 : ( j7 == 0L ) ? 0 : 1;
                    label1: {
                        if ( i32 > 0 ) {
                            break label1;
                        }
                        if ( j0 <= 0L ) {
                            i21 = i20;
                            break label0;
                        }
                    }
                    if ( i3 < 5 ) {
                        long j8 = 0L;
                        long j9 = 0L;
                        int i33 = 0;
                        int i34 = i3 + 1;
                        long j10 = j7 * 100L;
                        if ( j0 <= 0L ) {
                            j8 = j10;
                            j9 = j0;
                            i33 = i0;
                        } else {
                            int i35 = i0 + -2;
                            int i36 = 1;
                            int i37 = 0;
                            while ( i37 < i35 ) {
                                int i38 = i36 * 10;
                                int i39 = i37 + 1;
                                i36 = i38;
                                i37 = i39;
                            }
                            long j11 = j10 + j0 / ( long ) i36;
                            long j12 = j0 % ( long ) i36;
                            j8 = j11;
                            j9 = j12;
                            i33 = i35;
                        }
                        int i40 = i31 + 2;
                        j = j8;
                        j0 = j9;
                        i = i40;
                        i0 = i33;
                        i1 = i20;
                        i2 = 0;
                        i3 = i34;
                        break;
                    }
                    i21 = i20;
                }
                if ( i3 != 0 ) {
                    int i41 = 1;
                    int i42 = 0;
                    while ( i42 < i3 ) {
                        int i43 = i41 * 10;
                        int i44 = i42 + 1;
                        i41 = i43;
                        i42 = i44;
                    }
                    d0 = ( double ) i21 / ( double ) i41;
                } else {
                    d0 = ( double ) i21;
                }
                return d0;
            }
        }
    }
    public static void main ( String[] a ) {
        double d = 0.0;
        while ( d <= 10000.0 ) {
            System.out.print ( "sqrt(" );
            System.out.print ( d );
            System.out.print ( ") = " );
            System.out.print ( SqrtAlgorithm.SqrtByAlogorithm ( d ) );
            System.out.print ( ", " );
            System.out.println ( Math.sqrt ( d ) );
            d = d + 50.0;
        }
    }
}
