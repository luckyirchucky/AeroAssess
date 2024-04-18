package com.luckyirchucky.ui.main;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;

// Define the system of differential equations
public class MyDifferentialEquations implements FirstOrderDifferentialEquations {
    // Define your differential equations here
    public int getDimension() {
        return 6; // Number of equations
    }

    public void computeDerivatives(double t, double[] Y, double[] yDot) {
        // Unpack state variables
        double y = Y[1];
        double alpha = Y[3];
        double beta = Y[4];
        double gamma = Y[5];

        // Unpack parameters (you may need to adjust these)
        double Ft = 10000;
        double V_wind = 200;
        double alpha1 = 0;
        double alpha2 = 0;
        double S_section = 10;
        double rho = 0.414;
        double m = 4100;
        double M = 640000;
        double Cy = 0.432;
        double A = 35;
        double B = 6;
        double C = 4;
        double d = 88.4;
        double CC = 0.139;

        // Define auxiliary variables
        double dx = 0.0; // Placeholder for dx/dt
        double dy = 0.0; // Placeholder for dy/dt
        double dz = 0.0; // Placeholder for dz/dt
        double dAlpha = 0.0; // Placeholder for d(alpha)/dt
        double dBeta = 0.0; // Placeholder for d(beta)/dt
        double dGamma = 0.0; // Placeholder for d(gamma)/dt

        // Compute derivatives for each equation
        dx = (6 * Ft * Math.cos(alpha) * Math.cos(gamma) - Cy * rho * 905 * Math.pow(dx + V_wind, 2) * Math.sin(alpha - alpha1) * Math.sin(beta) * Math.cos(gamma)
                - Cy * rho * 905 * Math.pow(dx + V_wind, 2) * Math.sin(alpha - alpha2) * Math.sin(beta) * Math.cos(gamma) - 0.5 * Math.cos(gamma) * Math.cos(alpha) * rho * S_section * CC * Math.pow(dx + V_wind, 2)) / (6 * m + M);

        dy = (Cy * rho * 905 * Math.pow(dx + V_wind, 2) * Math.cos(alpha - alpha1) * Math.sin(beta) + 6 * Ft * Math.sin(alpha)
                + 2 * Cy * rho * 905 * Math.pow(dx + V_wind, 2) * Math.cos(alpha - alpha2) * Math.sin(beta) - (6 * m + M) * 9.81
                - Math.cos(gamma) * Math.sin(alpha) * rho * S_section * CC * Math.pow(dx + V_wind, 2)) / (6 * m + M);

        dz = (6 * Ft * Math.cos(alpha) * Math.sin(gamma) + Cy * rho * 905 * Math.pow(dx + V_wind, 2) * Math.cos(alpha - alpha1) * Math.sin(beta) * Math.sin(gamma)
                + Cy * rho * 905 * Math.pow(dx + V_wind, 2) * Math.cos(alpha - alpha2) * Math.sin(beta) * Math.sin(gamma)
                - Math.sin(gamma) * Math.sin(alpha) * rho * S_section * CC * Math.pow(dx + V_wind, 2)) / (6 * m + M);

        dAlpha = (rho * S_section * CC * Math.pow(dx + V_wind, 2) * Math.cos(alpha - alpha1) * Math.sin(beta) - 2 * rho * S_section * CC * (dx + V_wind) * Math.cos(alpha - alpha2) * Math.sin(beta) * d / 2) / (M * (Math.pow(B, 2) + Math.pow(C, 2))) / 5;

        dBeta = (rho * S_section * CC * Math.pow(dx + V_wind, 2) * Math.sin(alpha - alpha1) * Math.sin(beta) * Math.cos(gamma) - rho * S_section * CC * Math.pow(dx + V_wind, 2) * Math.sin(alpha - alpha2) * Math.sin(beta) * Math.cos(gamma) * d / 2) / (M * (Math.pow(A, 2) + Math.pow(B, 2))) / 5;

        dGamma = (rho * S_section * CC * Math.pow(dx + V_wind, 2) * Math.cos(alpha - alpha1) * Math.sin(beta) * Math.sin(gamma)
                - rho * S_section * CC * Math.pow(dx + V_wind, 2) * Math.cos(alpha - alpha2) * Math.sin(beta) * Math.sin(gamma) * d / 2) / (M * (Math.pow(A, 2) + Math.pow(B, 2))) / 5;

        // Pack derivatives into yDot array
        yDot[0] = dx;
        yDot[1] = dy;
        yDot[2] = dz;
        yDot[3] = dAlpha;
        yDot[4] = dBeta;
        yDot[5] = dGamma;
    }
}
