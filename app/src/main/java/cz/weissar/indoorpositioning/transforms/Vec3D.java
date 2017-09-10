package cz.weissar.indoorpositioning.transforms;

import java.util.Locale;

/**
 * 3D vector over real numbers (final double-precision), equivalent to 3D affine
 * point, immutable
 * 
 * @author PGRF FIM UHK 
 * @version 2016
 */

public class Vec3D {
	private final double x, y, z;

	/**
	 * Creates a zero vector
	 */
	public Vec3D() {
		x = y = z = 0.0f;
	}

	/**
	 * Creates a vector with the given coordinates
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 */
	public Vec3D(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Creates a vector by cloning the give one
	 * 
	 * @param v
	 *            vector to be cloned
	 */
	public Vec3D(final Vec3D v) {
		x = v.x;
		y = v.y;
		z = v.z;
	}

	/**
	 * Creates a vector by ignoring the fourth homogeneous coordinate w of the
	 * given homogeneous 3D point
	 * 
	 * @param point
	 *            homogeneous 3D point whose x,y,z will be cloned
	 */
	public Vec3D(Point3D point) {
		x = point.getX();
		y = point.getY();
		z = point.getZ();
	}
	
	/**
	 * Creates a vector by extracting coordinates from the given array of
	 * doubles
	 * 
	 * @param array
	 *            double array of size 3 (asserted)
	 */
	public Vec3D(final double[] array) {
		assert(array.length >= 3);
		x = array[0];
		y = array[1];
		z = array[2];
	}

	public float[] toFloatArray(){
		return new float[]{(float)x, (float)y, (float)z};
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
	 * Returns the z coordinate
	 * 
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Returns 2D vector with this x and y coordinates, i.e. an orthogonal
	 * projection of this as an affine point into the xy plane
	 * 
	 * @return new Vec2D instance
	 */
	public Vec2D ignoreZ() {
		return new Vec2D(x, y);
	}

	/**
	 * Returns the result of vector addition of the given vector
	 * 
	 * @param v
	 *            vector to add
	 * @return new Vec2D instance
	 */
	public Vec3D add(final Vec3D v) {
		return new Vec3D(x + v.x, y + v.y, z + v.z);
	}

	/**
	 * Returns the result of vector subtraction of the given vector
	 * 
	 * @param v
	 *            vector to subtract
	 * @return new Vec2D instance
	 */
	public Vec3D sub(final Vec3D v) {
		return new Vec3D(x - v.x, y - v.y, z - v.z);
	}
	
	/**
	 * Returns the result of scalar multiplication
	 * 
	 * @param d
	 *            scalar value of type double
	 * @return new Vec2D instance
	 */
	public Vec3D mul(final double d) {
		return new Vec3D(x * d, y * d, z * d);
	}

	/**
	 * Returns the result of multiplication by the given 3x3 matrix thus
	 * applying the transformation contained within
	 * 
	 * @param m
	 *            3x3 matrix
	 * @return new Vec3D instance
	 */
	public Vec3D mul(final Mat3 m) {
		return new Vec3D(
			m.mat[0][0] * x + m.mat[1][0] * y + m.mat[2][0] * z,
			m.mat[0][1] * x + m.mat[1][1] * y + m.mat[2][1] * z,
			m.mat[0][2] * x + m.mat[1][2] * y + m.mat[2][2] * z);
	}

	/**
	 * Returns the result of applying the given quaternion to this vector
	 * 
	 * @param q
	 *            quaternion
	 * @return new Vec3D instance
	 */
	public Vec3D mul(final Quat q) {
		final Quat p = q.mulR(new Quat(0, x, y, z)).mulR(q.inv());
		return new Vec3D(p.i, p.j, p.k);
	}

	/**
	 * Returns the result of element-wise multiplication with the given vector
	 * 
	 * @param v
	 *            3D vector 
	 * @return new Vec2D instance
	 */
	public Vec3D mul(final Vec3D v) {
		return new Vec3D(x * v.x, y * v.y, z * v.z);
	}

	/**
	 * Returns the result of dot-product with the given vector
	 * 
	 * @param v
	 *            3D vector 
	 * @return double-precision floating point value
	 */
	public double dot(final Vec3D v) {
		return x * v.x + y * v.y + z * v.z;
	}

	/**
	 * Returns the result of cross-product with the given vector, i.e. a vector
	 * perpendicular to both this and the given vector, the direction is
	 * right-handed
	 * 
	 * @param v
	 *            3D vector
	 * @return new Vec3D instance
	 */
	public Vec3D cross(final Vec3D v) {
		return new Vec3D(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y
				* v.x);
	}

	/**
	 * Returns a collinear unit vector (by dividing all vector components by
	 * vector length) if possible (nonzero length), empty Optional otherwise
	 * 
	 * @return new Optional<Vec3D> instance
	 */
	/*public Optional<Vec3D> normalized() {
		final double len = length();
		if (len == 0.0)
			return Optional.empty();
		return Optional.of(new Vec3D(x / len, y / len, z / len));
	}*/

	/**
	 * Returns the vector opposite to this vector
	 * 
	 * @return new Vec2D instance
	 */
	public Vec3D opposite() {
		return new Vec3D(-x, -y, -z);
	}

	/**
	 * Returns the length of this vector
	 * 
	 * @return double-precision floating point value
	 */
	public double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	/**
	 * Returns String representation of this vector
	 * 
	 * @return comma separated floating-point values in curly brackets
	 */
	public String toString() {
		return String.format(Locale.US, "{%4.1f,%4.1f,%4.1f}", x, y, z);
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
		return String.format(Locale.US, "{" + format + "," + format + "," + format + "}",
				x, y, z);
	}
}