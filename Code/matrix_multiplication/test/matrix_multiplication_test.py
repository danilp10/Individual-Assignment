import numpy as np
import pytest
from src.matrix_multiplication import matrix_multiply
import psutil
from memory_profiler import memory_usage
import time


@pytest.mark.parametrize("size", [10, 100, 250, 500])
def test_matrix_multiply(benchmark, size):
    matrix_a = np.random.rand(size, size)
    matrix_b = np.random.rand(size, size)

    mem_before = memory_usage(-1, interval=0.1, timeout=1)[0]
    cpu_before = psutil.cpu_percent(interval=None)
    start_time = time.perf_counter() * 1000

    benchmark.pedantic(matrix_multiply, args=(matrix_a, matrix_b), warmup_rounds=5, iterations=1, rounds=1)

    end_time = time.perf_counter() * 1000
    execution_time = end_time - start_time
    mem_after = memory_usage(-1, interval=0.1, timeout=1)[0]
    cpu_after = psutil.cpu_percent(interval=None)

    print(f"\nSize: {size} x {size}")
    print(f"Execution time: {execution_time:.4f} ms")
    print(f"Memory used: {mem_after-mem_before} MB")
    print(f"CPU used before: {cpu_before} %")
    print(f"CPU used after: {cpu_after} %")


