package cz.weissar.indoorpositioning.transforms;

import java.util.Locale;

/**
 * 2D vector over real numbers (final double-precision), equivalent to 2D affine
 * point, immutable
 * 
 * @author PGRF FIM UHK
 * @version 2016
 */


public class Vec2D {
	private final double x, y;

	/**
	 * Creates a zero vector
	 */
	public Vec2D() {
		x = y = 0.0;
	}

	/**
	 * Creates a vector with the given coordinates
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public Vec2D(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Creates a vector by cloning the give one
	 * 
	 * @param v
	 *            vector to be cloned
	 */
	public Vec2D(final Vec2D v) {
		x = v.x;
		y = v.y;
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
	 * Returns the y coordinate
	 * 
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Returns the result of vector addition of the given vector
	 * 
	 * @param v
	 *            vector to add
	 * @return new Vec2D instance
	 */
	public Vec2D add(final Vec2D v) {
		return new Vec2D(x + v.x, y + v.y);
	}


	/**
	 * Returns the result of vector subtraction of the given vector
	 * 
	 * @param v
	 *            vector to subtract
	 * @return new Vec2D instance
	 */
	public Vec2D sub(final Vec2D v) {
		return new Vec2D(x - v.x, y - v.y);
	}
	
	/**
	 * Returns the result of scalar multiplication
	 * 
	 * @param d
	 *            scalar value of type double
	 * @return new Vec2D instance
	 */
	public Vec2D mul(final double d) {
		return new Vec2D(x * d, y * d);
	}

	/**
	 * Returns the result of element-wise multiplication with the given vector
	 * 
	 * @param v
	 *            2D vector 
	 * @return new Vec2D instance
	 */
	public Vec2D mul(final Vec2D v) {
		return new Vec2D(x * v.x, y * v.y);
	}

	/**
	 * Returns the result of dot-product with the given vector
	 * 
	 * @param v
	 *            2D vector 
	 * @return double-precision floating point value
	 */
	public double dot(final Vec2D v) {
		return x * v.x + y * v.y;
	}

	/**
	 * Returns a collinear unit vector (by dividing all vector components by
	 * vector length) if possible (nonzero length), empty Optional otherwise
	 * 
	 * @return new Optional<Vec2D> instance
	 */
	/*public Optional<Vec2D> normalized() {
		final double len = length();
		if (len == 0.0)
			return Optional.empty();
		return Optional.of(new Vec2D(x / len, y / len));
	}*/


	/**
	 * Returns the vector opposite to this vector
	 * 
	 * @return new Vec2D instance
	 */
	public Vec2D opposite() {
		return new Vec2D(-x, -y);
	}
	
	/**
	 * Returns the length of this vector
	 * 
	 * @return double-precision floating point value
	 */
	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * Returns String representation of this vector
	 * 
	 * @return comma separated floating-point values in curly brackets
	 */
	public String toString() {
		return String.format(Locale.US, "{%4.1f,%4.1f}", x, y);
	}

	/**
	 * Returns String representation of this vector with coordinates formated
	 * according to the given format, see
	 * {@link String#format(String, Object...)}
	 * 
	 * @param format
	 *            String format applied to each coordinate
	 * @return comma separated floating-point values in curly brackets
	 */
	public String toString(String format) {
		return String.format(Locale.US, "{" + format + "," + format + "}",	x, y);
	}
}
