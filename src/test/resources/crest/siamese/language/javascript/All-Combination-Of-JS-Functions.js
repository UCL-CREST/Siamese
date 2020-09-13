"use strict";

// (1) traditional function declaration
function sum(a, b) {
    return a + b;
}

// (2) Function declaration: starts with "function"
function factorial(n) {
    if (n === 0) {
        return 1;
    }
    return n * factorial(n - 1);
}

// (3) nested function with outer and inner function block
function myFunction(a, b) {
    // (4) inner function
    function innerFunction(c, d) {
        return c + d;
    }

    return a * b;
}

// (5) Function declaration with self-calling anonymous functional expression
(function () {
    'use strict';
    if (true) {
        // (6)  inner anonymous function
        function innerFunctionOne() {
            return 'true ok';
        }
    } else {
        // (7) inner  anonymous function
        function innerFunctionTwo() {
            return 'false ok';
        }
    }
    console.log(typeof ok === 'undefined');
    console.log(ok());
})();


const methods = {
    numbers: [1, 5, 8],
    sum: function () { // (8) Function declaration inside as function
        return this.numbers.reduce(function (acc, num) { // (9) Function expression as parameter
            return acc + num;
        });
    }
}

    // (10) Function declaration (IIFE): starts with "(" self calling function
    // (counted twice function declaration and expression)
    (function messageFunction(message) {
        return message + ' World!';
    })('Hello');


// (11) Anonymous function, which name is an empty string.
(
    function (variable) {
        return typeof variable;
    }
).name;


// (12) Arrow function assigned to a variable
const absValue = (number) => {
    if (number < 0) {
        return -number;
    }
    return number;
}

// (13) Arrow function in expression body
var pairs = evens.map(v => ({
    even: v,
    odd: v + 1
}))

// (14) Arrow function in statement body
nums.forEach(v => {
    if (v % 5 === 0)
        fives.push(v)
})


// (15) Arrow function in current object context.
this.nums.forEach((v) => {
    if (v % 5 === 0)
        this.fives.push(v)
})


// (16) Generator function as function declaration with direct Use
function* range(start, end, step) {
    while (start < end) {
        yield start
        start += step
    }
}

// (17) Generator function as functional expression
let fibonacci = function* (numbers) {
    let pre = 0,
        cur = 1
    while (numbers-- > 0) {
        [pre, cur] = [cur, pre + cur]
        yield cur
    }
}


// (18) traditional async function (count twice)
async function asyncFunction() {
    console.log("Insider async generator function");
}

// (19) async generator function as functional expression (count twice)
async function* gen() {
    console.log("Insider async generator function");
}

// (20) async arrow function
async () => {
    await f();
}

// (21) strict function in outer block
function strict() {
    /* comment */
    // comment
    // comment
    'use strict';

    // (22) strict function in inner block
    function nestedStrictFunction() {
        let a = 4; // strict, "let" is not an identifier.
        return 'And so am I!';
    }

    return "Hi!  I'm a strict mode function!  " + nestedStrictFunction();
}


class Car {
    // (23) method as class constructor
    constructor(brand) {
        this.carname = brand;
    }

    // (24) non static class member method
    present() {
        return "I have a " + this.carname;
    }

    // (25) static class member method
    static startCar() {
        return "The car is started";
    }


    // (26) generator  function class member method
    * stopCar() {
        return "The car is stopped";
    }

    // (27) async class member method
    async asyncClassMethod() {
    }

    // (28) static async class member method
    static async staticAsyncClassMethod() {
    }

    // (28) async static  class member method
    async static #asyncStaticClassMethod() {
    }

    // (30) async generator class member method
    async* gen() {
    }
}