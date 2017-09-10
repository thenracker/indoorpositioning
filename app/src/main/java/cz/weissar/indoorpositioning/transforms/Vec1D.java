package cz.weissar.indoorpositioning.transforms;

import java.util.Locale;

/**
 * 1D vector over real numbers (final double-precision), equivalent to 1D affine
 * point, immutable
 * 
 * @author PGRF FIM UHK
 * @version 2016
 */

public class Vec1D {
	private final double x;

	/**
	 * Creates a zero vector
	 */
	public Vec1D() {
		x = 0.0;
	}

	/**
	 * Creates a vector with the given coordinate
	 * 
	 * @param x
	 *            x coordinate
	 */
	public Vec1D(final double x) {
		this.x = x;
	}

	/**
	 * Creates a vector by cloning the give one
	 * 
	 * @param v
	 *            vector to be cloned
	 */
	public Vec1D(final Vec1D v) {
		x = v.x;
	}
	
	

	/**
	 * Returns the x coordinate
	 * 
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the result of vector addition of the given vector
	 * 
	 * @param v
	 *            vector to add
	 * @return new Vec1D instance
	 */
	public Vec1D add(final Vec1D v) {
		return new Vec1D(x + v.x);
	}


	/**
	 * Returns the result of vector subtraction of the given vector
	 * 
	 * @param v
	 *            vector to subtract
	 * @return new Vec1D instance
	 */
	public Vec1D sub(final Vec1D v) {
		return new Vec1D(x - v.x);
	}
	
	/**
	 * Returns the result of scalar multiplication
	 * 
	 * @param d
	 *            scalar value of type double
	 * @return new Vec1D instance
	 */
	public Vec1D mul(final double d) {
		return new Vec1D(x * d);
	}

	/**
	 * Returns the vector opposite to this vector
	 * 
	 * @return new Vec1D instance
	 */
	public Vec1D opposite() {
		return new Vec1D(-x);
	}

	/**
	 * Returns String representation of this vector
	 * 
	 * @return floating-point value in parentheses
	 */
	@Override
	public String toString() {
		return String.format(Locale.US, "{%4.1f}",x);
	}
	
	/**
	 * Returns String representation of this vector with coordinate formated
	 * according to the given format, see
	 * {@link String#format(String, Object...)}
	 * 
	 * @param format
	 *            String format applied to the coordinate
	 * @return floating-point value in curly brackets
	 */
	public String toString(String format) {
		return String.format(Locale.US, "{"+format+"}",x);
	}
}
