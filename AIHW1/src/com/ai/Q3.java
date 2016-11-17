package com.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

/**
 * @author Sumit
 *
 */
public class Q3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		int i = 0;
		while (!(list.get(i) > 1370)) {
			if (list.get(i) > 1370) {
				break;
			}
			int nextVal[] = new int[3];
			nextVal = generateNextThree(list.get(i));
			for (int j = 0; j < nextVal.length; j++) {
				list.add(nextVal[j]);
			}
			i++;
		}
		int k = 0;
		int p = 0;
		for (int l = 0; l < i; l++) {
			System.out.print(" ");
		}
		System.out.print("(");
		for (int j = 0; j < i; j++) {
			if (j % 3 == 1) {
				System.out.print("(");
			}
			System.out.print(list.get(j));
			p++;
			if (j % 3 == 0) {
				System.out.print(")\t");
			} else {
				System.out.print(" ");
			}
			if (p % Math.pow(3, k) == 0) {
				System.out.println();
				for (int l = 0; l < i / (k + 1); l++) {
					System.out.print(" ");
				}
				k++;
				p = 0;
			}
		}
		System.out.print(")");
		System.out.println();
	}

	private static int[] generateNextThree(int label) {
		int nextVal[] = new int[3];
		for (int i = 1; i < 4; i++) {
			nextVal[i - 1] = label * label + i;
		}
		return nextVal;
	}

}
