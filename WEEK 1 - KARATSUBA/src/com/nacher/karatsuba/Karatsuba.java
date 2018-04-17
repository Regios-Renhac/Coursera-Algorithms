/**
 * Algorithms Part 1
 * Week 1 assignment
 * Sergio Nacher Fernández
 * 
 * In this programming assignment you will implement one or more of the integer multiplication algorithms described in lecture.
 * To get the most out of this assignment, your program should restrict itself to multiplying only pairs of single-digit numbers.
 * You can implement the grade-school algorithm if you want, but to get the most out of the assignment you’ll want to implement
 * recursive integer multiplication and/or Karatsuba’s algorithm.
 * 
 * What is the product of the following 64-digit numbers?
 * 3141592653589793238462643383279502884197169399375105820974944592
 * 2718281828459045235360287471352662497757247093699959574966967627
 */

/**
 * Reminder: Karatsuba multiplication consists of splitting the operands by the middle, multiplying the two front halves to get a,
 * multiplying the two back halves to get b, multiplying the sums of the halves of each operand to get c, subtracting a and b from its result
 * to get d, then putting a, d and b together in that order to get the final result.
 */

package com.nacher.karatsuba;

import java.util.Scanner;
import java.math.BigInteger;
	 
/** Karatsuba Class **/
public class Karatsuba {
	
	// CUTOFF defines the point in which the number of bits of a number is small enough to stop the recursion and perform the base case. 1536
	// ATTENTION: CUTOFF should be greater or equal to 32. Otherwise, an infinite recursion will occur.
		private static final int CUTOFF = 32;
		
		public static BigInteger multiplication(BigInteger x, BigInteger y) {
			if (x.bitLength() <= CUTOFF || y.bitLength() <= CUTOFF) {  // Base case, executed when numbers are small enough
				return x.multiply(y);
				
			} else {
				int n = Math.max(x.bitLength(), y.bitLength());
				int half = (n + 32) / 64 * 32;  // Middle point of the point, point where we 'split' the numbers for the multiplication
				BigInteger mask = BigInteger.ONE.shiftLeft(half).subtract(BigInteger.ONE); // Splitting point
				BigInteger xlow = x.and(mask);
				BigInteger ylow = y.and(mask);
				BigInteger xhigh = x.shiftRight(half);
				BigInteger yhigh = y.shiftRight(half);
				
				// The function will keep calling itself recursively until the bit length of the integers is low enough
				BigInteger a = multiplication(xhigh, yhigh);
				BigInteger b = multiplication(xlow, ylow);
				BigInteger c = multiplication(xlow.add(xhigh), ylow.add(yhigh));
				BigInteger d = c.subtract(a).subtract(b);
				
				return a.shiftLeft(half).add(d).shiftLeft(half).add(b);
			}
		}
	
	public static void main (String[] args) throws InterruptedException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Test of the Karatsuba multiplication algorithm\n");
		/** Accept two integers, inputed by keyboard **/
		System.out.println("Enter first integer...\n");
		BigInteger n1 = sc.nextBigInteger();
		System.out.println("Enter second integer...\n");
		BigInteger n2 = sc.nextBigInteger();
		sc.close();
		/** Call the multiply function in the Karatsuba class **/
		BigInteger product = multiplication(n1, n2);
		System.out.println("\nThe product is : "+ product);
	}
}
