package cz.weissar.indoorpositioning.transforms;

/**
 * A 4x4 matrix of right-handed rotation about z-axis
 * 
 * @author PGRF FIM UHK 
 * @version 2016
 */
public class Mat4RotZ extends Mat4Identity {

	/**
	 * Creates a 4x4 transformation matrix equivalent to right-handed rotation
	 * about z-axis
	 * 
	 * @param alpha
	 *            rotation angle in radians
	 */
	public Mat4RotZ(final double alpha) {
		mat[0][0] = (double) Math.cos(alpha);
		mat[1][1] = (double) Math.cos(alpha);
		mat[1][0] = (double) -Math.sin(alpha);
		mat[0][1] = (double) Math.sin(alpha);
	}
}