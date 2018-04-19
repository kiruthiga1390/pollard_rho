# Pollard-rho
Elliptic curve cryptography is quickly becoming a technological reality. They are faster, compact, more secure alternative to RSA cryptography.
Given 𝑎, 𝑏 ∈ 𝔾 (Graph), the best algorithms known for computing 𝑚 such that 𝑏 = a power m take exponential time, Θ (2 power 𝑘/2) group operations, depending
on the nature of 𝔾. This is called the discrete logarithm problem (DLP).
Pollard-rho is one of the best algorithm for DLP.

## Algorithm
Pollard’s rho algorithm recovers the discrete logarithm 𝑚 from two given curve point 𝑎
and 𝑏 such that 𝑏=𝑎 power 𝑚. It maintains a sextuple of variables (𝛼𝑘,𝛽𝑘,𝑧𝑘,𝛼2𝑘,𝛽2𝑘,𝑧2𝑘) for 
𝑘=0,1,2,… until it finds a sextuple where 𝑧𝑘=𝑧2𝑘 (called collision).

## Files
1.Pollard_Rho : consists of main method which calls the multiplication, exponentiation
and calculates sextuples (alphak, betak, zk, alpha2k, beta2k, z2k) till zk=z2k and returns
the average number of steps took to calculate

## How To execute
Run the Pollard_Rho.java 