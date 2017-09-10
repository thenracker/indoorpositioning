package cz.weissar.indoorpositioning.transforms;

import java.util.Locale;
import java.util.Optional;

/**
 * 2D point with homogeneous coordinates, immutable
 *  
 * @author PGRF FIM UHK 
 * @version 2016
 */

public class Point2D {
	private final double x, y, w;

	/**
	 * Creates a homogeneous point representing the origin 
	 */
	public Point2D() {
		x = y = 0.0;
		w = 1.0;
	}

	/**
	 * Creates a homogeneous point representing a 2D point with the two given
	 * affine coordinates
	 * 
	 * @param x
	 *            affine x coordinate
	 * @param y
	 *            affine y coordinate
	 */
	public Point2D(final double x, final double y) {
		this.x = x;
		this.y = y;
		this.w = 1.0;
	}

	/**
	 * Creates a point with the given homogeneous coordinates
	 * 
	 * @param x
	 *            homogeneous x coordinate
	 * @param y
	 *            homogeneous y coordinate
	 * @param w
	 *            homogeneous w coordinate
	 */
	public Point2D(final double x, final double y, final double w) {
		this.x = x;
		this.y = y;
		this.w = w;
	}

	/**
	 * Creates a homogeneous point representing a 2D affine point defined by the
	 * given vector from origin
	 * 
	 * @param v
	 *            affine coordinates vector (vector from origin to the point)
	 */
	public Point2D(final Vec2D v) {
		this.x = v.getX();
		this.y = v.getY();
		this.w = 1.0;
	}

	/**
	 * Creates a point by cloning the give one
	 * 
	 * @param p
	 *            homogeneous point to be cloned
	 */
	public Point2D(final Point2D p) {
		this.x = p.getX();
		this.y = p.getY();
		this.w = p.getW();
	}

	/**
	 * Creates a point by ignoring the z coordinate of the given 3D homogeneous
	 * point
	 * 
	 * @param p
	 *            homogeneous 3D point whose x,y,w will be cloned
	 */
	public Point2D(final Point3D p) {
		this.x = p.getX();
		this.y = p.getY();
		this.w = p.getW();
	}

	/**
	 * Creates a point by extracting homogeneous coordinates from the given
	 * array of doubles
	 * 
	 * @param array
	 *            double array of size 3 (asserted)
	 */
	public Point2D(final double[] array) {
		assert(array.length >= 3);
		x = array[0];
		y = array[1];
		w = array[3];
	}
	

	/**
	 * Returns the homogeneous x coordinate
	 * 
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the homogeneous y coordinate
	 * 
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Returns the homogeneous w coordinate
	 * 
	 * @return the w
	 */
	public double getW() {
		return w;
	}

	/**
	 * Returns the result of multiplication by the given 3x3 matrix thus
	 * applying the transformation contained within
	 * 
	 * @param mat
	 *            3x3 matrix
	 * @return new Point2D instance
	 */
	public Point2D mul(final Mat3 mat) {
		return new Point2D(
			mat.mat[0][0] * x + mat.mat[1][0] * y + mat.mat[2][0] * w,
			mat.mat[0][1] * x + mat.mat[1][1] * y + mat.mat[2][1] * w,
			mat.mat[0][2] * x + mat.mat[1][2] * y + mat.mat[2][2] * w);
	}
	
	/**
	 * Returns the result of element-wise summation with the given homogeneous
	 * 2D point
	 * 
	 * @param p
	 *            homogeneous 2D point to sum
	 * @return new Point2D instance
	 */
	public Point2D add(final Point2D p) {
		return new Point2D(x + p.x, y + p.y, w + p.w);
	}

	/**
	 * Returns the result of element-wise summation with the given 2D vector
	 * keeping the third homogeneous coordinate w unchanged
	 * 
	 * @param v
	 *            2D vector to sum
	 * @return new Point2D instance
	 */
	public Point2D add(final Vec2D v) {
		return new Point2D(x + v.getX(), y + v.getY(), w);
	}

	/**
	 * Returns the result of element-wise multiplication by the given scalar value
	 * 
	 * @param d
	 *            scalar value of type double
	 * @return new Point2D instance
	 */
	public Point2D mul(final double d) {
		return new Point2D(x * d, y * d, w * d);
	}

	/**
	 * Returns the result of point dehomogenization, i.e. the affine point
	 * coordinates = x,y divided by w, in the form of a vector from origin to
	 * the affine point if possible (the point is not in infinity), empty
	 * Optional otherwise
	 * 
	 * @return new Optional<Vec2D> instance
	 */
	/*public Optional<Vec2D> dehomog() {
		if (w == 0.0)
			return Optional.empty();
		return Optional.of(new Vec2D(x / w, y / w));
	}*/

	/**
	 * Converts the homogeneous 2D point to 2D vector by ignoring the third
	 * homogeneous coordinate w
	 * 
	 * @return new Vec2D instance
	 */
	public Vec2D ignoreW() {
		return new Vec2D(x, y);
	}
	
	/**
	 * Returns String representation of this point
	 * 
	 * @return comma separated floating-point values in parentheses
	 */
	@Override
	public String toString() {
		return String.format(Locale.US, "(%4.1f,%4.1f,%4.1f)", x, y, w);
	}
	
	/**
	 * Returns String representation of this point with coordinates formated
	 * according to the given format, see
	 * {@link String#format(String, Object...)}
	 * 
	 * @param format
	 *            String format applied to each coordinate
	 * @return comma separated floating-point values in parentheses
	 */
	public String toString(final String format) {
		return String.format(Locale.US, "(" + format + "," + format + "," + format + ")", x, y, w);
	}
}