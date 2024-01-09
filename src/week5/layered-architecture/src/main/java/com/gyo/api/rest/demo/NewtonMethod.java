package com.gyo.api.rest.demo;

public class NewtonMethod {

    private static final double EPSILON = 0.0000000001;

    public boolean goodEnough(double guess, double x) {

        if (guess < 0) {
            return false;
        }

        return Math.pow(guess, 2) - x < EPSILON;
    }

    public double improve(double guess, double x) {
        return average(guess, x / guess);
    }

    public double sqrtIter(double guess, double x) {
        if (goodEnough(guess, x)) {
            return guess;
        }
        return sqrtIter(improve(guess, x), x);
    }


    private double average(double x, double y) {
        return (x + y) / 2;
    }

    public double sqrt(double x) {
        return sqrtIter(1, x);
    }
}
