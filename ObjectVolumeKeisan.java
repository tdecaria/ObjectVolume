import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ObjectVolume {
	// ObjectVolume:
	// Reads in an object file (.obj) and calcultates the Volume,
	// Surface Area and Centroid of the object described in the file.

	public static void main(String[] args) {

		double totalVolume = 0;
		double totalArea = 0;

		// Stores the vertices for the object. This value needs to be changed if
		// you are working with more than 50.000 points.
		double[][] V = new double[50000][3];

		// Each face can contain up to 100 points.
		int[] faceValues = new int[100];
		int[] faces = new int[50];

		double[] result = { 0, 0, 0, 0, 0 };
		// Global Center of Volume accumulators
		double cx = 0;
		double cy = 0;
		double cz = 0;

		int vertexCount = 0;
		int faceCount = 0;
		int t1 = 0;
		int t2 = 1;
		int t3 = 2;
		int pos0 = 0;
		int pos1 = 0;
		int pos2 = 0;
		int pos3 = 0;
		int fieldLength1 = 0;
		int NaNCount = 0;
		int firstTime = 1;
		int triangleCount = 0;

		long time1, time2;
		long time3 = 0, time4;

		String fileIn = "";
		// The program requires only one argument - the object file to be
		// analyzed
		// - There must be only one argument
		// - The argument must end in .obj
		if (args.length == 1) {
			fileIn = args[0];
			if (!fileIn.substring(fileIn.length() - 4).equals(".obj")) {
				System.out
						.println("File specified is not an object file (.obj)  "
								+ fileIn + "\n");
				System.out.println("Usage:  java ObjectVolume filename.obj");
				System.out.println("Example:  java ObjectVolume cup.obj");
				System.exit(1);
			}
		} else if (args.length == 0) {
			System.out.println("An object filename is required: ");
			System.out.println("Usage:  java ObjectVolume filename.obj");
			System.out.println("Example:  java ObjectVolume cup.obj");
			System.exit(1);
		} else {
			System.out.println("Only one object filename is permitted: ");
			System.out.println("Usage:  java ObjectVolume filename.obj");
			System.out.println("Example:  java ObjectVolume cup.obj");
			System.exit(1);
		}

		time1 = System.currentTimeMillis();

		ObjectVolume X = new ObjectVolume();
		String line = "";
		System.out
				.println("\n   ObjectVolume - Calculates Volume, Surface Area and Centroid "
						+ "of an Object\n\n"
						+ "   Input File:      " + fileIn + "\n");

		// Read in the .obj file specified in the fileIn parameter. .obj files
		// contain the geometry of an object in terms of its vertices and faces.
		try {

			BufferedReader in = new BufferedReader(new FileReader(fileIn));
			int fCount = 0;
			int vIndex = 0;
			String moreSpace = "N";

			line = in.readLine();
			while ((line != null)) {
				if (line.length() > 1) {
					// Each Vertex line (begins with v) contains the three
					// coordinate values (x, y, z) for a single point in
					// 3-space. Each vertex line is parsed and placed into the
					// Vertex array V.
					// The Vertex array indexes are referenced in the Face lines
					// (begins with f) below to indicate the vertexes which make
					// up that particular face.
					if (line.substring(0, 2).startsWith("v ")) {
						vIndex = 0;
						for (int i = 1; i < line.length(); i++) {
							if (!(line.substring(i, i + 1).equals(" "))) {
								fieldLength1 = line.indexOf(" ", i);
								if (fieldLength1 == -1)
									fieldLength1 = line.length();

								V[vertexCount][vIndex] = new Double(
										line.substring(i, fieldLength1));
								vIndex++;
								i = fieldLength1;
								if (vIndex == 3)
									break;
							}
						}

						vertexCount++;

						// Each Face line (begins with f) contains three or more
						// Vertex array indexes. A Face line which contains only
						// three values defines a single triangle in 3-space.
						// This triangle is joined to the origin (0, 0, 0) to
						// form the four points of a tetrahedron. These four
						// points are sent to the calc method which caluclates
						// the signed volume of the tetrahedron, the surface
						// area of the triangle and the object center of volume.

						// If more than 3 vertex array indexes are found, a
						// single point is selected to construct triangles
						// dividing the face. For a face containing 4 vertex
						// indexes, two triangles are passed to the calc
						// function. In general, if a Face line contains n
						// vertices, n - 2 triangles will be sent to the calc
						// routine returning individual volumes and surface
						// areas.
					} else if ((line.substring(0, 1).startsWith("f"))) {
						if (firstTime == 1) {
							firstTime = 2;
							time3 = System.currentTimeMillis();
						}
						fCount++;
						// These t values are used to construct triangles from
						// faces with more than 3 vertices.
						t1 = 0;
						t2 = 1;
						t3 = 2;

						// Determine the number of faces on the line by counting
						// the number of fields between the spaces.
						// Remove any - signs.
						faceCount = 0;
						int lineLength = line.length() - 1;
						for (int i = 0; i < lineLength; i++) {
							if (line.substring(i, i + 1).equals(" ")) {
								if (moreSpace == "N")
									faceCount++;
								moreSpace = "Y";
							} else {
								moreSpace = "N";
								if (line.substring(i, i + 1).equals("-")) {
									line = line.substring(0, i)
											+ line.substring(i + 1);
									lineLength--;
								}
							}
						}
						// Place the vertex index values into the faceValues
						// array. If a "/" value is found remove it and all
						// subsequent characters till a space or end of line is
						// encountered.
						faces[faceCount]++;
						pos0 = 0;
						for (int i = 0; i < faceCount - 1; i++) {
							pos1 = line.indexOf(" ", pos0);
							while (line.substring(pos1 + 1, pos1 + 2).equals(
									" "))
								pos1++;
							pos2 = line.indexOf(" ", pos1 + 1);
							pos3 = line.substring(pos1 + 1, pos2).indexOf("/");
							if (pos3 == -1) {
								faceValues[i] = new Integer(line.substring(
										pos1 + 1, pos2));
							} else {
								faceValues[i] = new Integer(line.substring(
										pos1 + 1, pos1 + 1 + pos3));
							}
							while (line.substring(pos2 + 1, pos2 + 2).equals(
									" "))
								pos2++;
							pos0 = pos2;
						}
						pos1 = line.indexOf(" ", pos0);
						pos2 = line.indexOf(" ", pos1 + 1);
						pos3 = line.substring(pos0).indexOf("/");
						if (pos3 == -1) {
							faceValues[faceCount - 1] = new Integer(
									line.substring(pos1 + 1));
						} else {
							faceValues[faceCount - 1] = new Integer(
									line.substring(pos1 + 1, pos1 + pos3));
						}
						for (int i = 0; i < faceCount; i++) {
						}
						// call calc faceCount - 2 times. Once for each triangle
						// in the face. The first three values for point 1 are
						// (0,0,0) indicating the origin.
						for (int i = 2; i < faceCount; i++) {
							result = X.calc(0.0, 0.0, 0.0,
									V[faceValues[t1] - 1][0],
									V[faceValues[t1] - 1][1],
									V[faceValues[t1] - 1][2],
									V[faceValues[t2] - 1][0],
									V[faceValues[t2] - 1][1],
									V[faceValues[t2] - 1][2],
									V[faceValues[t3] - 1][0],
									V[faceValues[t3] - 1][1],
									V[faceValues[t3] - 1][2]);
							// If the triangle is viewed as a line from the
							// perspective of the origin it contains no volume
							// and is ignored. The number of these values is
							// however displayed at the end of the program.
							// Otherwise the signed volume is added to the total
							// volume and the area is added to the total surface
							// area.
							if (Double.isNaN(result[0])) {
								NaNCount++;
							} else {
								totalVolume += result[0];
							}
							totalArea += result[1];
							// Add the tetrhedron's weighted average centroid to
							// the global values.
							if (!Double.isNaN(result[2]))
								cx += result[2];
							if (!Double.isNaN(result[3]))
								cy += result[3];
							if (!Double.isNaN(result[4]))
								cz += result[4];
							t2++;
							t3++;
						}
					}
				}
				line = in.readLine();
			}
			// Print out the counts, object volume, object surface area and
			// center of volume.
			time4 = System.currentTimeMillis();
			System.out
					.printf("   Total Volume       =   %13.5f\n", totalVolume);
			System.out.printf("   Total Surface Area =   %13.5f\n", totalArea);
			cx = cx / totalVolume;
			cy = cy / totalVolume;
			cz = cz / totalVolume;
			System.out
					.printf("\n   Centroid of Object (x,y,z):( %8.5f ,  %8.5f , %8.5f )\n",
							cx, cy, cz);
			System.out.printf("\n   Total Number of 3D Points = %8d\n",
					vertexCount);

			System.out.printf("   Total Number of Faces =     %8d\n\n", fCount);
			System.out.println("         Number of Sides   Count");
			for (int i = 0; i < 50; i++) {
				if (faces[i] > 0.0) {
					System.out.println("                 " + i + "         "
							+ faces[i]);
					triangleCount += (i - 2) * faces[i];
				}
			}
			System.out.println("\n   Total Number of Triangles Processed = "
					+ triangleCount);
			System.out.println("   Total Number of Undefined Volumes = "
					+ NaNCount);
			time2 = System.currentTimeMillis();
			System.out.println("\n   Total Program Run Time = "
					+ (time2 - time1) + " milli seconds");
			System.out.println("   Time in Calc Method = " + (time4 - time3)
					+ " milli seconds");
			in.close();

		} catch (IOException e) {
			System.out.println("File Not Found: " + fileIn);
		}
	}

	// calc method
	//
	// 1. Determines if the normal to the triangle formed from the last three
	// points faces away from (positive) or toward the origin(negative). In a
	// .obj file the points of each face are entered in a counter clockwise
	// manner allowing the total volume to be caluclated as the difference
	// between back facing and forward facing tetrahedron volumes.
	//
	// 2. Calculates the area of the triangle formed from the last three points
	//
	// 3. Calulates the volume of a tetrahedron formed from the four input
	// points.
	//
	// 4. Calculates the center of volume of the tetrahedron and multiplies
	// these
	// values by the signed volume.
	//
	// 5. Returns:
	// - the signed volume of the tetrahedron
	// - the area of the triangle
	// - the tetrahedron's center of volume (x, y, z) weighted by the signed
	// volume
	//
	double[] calc(double x1, double y1, double z1, double x2, double y2,
			double z2, double x3, double y3, double z3, double x4, double y4,
			double z4) {

		double d1, d2, d3, d4, d5, d6;

		double[] ans = { 0, 0, 0, 0, 0 };
		double dot = 0;
		// Make vector a (from points 2 and 3) and vector b (from points 3 and
		// 4). Both a and b will lie in the same plane as the triangle.
		double a1 = x2 - x3;
		double a2 = y2 - y3;
		double a3 = z2 - z3;

		double b1 = x3 - x4;
		double b2 = y3 - y4;
		double b3 = z3 - z4;

		// Perform a cross product on the two vectors (a x b) to produce the
		// normal vector n for the triangular face.
		double n1 = (a2 * b3) - (a3 * b2);
		double n2 = ((a3 * b1) - (a1 * b3));
		double n3 = (a1 * b2) - (a2 * b1);

		// The dot product is calculated between the normal vector and a vector
		// constructed from the origin to any point on the triangle (in this
		// case point 2 is used). The sign of the dot product is used to
		// determine if the triangular surface is "backward facing" (positive
		// value) or "forward facing" (negative value).
		dot = ((x2 - x1) * n1) + ((y2 - y1) * n2) + ((z2 - z1) * n3);

		// The surface area of the triangle is calculated using |a x b| / 2.
		// where n = a x b from above producing | n | / 2.
		double area = Math.sqrt((n1 * n1) + (n2 * n2) + (n3 * n3)) / 2;
		ans[1] = area;

		// When the normal vector of the triangle is zero the volume of the
		// tetrahedron is zero and is ignored. The NaN(Not a Number) value is
		// caused by having a negative value under the square root in the volume
		// formula.
		if (dot == 0) {
			return (ans);
		}

		// Calculate the six edge lengths of the tetrahedron from the four
		// input points and square each value.

		d1 = (x3 - x1) * (x3 - x1) + (y3 - y1) * (y3 - y1) + (z3 - z1)
				* (z3 - z1);
		d2 = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) + (z2 - z1)
				* (z2 - z1);
		d3 = (x4 - x1) * (x4 - x1) + (y4 - y1) * (y4 - y1) + (z4 - z1)
				* (z4 - z1);
		d4 = (x3 - x2) * (x3 - x2) + (y3 - y2) * (y3 - y2) + (z3 - z2)
				* (z3 - z2);
		d5 = (x4 - x2) * (x4 - x2) + (y4 - y2) * (y4 - y2) + (z4 - z2)
				* (z4 - z2);
		d6 = (x4 - x3) * (x4 - x3) + (y4 - y3) * (y4 - y3) + (z4 - z3)
				* (z4 - z3);

		double vol = Math.sqrt(((d1 * d5 * (d2 + d3 + d4 + d6 - d1 - d5))
				+ (d2 * d6 * (d1 + d3 + d4 + d5 - d2 - d6))
				+ (d3 * d4 * (d1 + d2 + d5 + d6 - d3 - d4)) - (d1 * d2 * d4)
				- (d2 * d3 * d5) - (d1 * d3 * d6) - (d4 * d5 * d6)) / 144);

		if (dot < 0)
			vol = -vol;
		// Calculates the center of volume of the tetrahedron and multiplies by
		// the signed volume.
		ans[2] = ((x1 + x2 + x3 + x4) / 4) * vol;
		ans[3] = ((y1 + y2 + y3 + y4) / 4) * vol;
		ans[4] = ((z1 + z2 + z3 + z4) / 4) * vol;

		ans[0] = vol;
		return (ans);
	}
}
