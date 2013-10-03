package grafx;
/*
 * Copyright (c) 2012, Oskar Veerhoek
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the FreeBSD Project.
 */


//import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;
import java.nio.FloatBuffer;

//import org.lwjgl.opengl.*;

/**
 * @author Oskar
 */
public class OBJLoader {
    private static FloatBuffer reserveData(int size) {
        FloatBuffer data = null;//BufferUtils.createFloatBuffer(size);
        return data;
    }

    private static float[] asFloats(Vector3f v) {
        return new float[]{v.x, v.y, v.z};
    }


    public static Model loadModel(File f) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        Model m = new Model();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("v ")) {
                float x = Float.valueOf(line.split(" ")[1]);
                float y = Float.valueOf(line.split(" ")[2]);
                float z = Float.valueOf(line.split(" ")[3]);
                m.vertices.add(x);
                m.vertices.add(y);
                m.vertices.add(z);
                m.vertices.add(0f);
                m.vertices.add(0f);
                m.vertices.add(0f);
                m.vertices.add(0f);
                m.vertices.add(1f);
            } else if (line.startsWith("vn ")) {
                float x = Float.valueOf(line.split(" ")[1]);
                float y = Float.valueOf(line.split(" ")[2]);
                float z = Float.valueOf(line.split(" ")[3]);
                m.normals.add(new Vector3f(x, y, z));
            } else if (line.startsWith("vt ")) {

            } else if (line.startsWith("f ")) {
                int v1 = Integer.valueOf(line.split(" ")[1].split("/")[0]);
                int v2 = Integer.valueOf(line.split(" ")[2].split("/")[0]);
                int v3 = Integer.valueOf(line.split(" ")[3].split("/")[0]);
            	//Vector3f vertexIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[0]),
                //        Float.valueOf(line.split(" ")[2].split("/")[0]),
                //        Float.valueOf(line.split(" ")[3].split("/")[0]));
                int n1 = Integer.valueOf(line.split(" ")[1].split("/")[2]);
                int n2 = Integer.valueOf(line.split(" ")[2].split("/")[2]);
                int n3 = Integer.valueOf(line.split(" ")[3].split("/")[2]);
            	//Vector3f normalIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[2]),
                //        Float.valueOf(line.split(" ")[2].split("/")[2]),
                //        Float.valueOf(line.split(" ")[3].split("/")[2]));
                //  m.faces.add(new Face(vertexIndices, normalIndices));
                m.faces.add(new Face(v1,v2,v3,n1,n2,n3));
            }
        }
        reader.close();
        return m;
    }
}