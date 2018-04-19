package org.uw.algo;

import java.math.BigInteger;
import java.util.Scanner;

public class Pollard_Rho {

	public static void main(String[] args) {
		System.out
		    .println("Please Provide following values in new line a(x),a(y),d,p,n,N(number of runs)");
		Scanner sc = new Scanner(System.in);
		BigInteger[] a = new BigInteger[2];
		a[0] = sc.nextBigInteger();
		a[1] = sc.nextBigInteger();
		BigInteger d = sc.nextBigInteger();
		BigInteger p = sc.nextBigInteger();
		BigInteger n = sc.nextBigInteger();
		int N = sc.nextInt();
		sc.close();
		Pollard_Rho obj = new Pollard_Rho();
		System.out.println("Average steps = " + obj.avgsteps(N, a, d, p, n));
	}

	/*
	 * Finds the multiplication of two points(a,b)
	 * c(x3,y3)=((x1y2+y1x2/(1+dx1x2y1y2)),(y1y2-x1x2/(1-dx1x2y1y2)))
	 */

	BigInteger[] mul(BigInteger[] a, BigInteger[] b, BigInteger d, BigInteger p) {
		BigInteger[] a3 = new BigInteger[2];
		BigInteger x1 = a[0];
		BigInteger y1 = a[1];
		BigInteger x2 = b[0];
		BigInteger y2 = b[1];
		BigInteger numx3 = ((x1.multiply(y2)).mod(p).add((y1.multiply(x2)).mod(p)))
		    .mod(p);
		BigInteger deno = ((((((d.multiply(x1)).mod(p).multiply(x2)).mod(p))
		    .multiply(y1)).mod(p)).multiply(y2)).mod(p);
		BigInteger denox3 = (deno.add(BigInteger.valueOf(1))).modInverse(p);
		BigInteger denoy3 = ((BigInteger.valueOf(1)).subtract(deno))
		    .modInverse(p);
		BigInteger numy3 = (((y1.multiply(y2)).mod(p)).subtract((x1.multiply(x2))
		    .mod(p))).mod(p);
		a3[0] = (numx3.multiply(denox3)).mod(p);
		a3[1] = (numy3.multiply(denoy3)).mod(p);
		return a3;

	}

	/* Finds the exponential of given input b=a power m */

	BigInteger[] exp(BigInteger[] a, BigInteger m, BigInteger d, BigInteger p) {
		BigInteger[] b = new BigInteger[2];
		b[0] = a[0].mod(p);
		b[1] = a[1].mod(p);
		if (m.compareTo(BigInteger.ONE) == 0) {
			return b;
		}
		for (BigInteger i = BigInteger.valueOf(2); i.compareTo(m) <= 0; i = i
			    .add(BigInteger.ONE)) {
			b = mul(b, a, d, p);
			b[0] = b[0].mod(p);
			b[1] = b[1].mod(p);
		}
		return b;
	}

	/* Finds the 6 constants alphak,betak,zk,alpha2k,beta2k,z2k */

	BigInteger[] getnextsextuple(BigInteger[] sextuple, BigInteger[] a,
	    BigInteger[] b, BigInteger d, BigInteger p, BigInteger n) {
		  BigInteger[] resulttuple=new BigInteger[4];

		BigInteger[] zprev = { new BigInteger(sextuple[2].toString()),
		    new BigInteger(sextuple[3].toString()) };
		BigInteger xcoord = zprev[0].mod(BigInteger.valueOf(3));
		if (xcoord.longValue() == 0) {
			BigInteger[] z = mul(b, zprev, d, p);
			resulttuple[0] = (sextuple[0].add(BigInteger.ONE)).mod(n);
			resulttuple[1] = sextuple[1];
			resulttuple[2] = z[0];
			resulttuple[3] = z[1];
		} else if (xcoord.longValue() == 1) {
			BigInteger[] z = mul(zprev, zprev, d, p);
			resulttuple[0] = (BigInteger.valueOf(2).multiply(sextuple[0])).mod(n);
			resulttuple[1] = (BigInteger.valueOf(2).multiply(sextuple[1])).mod(n);
			resulttuple[2] = z[0];
			resulttuple[3] = z[1];
		} else {
			BigInteger[] z = mul(a, zprev, d, p);
			resulttuple[0] = sextuple[0];
			resulttuple[1] = (sextuple[1].add(BigInteger.ONE)).mod(n);
			resulttuple[2] = z[0];
			resulttuple[3] = z[1];
		}
			
		return resulttuple;

	}

	/* Checks if zk=z2k */

	BigInteger[] rho(BigInteger[] a, BigInteger[] b, BigInteger d, BigInteger p,
	    BigInteger n) {
		BigInteger[] result = new BigInteger[2];

		BigInteger[] sextuplek = { BigInteger.ZERO, BigInteger.ZERO,
		    BigInteger.ZERO, BigInteger.ONE };
		BigInteger[] sextuple2k = { BigInteger.ZERO, BigInteger.ZERO,
		    BigInteger.ZERO, BigInteger.ONE };
		BigInteger k = BigInteger.ZERO;
		for (BigInteger i = BigInteger.valueOf(1); i.compareTo(n) == -1; i = i
		    .add(BigInteger.ONE)) {
			sextuplek = getnextsextuple(sextuplek, a, b, d, p, n);
			sextuple2k = getnextsextuple(sextuple2k, a, b, d, p, n);
			sextuple2k = getnextsextuple(sextuple2k, a, b, d, p, n);
			k = k.add(BigInteger.ONE);
			if ((sextuplek[2].compareTo(sextuple2k[2]) == 0)
			    && (sextuplek[3].compareTo(sextuple2k[3]) == 0)) {
				break;
			}
		}

		BigInteger num = (sextuple2k[1].subtract(sextuplek[1])).mod(n);
		BigInteger deno = (sextuplek[0].subtract(sextuple2k[0])).modInverse(n);
		BigInteger m = (num.multiply(deno)).mod(n);
		result[0] = m;
		result[1] = k;
		return result;
	}

	/* Checks if the algorithm is correct */

	long check(BigInteger[] a, BigInteger d, BigInteger p, BigInteger n) {
		long steps = 0l;
		long leftLimit = 1L;
		long rightLimit = 10000000L;
		long generatedLong = leftLimit
		    + (long) (Math.random() * (rightLimit - leftLimit));
		BigInteger m = (BigInteger.valueOf(generatedLong)).mod(n);

		BigInteger[] b = exp(a, m, d, p);


		BigInteger[] result = rho(a, b, d, p, n);

		if (m.compareTo(result[0]) == 0) {
			System.out.println("m is" + m);
			System.out.println("b is" + b[0] + " " + b[1]);
			System.out.println("m' is" + result[0]);
			steps = result[1].longValue();
		} else {
			throw new RuntimeException("m " + m + " does not match m' " + result[0]);
		}
		return steps;
	}
	
	/* Calculates the average number of steps to find zk=z2k */

	long avgsteps(long N,BigInteger[] a, BigInteger d, BigInteger p,BigInteger n) {
		long totalK = 0;
		for (int i = 1; i <= N; i++) {
			// System.out.println("Running step # " + i + "...");
			long currK = check(a, d, p, n);
			// System.out.println("Value of k is " + currK);
			totalK += currK;
		}
		return totalK / N;
	}


}
