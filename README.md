# ObjectVolume
Calculates Volume, Surface Area and Centroid of a Closed Triangular Mesh

The ObjectVolume calculator is program written as a standalone java application and is executed at the command line in either Windows or Linux or other supported Java environment. The ObjectVolume Tutorial document gives a detailed account of the approach used to determine the volume of a closed triangular mesh represented in an object file (.obj).

The ObjectVolume.zip file contains: 

  Program Executables and Samples directory - 
    ObjectVolume.class Four sample .obj files (cup.obj, sphere2.obj, etc.)
  
  Java source code - 
      The four programs used to perform the calculations. Each program is the same except for the calc method. Each calc method uses a different set of equations to determine the volume of a tetrahedron. It turns out that all four produced similar results and run times. Method 2 was used to generate the ObjectVolume.class file above.
  
  VolumeTutorial.doc - 
    Contains the project background and theory, a detailed program description, notes and execution results.
  
Copy and unzip the ObjectVolume.zip file under your home directory or anywhere you have read/write privileges. This will create a single directory named ObjectVolume which will contain the subdirectories and files described above.
