package test.matrix;

import org.openjdk.jmh.annotations.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import com.sun.management.OperatingSystemMXBean;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import static org.example.MatrixMultiplication.matrixMultiply;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class TestMatrixMultiplication {

    @Param({"10", "100", "250", "500", "1000"})
    private int size;

    @Setup(Level.Trial)
    public void setup() {
        double[][] A = new double[size][size];
        double[][] B = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                A[i][j] = Math.random();
                B[i][j] = Math.random();
            }
        }

        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage beforeMemory = memoryBean.getHeapMemoryUsage();
        long beforeUsedMemory = beforeMemory.getUsed();

        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double cpuLoadBefore = osBean.getProcessCpuLoad() * 100;

        long startTime = System.nanoTime();
        matrixMultiply(A, B);
        long endTime = System.nanoTime();

        MemoryUsage afterMemory = memoryBean.getHeapMemoryUsage();
        long afterUsedMemory = afterMemory.getUsed();
        long memoryUsed = afterUsedMemory - beforeUsedMemory;

        double executionTime = (endTime - startTime) / 1e6;
        double cpuLoadAfter = osBean.getProcessCpuLoad() * 100;

        printResults(size, executionTime, memoryUsed, cpuLoadBefore, cpuLoadAfter);
    }

    @Benchmark
    public void benchmarkMatrixMult() {
        double[][] A = new double[size][size];
        double[][] B = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                A[i][j] = Math.random();
                B[i][j] = Math.random();
            }
        }
        matrixMultiply(A, B);
    }

    private static void printResults(int size, double executionTime, long memoryUsed, double cpuBefore, double cpuAfter) {
        System.out.println("\nMatrix size " + size + "x" + size);
        System.out.println("Execution time: " + executionTime + " ms");
        System.out.println("Memory used: " + memoryUsed / (1024 * 1024) + " MB");
        System.out.println("CPU usage before: " + cpuBefore + " %");
        System.out.println("CPU usage after: " + cpuAfter + " %\n");
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(TestMatrixMultiplication.class.getSimpleName())
                .build();

        new Runner(opt).run();
    }
}
