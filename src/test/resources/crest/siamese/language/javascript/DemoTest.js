// (2) Function declaration: starts with "function"
function factorial(n) {
    if (n === 0) {
        return 1;
    }
    return n * factorial(n - 1);
}


//function factorial ( n ) { if ( n === 0 ) { return 1 ; } return n * factorial (n - 1 ) ; }