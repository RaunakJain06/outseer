package org.example;

import java.io.*;
import java.util.*;

public class NumberSorter {

    public static void main(String[] args) {
        String inputFile = "/Users/raunak.jain/Documents/large_mobile_numbers.txt";
        String outputFile = "/Users/raunak.jain/Documents/sorted_mobile_numbers.txt";
        int chunkSize = 10000;
        try {
            sortLargeFile(inputFile, outputFile, chunkSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void sortLargeFile(String inputFile, String outputFile, int chunkSize) throws IOException {
        List<String> chunkFiles = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            List<String> chunk = new ArrayList<>();
            int count = 0;
            while ((line = br.readLine()) != null) {
                chunk.add(line);
                count++;
                if (count >= chunkSize) {
                    Collections.sort(chunk);
                    String chunkFile = "chunk_" + UUID.randomUUID().toString() + ".txt";
                    chunkFiles.add(chunkFile);
                    writeChunkToFile(chunk, chunkFile);
                    chunk.clear();
                    count = 0;
                }
            }
            if (!chunk.isEmpty()) {
                Collections.sort(chunk);
                String chunkFile = "chunk_" + UUID.randomUUID().toString() + ".txt";
                chunkFiles.add(chunkFile);
                writeChunkToFile(chunk, chunkFile);
            }
        }

        mergeSortedChunks(chunkFiles, outputFile);

        for (String chunkFile : chunkFiles) {
            new File(chunkFile).delete();
        }
    }
    private static void writeChunkToFile(List<String> chunk, String fileName) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (String number : chunk) {
                bw.write(number);
                bw.newLine();
            }
        }
    }
    private static void mergeSortedChunks(List<String> chunkFiles, String outputFile) throws IOException {
        PriorityQueue<BufferedReader> minHeap = new PriorityQueue<>(Comparator.comparingInt(NumberSorter::readNextInt));
        for (String chunkFile : chunkFiles) {
            BufferedReader br = new BufferedReader(new FileReader(chunkFile));
            minHeap.offer(br);
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            while (!minHeap.isEmpty()) {
                BufferedReader br = minHeap.poll();
                String line = br.readLine();
                if (line != null) {
                    bw.write(line);
                    bw.newLine();
                    minHeap.offer(br);
                } else {
                    br.close();
                }
            }
        }
    }
    private static int readNextInt(BufferedReader br) {
        try {
            String line = br.readLine();
            return (line != null) ? Integer.parseInt(line) : Integer.MAX_VALUE;
        } catch (IOException e) {
            return Integer.MAX_VALUE;
        }
    }
}
