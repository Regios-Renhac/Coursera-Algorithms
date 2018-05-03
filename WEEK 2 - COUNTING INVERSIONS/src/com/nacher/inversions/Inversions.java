/**
 * Algorithms Part 1
 * Week 2 assignment
 * Sergio Nacher Fernández
 * The file contains all the 100,000 integers between 1 and 100,000 (including both) in some random order( no integer is repeated).
 *
 * Your task is to find the number of inversions in the file given (every row has a single integer between 1 and 100,000).
 * Assume your array is from 1 to 100,000 and ith row of the file gives you the ith entry of the array.
 * Write a program and run over the file given. The numeric answer should be written in the space.
 * So if your answer is 1198233847, then just type 1198233847 in the space provided without any space / commas / any other punctuation marks.
 *
 */
 
package com.nacher.inversions;

import java.io.*;
import java.util.Scanner;
import java.math.BigDecimal;

public class Inversions {
	
	private static BigDecimal total = new BigDecimal(0);

    // First off, we split the main array into subarrays
	public int[] divideAndConquer(int[] inputArray) {
		int n = inputArray.length;
	    if(n == 1) {
	    	return inputArray;
	    }
	    
	    int mid = n/2;
	    int[] leftArray = new int[mid];
	    int[] rightArray = new int[n - mid];
	    
	    System.arraycopy(inputArray, 0, leftArray, 0, leftArray.length);
	    System.arraycopy(inputArray, leftArray.length, rightArray, 0, rightArray.length);
	    
	    divideAndConquer(leftArray);
	    divideAndConquer(rightArray);
	    sortAndMerge(leftArray, rightArray, inputArray);
	    
	    return inputArray;
	}

    // Then, we merge the split arrays, sorting them and counting the inversions
	public void sortAndMerge(int[] leftArray, int[] rightArray, int[] sortedArray) {
		int leftArrayLength = leftArray.length;
		int rightArrayLength = rightArray.length;
		int i = 0;
		int j = 0;
		int k = 0;
		
		while(i < leftArrayLength && j < rightArrayLength) {
			if(leftArray[i] < rightArray[j]) {
				sortedArray[k] = leftArray[i];
				i++;
			} else {
				//This is an inversion. We must increase the total
				sortedArray[k] = rightArray[j];
				j++;
				total = total.add(new BigDecimal(leftArray.length - i));
			}
			k++;
		}
		
		while(i < leftArrayLength) {
			sortedArray[k] = leftArray[i];
			i++;
			k++;
		}
		
		while(j < rightArrayLength) {
			sortedArray[k] = rightArray[j];
			j++;
			k++;
		}
	}

    // This method counts the lines in the file to calculate the required size of the input array
    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n')
                        ++count;
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

	public static void main(String[] args) {
		File file = new File("C:\\Users\\Sergio Nacher\\Documents\\Java\\WEEK 2 - COUNTING INVERSIONS\\src\\com\\nacher\\inversions\\IntegerArray.txt");
        Inversions ci = new Inversions();
		int temp, k = 0;
        int count = 0;

        /* Count the number of lines in the text file to find the correct size for the array */
        try {
            count = countLines("C:\\Users\\Sergio Nacher\\Documents\\Java\\WEEK 2 - COUNTING INVERSIONS\\src\\com\\nacher\\inversions\\IntegerArray.txt");
        }
        catch (IOException e) {
            System.out.println("File not found!");
        }

        /* Read every number in the text file line by line and add it to the array */
        int originalArray[] = new int[count];
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) 
            {
                temp = sc.nextInt();
                sc.nextLine();
                originalArray[k] = temp;
                k++;
            }
            sc.close();
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        }
        
        int[] sortedArray = ci.divideAndConquer(originalArray);
        System.out.println("Number of inversions: " + total);

	}

}
