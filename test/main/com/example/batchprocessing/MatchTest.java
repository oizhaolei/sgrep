package com.example.batchprocessing;

import org.junit.Test;

public class MatchTest {
	@Test
	public void test() {
		boolean matchLine = SuperGrep.doMatchLine("<?xml version=\"1.0\"?>", "10.128.142.126/32");
		System.out.println(matchLine);
	}
}
