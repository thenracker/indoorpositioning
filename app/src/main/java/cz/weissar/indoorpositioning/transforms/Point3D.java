package cz.weissar.indoorpositioning.transforms;

import java.util.Locale;

/**
 * 3D point with homogeneous coordinates, immutable
 *  
 * @author PGRF FIM UHK 
 * @version 2014
 */

public class Point3D {
	private final double x, y, z, w;

	/**
	 * Creates a homogeneous point representing the origin 
	 */
	public Point3D() {
		x = y = z = 0.0;
		w = 1.0;
	}

	/**
	 * Creates a homogeneous point representing a 3D point with the three given
	 * affine coordinates
	 * 
	 * @param x
	 *            affine x coordinate
	 * @param y
	 *            affine y coordinate
	 * @param z
	 *            affine z coordinate
	 */
	public Point3D(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1.0;
	}

	/**
	 * Creates a point with the given homogeneous coordinates
	 * 
	 * @param x
	 *            homogeneous x coordinate
	 * @param y
	 *            homogeneous y coordinate
	 * @param z
	 *            homogeneous z coordinate
	 * @param w
	 *            homogeneous w coordinate
	 */
	public Point3D(final double x, final double y, final double z, final double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * Creates a homogeneous point representing a 3D affine point defined by the
	 * given vector from origin
	 * 
	 * @param v
	 *            affine coordinates vector (vector from origin to the point)
	 */
	public Point3D(final Vec3D v) {
		this.x = v.getX();
		this.y = v.getY();
		this.z = v.getZ();
		this.w = 1.0;
	}

	/**
	 * Creates a point by cloning the give one
	 * 
	 * @param p
	 *            homogeneous point to be cloned
	 */
	public Point3D(final Point3D p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
		this.w = p.w;
	}

	/**
	 * Creates a point by attaching the given z coordinate to the given 2D
	 * homogeneous point coordinates
	 * 
	 * @param p
	 *            homogeneous 2D point whose x,y,w will be cloned
	 * @param z
	 *            z coordinate to be attached
	 */
	public Point3D(final Point2D p, final double z) {
		x = p.getX();
		y = p.getY();
		this.z = z;
		w = p.getW();
	}

	/**
	 * Creates a point by extracting homogeneous coordinates from the given
	 * array of doubles
	 * 
	 * @param array
	 *            double array of size 4 (asserted)
	 */
	public Point3D(final double[] array) {
		assert(array.length >= 4);
		x = array[0];
		y = array[1];
		z = array[2];
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
	 * Returns the homogeneous z coordinate
	 * 
	 * @return the z
	 */
	public double getZ() {
		return z;
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
	 * Returns the result of multiplication by the given 4x4 matrix thus
	 * applying the transformation contained within
	 * 
	 * @param mat
	 *            4x4 matrix
	 * @return new Point3D instance
	 */
	public Point3D mul(final Mat4 mat) {
		return new Point3D(
			mat.mat[0][0] * x + mat.mat[1][0] * y + mat.mat[2][0] * z
				+ mat.mat[3][0] * w,
			mat.mat[0][1] * x + mat.mat[1][1] * y + mat.mat[2][1] * z
				+ mat.mat[3][1] * w,
			mat.mat[0][2] * x + mat.mat[1][2] * y + mat.mat[2][2] * z
				+ mat.mat[3][2] * w,
			mat.mat[0][3] * x + mat.mat[1][3] * y + mat.mat[2][3] * z
				+ mat.mat[3][3] * w);
	}
	
	/**
	 * Returns the result of applying the given quaternion to the affine 3D
	 * point defined by this point if possible (the point is not in infinity),
	 * empty Optional otherwise
	 * 
	 * @param q
	 *            quaternion
	 * @return new Optional<Point3D> instance
	 */
	/*public Optional<Point3D> mul(final Quat q) {
		return dehomog().map(affinePoint ->  new Point3D(affinePoint.mul(q)));
	}*/

	/**
	 * Returns the result of element-wise summation with the given homogeneous
	 * 3D point
	 * 
	 * @param p
	 *            homogeneous 3D point to sum
	 * @return new Point3D instance
	 */
	public Point3D add(final Point3D p) {
		return new Point3D(x + p.x, y + p.y, z + p.z, w + p.w);
	}

	/**
	 * Returns the result of element-wise multiplication by the given scalar value
	 * 
	 * @param d
	 *            scalar value of type double
	 * @return new Point3D instance
	 */
	public Point3D mul(final double d) {
		return new Point3D(x * d, y * d, z * d, w * d);
	}
	
	/**
	 * Returns the result of point dehomogenization, i.e. the affine point
	 * coordinates = x,y,z divided by w, in the form of a vector from origin to
	 * the affine point if possible (the point is not in infinity), empty
	 * Optional otherwise
	 * 
	 * @return new Optional<Vec3D> instance
	 */
	/*public Optional<Vec3D> dehomog() {
		if (w == 0.0)
			return Optional.empty();
		return Optional.of(new Vec3D(x / w, y / w, z / w));
	}*/

	/**
	 * Converts the homogeneous 3D point to 3D vector by ignoring the fourth
	 * homogeneous coordinate w
	 * 
	 * @return new Vec3D instance
	 */
	public Vec3D ignoreW() {
		return new Vec3D(x, y, z);
	}
	
	/**
	 * Returns String representation of this point
	 * 
	 * @return comma separated floating-point values in parentheses
	 */
	@Override
	public String toString() {
		return String.format(Locale.US, "(%4.1f,%4.1f,%4.1f,%4.1f)", x, y, z, w);
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
		return String.format(Locale.US, "(" + format + "," + format + "," + format + "," + format + ")", x, y, z, w);
	}
}