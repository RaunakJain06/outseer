package org.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.mockito.Mock;

import static org.junit.Assert.assertTrue;

public class NumberSorterTest {

    @Mock
    BufferedReader bufferedReader;

    @Test
    void testSortLargeFile() throws IOException {
        MockitoAnnotations.openMocks(this);
        List<String> inputLines = Arrays.asList("9876543210", "1234567890", "5555555555");
        when(bufferedReader.readLine()).thenReturn("9876543210", "1234567890", "5555555555", null);
        doNothing().when(bufferedReader).close();

        NumberSorter.sortLargeFile("/path/to/input/file", "/path/to/output/file", 3);
        // Here you can verify the behavior as expected after sorting the large file
    }

    @Test
    void testWriteChunkToFile() throws IOException {
        MockitoAnnotations.openMocks(this);
        List<String> chunk = Arrays.asList("9876543210", "1234567890", "5555555555");

        NumberSorter.writeChunkToFile(chunk, "test_chunk.txt");
        // Here you can verify that the file "test_chunk.txt" has been written correctly with the given chunk
    }

    @Test
    void testMergeSortedChunks() throws IOException {
        MockitoAnnotations.openMocks(this);
        List<String> chunkFiles = Arrays.asList("chunk1.txt", "chunk2.txt", "chunk3.txt");
        List<String> lines = Arrays.asList("1234567890", "5555555555", "9876543210");

        for (String chunkFile : chunkFiles) {
            BufferedReader br = mock(BufferedReader.class);
            when(br.readLine()).thenReturn(lines.remove(0)).thenReturn(null);
            doNothing().when(br).close();
            whenNew(BufferedReader.class).withArguments(new java.io.FileReader(chunkFile)).thenReturn(br);
        }

        NumberSorter.mergeSortedChunks(chunkFiles, "merged_file.txt");
        // Here you can verify the merged file's content or any other behavior you expect
    }
}

