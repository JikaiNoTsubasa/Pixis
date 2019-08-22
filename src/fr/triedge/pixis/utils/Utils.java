package fr.triedge.pixis.utils;

public class Utils {

	public static int roundDown(double x) {
		return (int)(x-(x%1));
	}
	
	public static int roundDown(long x) {
		return (int)(x-(x%1));
	}
}
