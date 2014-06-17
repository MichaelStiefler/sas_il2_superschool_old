package com.maddox.sas1946.il2.util;

import java.security.SecureRandom;
import java.util.Random;

import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;

/**
 * "TrueRandom" Class of the "SAS Common Utils"
 * <p>
 * This Class is used to provide "true" randomized numbers for IL-2 Sturmovik 1946, in opposite to the reproduceable "pseudo" random numbers being available in the stock game.
 * <p>
 * 
 * @version 1.0.4
 * @since 1.0.3
 * @author SAS~Storebror
 */
public class TrueRandom {
	/**
	 * Override default Constructor to avoid instanciation
	 * 
	 * @throws Exception
     * @since 1.0.4
	 */
	private TrueRandom() throws Exception {
		throw new Exception("Class com.maddox.sas1946.il2.util.TrueRandom cannot be instanciated!");
	}

	// *****************************************************************************************************************************************************************************************************
	// Public interface section.
	// Methods and Arguments are supposed to be final here.
	// Take care of encapsulation and don't modify methods or arguments declared on this interface
	// to ensure future backward compatibility
	// If you need a new method with the same name but different parameters or return types,
	// simply overload the given methods in this interface.
	// *****************************************************************************************************************************************************************************************************

	/**
	 * This method can turn the "pseudo" random behaviour of IL-2 into "real" random behaviour
	 * with a single line of code.<br>
	 * Just call this method anywhere in your code and from the moment when this line is
	 * processed, all random actions in IL-2 will be really randomized.
     * @since 1.0.4
	 */
	public final static void makeIl2TrueRandom() {
		long lTime = System.currentTimeMillis();
		SecureRandom secRandom = new SecureRandom();
		secRandom.setSeed(lTime);
		int saveCountAccess = World.cur().rnd.countAccess();
		World.cur().rnd = new RangeRandom(secRandom.nextLong());
		Reflection.setInt(World.cur().rnd, "countAccess", saveCountAccess);
		saveCountAccess = World.cur().rnd2.countAccess();
		World.cur().rnd2 = new RangeRandom(secRandom.nextLong());
		Reflection.setInt(World.cur().rnd2, "countAccess", saveCountAccess);
	}

	/**
	 * Returns the next pseudorandom, uniformly distributed int value from this random number generator's sequence.
	 * The general contract of nextRandomInt is that one int value is pseudorandomly generated and returned.
	 * All 2<small><sup>32</sup></small> possible int values are produced with (approximately) equal probability.<br>
	 * 
	 * @return the next pseudorandom, uniformly distributed int value from this random number generator's sequence.
     * @since 1.0.3
	 */
	public final static int nextRandomInt() {
		if (theRandom == null) initRandom();
		return theRandom.nextInt();
	}

	/**
	 * Returns a pseudorandom, uniformly distributed int value between 0 (inclusive) and the specified value (exclusive), drawn from this random number generator's sequence.
	 * The general contract of nextRandomInt is that one int value in the specified range is pseudorandomly generated and returned.
	 * All max possible int values are produced with (approximately) equal probability.
	 * 
	 * @param max
	 *            the bound on the random number to be returned. Must be positive.
	 * @throws
	 *        IllegalArgumentException if max is not positive.
	 * @return a pseudorandom, uniformly distributed int value between 0 (inclusive) and max (exclusive).
     * @since 1.0.3
	 */
	public final static int nextRandomInt(int max) {
		if (theRandom == null) initRandom();
		return theRandom.nextInt(max);
	}

	/**
	 * Returns a pseudorandom, uniformly distributed int value between min (inclusive) and max (exclusive), drawn from this random number generator's sequence.
	 * The general contract of nextRandomInt is that one int value in the specified range is pseudorandomly generated and returned.
	 * All max-min possible int values are produced with (approximately) equal probability.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned. Must be larger than min.
	 * @throws
	 *        IllegalArgumentException if max-min is not positive.
	 * @return a pseudorandom, uniformly distributed int value between min (inclusive) and max (exclusive).
     * @since 1.0.3
	 */
	public final static int nextRandomInt(int min, int max) {
		if (theRandom == null) initRandom();
		return theRandom.nextInt(max - min) + min;
	}

	/**
	 * Returns the next pseudorandom, uniformly distributed long value from this random number generator's sequence.
	 * The general contract of nextRandomLong is that one long value is pseudorandomly generated and returned.
	 * All 2<small><sup>64</sup></small> possible long values are produced with (approximately) equal probability.<br>
	 * 
	 * @return the next pseudorandom, uniformly distributed long value from this random number generator's sequence.
     * @since 1.0.3
	 */
	public final static long nextRandomLong() {
		if (theRandom == null) initRandom();
		return theRandom.nextLong();
	}

	/**
	 * Returns a pseudorandom, uniformly distributed long value between 0 (inclusive) and the specified value (exclusive), drawn from this random number generator's sequence.
	 * The general contract of nextRandomLong is that one long value in the specified range is pseudorandomly generated and returned.
	 * All max possible long values are produced with (approximately) equal probability.
	 * 
	 * @param max
	 *            the bound on the random number to be returned. Must be positive.
	 * @throws
	 *        IllegalArgumentException if max is not positive.
	 * @return a pseudorandom, uniformly distributed long value between 0 (inclusive) and max (exclusive).
     * @since 1.0.3
	 */
	public final static long nextRandomLong(long max) {
		if (theRandom == null) initRandom();
		return theRandom.nextLong() % max;
	}

	/**
	 * Returns a pseudorandom, uniformly distributed long value between min (inclusive) and max (exclusive), drawn from this random number generator's sequence.
	 * The general contract of nextRandomLong is that one long value in the specified range is pseudorandomly generated and returned.
	 * All max-min possible long values are produced with (approximately) equal probability.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned. Must be larger than min.
	 * @throws
	 *        IllegalArgumentException if max-min is not positive.
	 * @return a pseudorandom, uniformly distributed long value between min (inclusive) and max (exclusive).
     * @since 1.0.3
	 */
	public final static long nextRandomLong(long min, long max) {
		if (theRandom == null) initRandom();
		return (theRandom.nextLong() % (max - min)) + min;
	}

	/**
	 * Returns the next pseudorandom, uniformly distributed float value between 0.0 and 1.0 from this random number generator's sequence.
	 * The general contract of nextRandomFloat is that one float value, chosen (approximately) uniformly from the range 0.0f (inclusive) to 1.0f (exclusive), is pseudorandomly generated and returned.
	 * All 2<small><sup>24</sup></small> possible float values of the form m x 2<small><sup>-24</sup></small>, where m is a positive integer less than 2<small><sup>24</sup></small>, are produced with (approximately) equal
	 * probability.
	 * 
	 * @return the next pseudorandom, uniformly distributed float value between 0.0 and 1.0 from this random number generator's sequence.
     * @since 1.0.3
	 */
	public final static float nextRandomFloat() {
		if (theRandom == null) initRandom();
		return theRandom.nextFloat();
	}

	/**
	 * Returns a pseudorandom, uniformly distributed float value between 0 (inclusive) and the specified value (exclusive), drawn from this random number generator's sequence.
	 * The general contract of nextRandomFloat is that one float value in the specified range is pseudorandomly generated and returned.
	 * All 2<small><sup>24</sup></small> possible float values between 0.0 and max are produced with (approximately) equal probability.
	 * 
	 * @param max
	 *            the bound on the random number to be returned. Can be positive or negative.
	 * @return a pseudorandom, uniformly distributed float value between 0 (inclusive) and max (exclusive).
     * @since 1.0.3
	 */
	public final static float nextRandomFloat(float max) {
		if (theRandom == null) initRandom();
		return theRandom.nextFloat() * max;
	}

	/**
	 * Returns a pseudorandom, uniformly distributed float value between min (inclusive) and max (exclusive), drawn from this random number generator's sequence.
	 * The general contract of nextRandomFloat is that one float value in the specified range is pseudorandomly generated and returned.
	 * All 2<small><sup>24</sup></small> possible float values between min and max are produced with (approximately) equal probability.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return a pseudorandom, uniformly distributed float value between min (inclusive) and max (exclusive).
     * @since 1.0.3
	 */
	public final static float nextRandomFloat(float min, float max) {
		if (theRandom == null) initRandom();
		return theRandom.nextFloat() * (max - min) + min;
	}

	/**
	 * Returns the next pseudorandom, uniformly distributed double value between 0.0 and 1.0 from this random number generator's sequence.
	 * The general contract of nextRandomDouble is that one double value, chosen (approximately) uniformly from the range 0.0d (inclusive) to 1.0d (exclusive), is pseudorandomly generated and returned.
	 * All 2<small><sup>53</sup></small> possible double values of the form m x 2<small><sup>-53</sup></small>, where m is a positive integer less than 2<small><sup>53</sup></small>, are produced with (approximately) equal
	 * probability.
	 * 
	 * @return the next pseudorandom, uniformly distributed double value between 0.0 and 1.0 from this random number generator's sequence.
     * @since 1.0.3
	 */
	public final static double nextRandomDouble() {
		if (theRandom == null) initRandom();
		return theRandom.nextDouble();
	}

	/**
	 * Returns a pseudorandom, uniformly distributed double value between 0 (inclusive) and the specified value (exclusive), drawn from this random number generator's sequence.
	 * The general contract of nextRandomDouble is that one double value in the specified range is pseudorandomly generated and returned.
	 * All 2<small><sup>53</sup></small> possible double values between 0.0 and max are produced with (approximately) equal probability.
	 * 
	 * @param max
	 *            the bound on the random number to be returned. Can be positive or negative.
	 * @return a pseudorandom, uniformly distributed double value between 0 (inclusive) and max (exclusive).
     * @since 1.0.3
	 */
	public final static double nextRandomDouble(double max) {
		if (theRandom == null) initRandom();
		return theRandom.nextDouble() * max;
	}

	/**
	 * Returns a pseudorandom, uniformly distributed double value between min (inclusive) and max (exclusive), drawn from this random number generator's sequence.
	 * The general contract of nextRandomDouble is that one double value in the specified range is pseudorandomly generated and returned.
	 * All 2<small><sup>53</sup></small> possible double values between min and max are produced with (approximately) equal probability.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return a pseudorandom, uniformly distributed double value between min (inclusive) and max (exclusive).
     * @since 1.0.3
	 */
	public final static double nextRandomDouble(double min, double max) {
		if (theRandom == null) initRandom();
		return theRandom.nextDouble() * (max - min) + min;
	}

	/**
	 * Returns the next pseudorandom, Gaussian ("normally") distributed double value with mean 0.0 and standard deviation 1.0 from this random number generator's sequence.
	 * The general contract of nextRandomGaussian is that one double value, chosen from (approximately) the usual normal distribution with mean 0.0 and standard deviation 1.0, is pseudorandomly generated and returned.
	 * 
	 * @return the next pseudorandom, Gaussian ("normally") distributed double value with mean 0.0 and standard deviation 1.0 from this random number generator's sequence.
     * @since 1.0.3
	 */
	public final static double nextRandomGaussian() {
		if (theRandom == null) initRandom();
		return theRandom.nextGaussian();
	}

	/**
	 * Returns the next pseudorandom, Gaussian ("normally") distributed double value with mean 0.0 and the specified deviation from this random number generator's sequence.
	 * The general contract of nextRandomGaussian is that one double value, chosen from (approximately) the usual normal distribution with mean 0.0 and the specified deviation, is pseudorandomly generated and returned.
	 * 
	 * @param deviation
	 *            the deviation for this Gaussian Distribution
	 * @return the next pseudorandom, Gaussian ("normally") distributed double value with mean 0.0 and the specified deviation from this random number generator's sequence.
     * @since 1.0.3
	 */
	public final static double nextRandomGaussian(double deviation) {
		if (theRandom == null) initRandom();
		return theRandom.nextGaussian() * deviation;
	}

	/**
	 * Returns the next pseudorandom, Gaussian ("normally") distributed double value with the specified mean and deviation from this random number generator's sequence.
	 * The general contract of nextRandomGaussian is that one double value, chosen from (approximately) the usual normal distribution with the specified mean and deviation, is pseudorandomly generated and returned.
	 * 
	 * @param deviation
	 *            the deviation for this Gaussian Distribution
	 * @param center
	 *            the mean for this Gaussian Distribution
	 * @return the next pseudorandom, Gaussian ("normally") distributed double value with the specified mean and deviation from this random number generator's sequence.
     * @since 1.0.3
	 */
	public final static double nextRandomGaussian(double deviation, double center) {
		if (theRandom == null) initRandom();
		return theRandom.nextGaussian() * deviation + center;
	}

	/**
	 * Returns the next pseudorandom, "Dome" type distributed double value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextRandomDouble_Dome is that one double value, chosen from (approximately) a "Dome" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Dome" type distributed double value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static double nextRandomDouble_Dome(double min, double max) {
		if (theRandom == null) initRandom();
		double d1 = nextRandomDouble();
		double d2 = nextRandomDouble();
		double d3 = nextRandomDouble();
		double d4 = (d1 + d2 + d3) / 3D;
		return min + (max - min) * d4;
	}

	/**
	 * Returns the next pseudorandom, "Dome" type distributed float value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextRandomFloat_Dome is that one float value, chosen from (approximately) a "Dome" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Dome" type distributed float value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextRandomFloat_Dome(float min, float max) {
		return (float) nextRandomDouble_Dome(min, max);
	}

	/**
	 * Returns the next pseudorandom, "Dome" type distributed int value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextRandomInt_Dome is that one int value, chosen from (approximately) a "Dome" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Dome" type distributed int value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextRandomInt_Dome(int min, int max) {
		return (int) nextRandomDouble_Dome(min, max);
	}

	/**
	 * Returns the next pseudorandom, "Dome" type distributed long value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextRandomLong_Dome is that one long value, chosen from (approximately) a "Dome" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Dome" type distributed long value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextRandomLong_Dome(long min, long max) {
		return (long) nextRandomDouble_Dome(min, max);
	}

	/**
	 * Returns the next pseudorandom, "Inverted Dome" type distributed double value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextRandomDouble_DomeInv is that one double value, chosen from (approximately) a "Inverted Dome" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Inverted Dome" type distributed double value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static double nextRandomDouble_DomeInv(double min, double max) {
		if (theRandom == null) initRandom();
		double d1 = nextRandomDouble();
		double d2 = nextRandomDouble();
		double d3 = nextRandomDouble();
		double d4 = (d1 + d2 + d3) / 3D;
		if (d4 >= 0.5D) d4 -= 0.5D;
		else d4 += 0.5D;
		return min + (max - min) * d4;
	}

	/**
	 * Returns the next pseudorandom, "Inverted Dome" type distributed float value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextRandomFloat_DomeInv is that one float value, chosen from (approximately) a "Inverted Dome" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Inverted Dome" type distributed float value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextRandomFloat_DomeInv(float min, float max) {
		return (float) nextRandomDouble_DomeInv(min, max);
	}

	/**
	 * Returns the next pseudorandom, "Inverted Dome" type distributed int value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextRandomInt_DomeInv is that one int value, chosen from (approximately) a "Inverted Dome" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Inverted Dome" type distributed int value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextRandomInt_DomeInv(int min, int max) {
		return (int) nextRandomDouble_DomeInv(min, max);
	}

	/**
	 * Returns the next pseudorandom, "Inverted Dome" type distributed long value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextRandomLong_DomeInv is that one long value, chosen from (approximately) a "Inverted Dome" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Inverted Dome" type distributed long value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextRandomLong_DomeInv(long min, long max) {
		return (long) nextRandomDouble_DomeInv(min, max);
	}

	/**
	 * Returns the next pseudorandom, "Fade" type distributed double value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextRandomDouble_Fade is that one double value, chosen from (approximately) a "Fade" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Fade" type distributed double value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static double nextRandomDouble_Fade(double min, double max) {
		if (theRandom == null) initRandom();
		double d1 = nextRandomDouble();
		double d2 = nextRandomDouble();
		double d3 = nextRandomDouble();
		double d4 = (d1 + d2 + d3) / 3D;
		if (d4 >= 0.5D) d4 -= 0.5D;
		else d4 += 0.5D;
		return min + (max - min) * d4;
	}

	/**
	 * Returns the next pseudorandom, "Fade" type distributed float value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextRandomFloat_Fade is that one float value, chosen from (approximately) a "Fade" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Fade" type distributed float value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextRandomFloat_Fade(float min, float max) {
		return (float) nextRandomDouble_Fade(min, max);
	}

	/**
	 * Returns the next pseudorandom, "Fade" type distributed int value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextRandomInt_Fade is that one int value, chosen from (approximately) a "Fade" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Fade" type distributed int value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextRandomInt_Fade(int min, int max) {
		return (int) nextRandomDouble_Fade(min, max);
	}

	/**
	 * Returns the next pseudorandom, "Fade" type distributed long value between the specified min and max values from this random number generator's sequence.
	 * The general contract of nextRandomLong_Fade is that one long value, chosen from (approximately) a "Fade" type distribution between the specified min and max values, is pseudorandomly generated and returned.
	 * 
	 * @param min
	 *            the lower bound on the random number to be returned.
	 * @param max
	 *            the upper bound on the random number to be returned.
	 * @return the next pseudorandom, "Fade" type distributed long value between the specified min and max values from this random number generator's sequence.
     * @since 1.0.4
	 */
	public final static float nextRandomLong_Fade(long min, long max) {
		return (long) nextRandomDouble_Fade(min, max);
	}
	
	// *****************************************************************************************************************************************************************************************************
	// Private implementation section.
	// Do whatever you like here but keep it private to this class.
	// *****************************************************************************************************************************************************************************************************

	private static Random theRandom = null;

	private static void initRandom() {
		long lTime = System.currentTimeMillis();
		SecureRandom secRandom = new SecureRandom();
		secRandom.setSeed(lTime);
		theRandom = new Random(secRandom.nextLong());
	}

}
