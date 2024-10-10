#ifndef MATRIX_MULTIPLICATION_H
#define MATRIX_MULTIPLICATION_H

double** createMatrix(int size);
void freeMatrix(double **matrix, int size);
double** matrixMultiply(double **A, double **B, int n);
void printMemoryUsage();

#endif
