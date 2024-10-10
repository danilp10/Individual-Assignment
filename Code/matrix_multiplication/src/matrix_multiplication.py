def matrix_multiply(matrix_a, matrix_b):
    n = len(matrix_a)
    matrix_c = []
    for i in range(n):
        matrix_c.append([])
        for j in range(n):
            matrix_c[i].append(0)
    for i in range(n):
        for j in range(n):
            for k in range(n):
                matrix_c[i][j] += matrix_a[i][k] * matrix_b[k][j]
    return matrix_c
