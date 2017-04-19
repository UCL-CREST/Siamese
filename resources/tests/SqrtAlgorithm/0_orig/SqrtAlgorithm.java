import java.io.*;
import java.lang.*;
class SqrtAlgorithm {
    public static double SqrtByAlogorithm ( double x ) {
        long numeric = ( long ) x;
        long n = numeric;
        long fraction = ( long ) ( ( x - numeric ) * 1000000 );
        long f = fraction;
        int numdigits = 0, fnumdigits = 0, currdigits = 0;
        int tempresult = 0;
        int bOdd = 0, part = 0, tens = 1;
        int fractioncount = 0;
        double result = 0;
        int k, f1, f2, i, num, temp, quotient;
        for ( numdigits = 0; n >= 10; numdigits++ ) {
            n = ( n / 10 );
        }
        numdigits++;
        for ( fnumdigits = 0; f >= 10; fnumdigits++ ) {
            f = ( f / 10 );
        }
        fnumdigits++;
        if ( ( numdigits % 2 ) == 1 ) {
            bOdd = 1;
        }
        while ( true ) {
            tens = 1;
            currdigits = ( bOdd == 1 ) ? ( numdigits - 1 ) : ( numdigits - 2 );
            for ( k = 0; k < currdigits; k++ ) {
                tens *= 10;
            }
            part = ( int ) numeric / tens;
            num = part;
            quotient = tempresult * 2;
            i = 0;
            temp = 0;
            for ( i = 1; ; i++ ) {
                if ( quotient == 0 ) {
                    if ( num - i * i < 0 ) {
                        tempresult = ( i - 1 );
                        break;
                    }
                } else {
                    temp = quotient * 10 + i;
                    if ( num - i * temp < 0 ) {
                        tempresult = quotient / 2 * 10 + i - 1;
                        break;
                    }
                }
            }
            f1 = tempresult / 10;
            f2 = tempresult % 10;
            if ( f1 == 0 ) {
                numeric = numeric - ( tempresult * tempresult * tens );
            } else {
                numeric = numeric - ( ( f1 * 2 * 10 + f2 ) * f2 * tens );
            }
            if ( numeric == 0 && fraction == 0 ) {
                if ( currdigits > 0 ) {
                    tens = 1;
                    currdigits = currdigits / 2;
                    for ( k = 0; k < currdigits; k++ ) {
                        tens *= 10;
                    }
                    tempresult *= tens;
                }
                break;
            }
            if ( bOdd == 1 ) {
                numdigits -= 1;
                bOdd = 0;
            } else {
                numdigits -= 2;
            }
            if ( numdigits <= 0 ) {
                if ( numeric > 0 || fraction > 0 ) {
                    if ( fractioncount >= 5 ) {
                        break;
                    }
                    fractioncount++;
                    numeric *= 100;
                    if ( fraction > 0 ) {
                        fnumdigits -= 2;
                        tens = 1;
                        for ( k = 0; k < fnumdigits; k++ ) {
                            tens *= 10;
                        }
                        numeric += fraction / tens;
                        fraction = fraction % tens;
                    }
                    numdigits += 2;
                } else {
                    break;
                }
            }
        }
        if ( fractioncount == 0 ) {
            result = tempresult;
        } else {
            tens = 1;
            for ( k = 0; k < fractioncount; k++ ) {
                tens *= 10;
            }
            result = ( double ) tempresult / tens;
        }
        return result;
    }
    public static void main ( String[] args ) {
        for ( double d = 0; d <= 10000; d += 50 ) {
            System.out.print ( "sqrt(" );
            System.out.print ( d );
            System.out.print ( ") = " );
            System.out.print ( SqrtByAlogorithm ( d ) );
            System.out.print ( ", " );
            System.out.println ( Math.sqrt ( d ) );
        }
    }
}
