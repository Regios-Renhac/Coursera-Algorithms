/**
 * Algorithms Part 1
 * Week 3 assignment
 * Sergio Nacher Fernández
 * 
 * The included file contains all of the integers between 1 and 10,000 (inclusive) in unsorted order (with no integer repeated).
 * The integer in the ith row of the file gives you the ith entry of an input array.
 * 
 * Your task is to compute the total number of comparisons used to sort the given input file by QuickSort. As you know,
 * the number of comparisons depends on which elements are chosen as pivots, so we'll ask you to explore three different
 * pivoting rules. You should not count comparisons one-by-one. Rather, when there is a recursive call on a subarray of
 * length m, you should simply add m-1 to your running total of comparisons. (This is because the pivot element will be
 * compared to each of the other m-1 elements in the subarray in this recursive call.) WARNING: The Partition subroutine
 * can be implemented in several different ways, and different implementations can give you differing numbers of
 * comparisons. For this problem, you should implement the Partition subroutine as it is described in the video lectures
 * (otherwise you might get the wrong answer).
 * 
 * 1st part: For the first part of the programming assignment, you should always use the first element of the array as
 * the pivot element.
 * 
 * 2nd part: Compute the number of comparisons (as in Problem 1), always using the final element of the given array as
 * the pivot element. Again, be sure to implement the Partition subroutine as it is described in the video lectures.
 * Recall from the lectures that, just before the main Partition subroutine, you should exchange the pivot element
 * (i.e., the last element) with the first element.
 * 
 * 3rd part: Compute the number of comparisons (as in Problem 1), using the "median-of-three" pivot rule. [This primary
 * motivation behind this rule is to do a little bit of extra work to get much better performance on input arrays that
 * are already sorted.] In more detail, you should choose the pivot as follows. Consider the first, middle, and final
 * elements of the given array. (If the array has odd length it should be clear what the "middle" element is; for an
 * array with even length 2k, use the kth element as the "middle" element. So for the array: 4 5 6 7, the "middle"
 * element is the second one ---- 5 and not 6!) Identify which of these three elements is the median (i.e., the one
 * whose value is in between the other two), and use this as your pivot. As discussed in the first and second parts of
 * this programming assignment, be sure to implement Partition as described in the video lectures (including exchanging
 * the pivot element with the first element just before the main Partition subroutine).
 * SUBTLE POINT: A careful analysis would keep track of the comparisons made in identifying the median of the three
 * elements. You should NOT do this. That is, as in the previous two problems, you should simply add m-1 to your running
 * total of comparisons every time you recurse on a subarray with length m.
 * 
 */

package com.nacher.quicksort;

import java.io.*;
import java.util.*;

public class Quicksort {

	private static int qSort(int[] array, int begin, int end, PivotPicker picker) {
		// End condition
		if (end - begin < 2) {
			return 0;
		}
		// The picker chooses an element as the pivot and places it to the front
		picker.moveToFront(array, begin, end);
		// The array is partitioned using the first element as the pivot
		int pivotPosition = partition(array, begin, end);
		// The only remaining task is to sort the left and right intervals
		return end - begin - 1 + qSort(array, begin, pivotPosition, picker) + qSort(array, pivotPosition + 1, end, picker);
	}

	private interface PivotPicker {
		void moveToFront(int[] array, int begin, int end);
	}

	// Pick the first element in the array and use it as pivot
	private static class FirstElementPivotPicker implements PivotPicker {
		@Override
		public void moveToFront(int[] array, int begin, int end) {
			// Pivot is already the first element of the array interval
		}
	}

	// Pick the last element in the array and use it as pivot
	private static class LastElementPivotPicker implements PivotPicker {
		@Override
		public void moveToFront(int[] array, int begin, int end) {
			// We move the last element to the front
			int temp = array[begin];
			array[begin] = array[end - 1];
			array[end - 1] = temp;
		}
	}

	// Find the median of three between the first, last and middle elements
	// in the array and use it as pivot
	private static class MedianOfThreePivotPicker implements PivotPicker {
		@Override
		public void moveToFront(int[] array, int begin, int end) {
			int median;
			int mid = (begin + end - 1) / 2;
			if (array[begin] < array[mid]) {
				if (array[mid] < array[end - 1]) {
					median = mid;
				} else if (array[begin] < array[end - 1]) {
					median = end - 1;
				} else {
					median = begin;
				}
			} else {
				if (array[end - 1] < array[mid]) {
					median = mid;
				} else if (array[end - 1] < array[begin]) {
					median = end - 1;
				} else {
					median = begin;
				}
			}
			// We move the median element to the front
			int temp = array[begin];
			array[begin] = array[median];
			array[median] = temp;
		}
	}

	// This method partitions the array according to the pseudocode
	// in the lesson slides
	private static int partition(int[] array, int begin, int end) {
		int pivot = array[begin];
		int pivotPosition = begin;
		// We sweep the array interval to determine the pivot position
		for (int i = pivotPosition + 1; i < end; i++) {
			// As we sweep we also maintain the state of the traversed section
			// by moving elements smaller than the pivot to the front section
			if (array[i] < pivot) {
				int temp = array[pivotPosition + 1];
				array[pivotPosition + 1] = array[i];
				array[i] = temp;
				pivotPosition++;
			}
		}
		array[begin] = array[pivotPosition];
		array[pivotPosition] = pivot;
		return pivotPosition;
	}

	// This method counts the lines in the file to calculate the required size
	// of the input array
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
	
	// This method prepares the input array
	private static int[] getArray(String filename) {
		File file = new File(filename);
		int temp, k = 0;
		int count = 0;

		// Count the number of lines in the text file to find the correct size for the array
		try {
			count = countLines(filename);
		} catch (IOException e) {
			System.out.println("File not found!");
			System.exit(-1);
		}

		// Read every number in the text file line by line and add it to the array
		int inputArray[] = new int[count];
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				temp = sc.nextInt();
				sc.nextLine();
				inputArray[k] = temp;
				k++;
			}
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found!");
			System.exit(-1);
		}
		return inputArray;
	}

	public static void main(String[] args) {
		String filename = "C:\\Users\\Sergio Nacher\\Documents\\Java\\WEEK 3 - QUICKSORT\\src\\com\\nacher\\quicksort\\QuickSort.txt";
		
		// 1st part: using the first element as the pivot
		int inputArray1[] = getArray(filename);
		int sortCount1 = qSort(inputArray1, 0, inputArray1.length, new FirstElementPivotPicker());
		System.out.println(sortCount1);
		
		// 2nd part: using the last element as the pivot
		int inputArray2[] = getArray(filename);
		int sortCount2 = qSort(inputArray2, 0, inputArray2.length, new LastElementPivotPicker());
		System.out.println(sortCount2);
		
		// 3rd part: using the median of three for determining the pivot
		int inputArray3[] = getArray(filename);
		int sortCount3 = qSort(inputArray3, 0, inputArray3.length, new MedianOfThreePivotPicker());
		System.out.println(sortCount3);
	}

}
