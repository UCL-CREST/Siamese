"use strict";


(number, X, Y) => {
    console.log(this === numbersObject); // => true
    this.array.push(number);
}

//Context transparency
class Numbers {
    constructor(array) {
        this.array = array;
    }

    addNumber(number) {
        if (number !== undefined) {
            this.array.push(number);
        }
        return (number) => {
            console.log(this === numbersObject); // => true
            this.array.push(number);
        };
    }
}

//Generator function
function* indexGenerator(x, y) {
    var index = 0;
    while (true) {
        yield index++;
    }
}


// (1) traditional function declaration
function sum(a, b) {
    return a + b;
}

// (4) Function expression: starts with "const"
const isTruthy = function (value) {
    return !!value;
};