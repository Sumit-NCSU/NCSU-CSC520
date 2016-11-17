package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FilePathTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String basePath = new File("").getAbsolutePath();

		System.out.println(basePath.concat("\\usroads.pl"));
		FileInputStream fis = new FileInputStream("F:\\Work\\Java\\Workspace\\AIHW2\\src\\com\\hw\\usroads.pl");
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}

}
