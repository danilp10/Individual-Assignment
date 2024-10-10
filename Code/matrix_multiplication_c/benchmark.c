#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/resource.h>

extern double** createMatrix(int size);
extern void freeMatrix(double **matrix, int size);
extern double** matrixMultiply(double **A, double **B, int n);

void printMemoryUsage() {
    struct rusage usage;
    getrusage(RUSAGE_SELF, &usage);
    printf("Memory usage: %ld MB\n", usage.ru_maxrss / 1024);
}

double benchmarkMatrixMult(int size) {
    double **A = createMatrix(size);
    double **B = createMatrix(size);

    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            A[i][j] = (double)rand() / RAND_MAX;
            B[i][j] = (double)rand() / RAND_MAX;
        }
    }

    clock_t start = clock();
    double **C = matrixMultiply(A, B, size);
    clock_t end = clock();

    freeMatrix(A, size);
    freeMatrix(B, size);
    freeMatrix(C, size);

    return ((double)(end - start)) / CLOCKS_PER_SEC * 1000;
}

int main() {
    srand(time(NULL));

    int sizes[] = {10, 100, 250, 500, 1000};
    for (int i = 0; i < sizeof(sizes) / sizeof(sizes[0]); i++) {
        int size = sizes[i];
        double execTime = benchmarkMatrixMult(size);
        printf("Matrix size %dx%d - Execution time: %.2f ms\n", size, size, execTime);
        printMemoryUsage();
    }

    return 0;
}
