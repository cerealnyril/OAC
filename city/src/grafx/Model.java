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


import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Oskar
 */
public class Model {

    public List<Float> vertices = new ArrayList<Float>();
    public List<Vector3f> normals = new ArrayList<Vector3f>();
    public List<Face> faces = new ArrayList<Face>();
	private int modelId;

    public Model() {
    }

	public float[] getVertex() {
		float[] f = new float[vertices.size()];
		Iterator<Float> iter = vertices.iterator();
		int a=0;
		while (iter.hasNext()){
			float i = iter.next();
			f[a] = i;
			a++;
		}
		return f;
	}

	public int[] getIndex() {
		int[] f = new int[faces.size()*3];
		int a = 0;
		Iterator<Face> iter = faces.iterator();
		while (iter.hasNext()){
			Face i = iter.next();
			f[a] = i.v1;
			f[a+1] = i.v2;
			f[a+2] = i.v3;
			a += 3;
		}
		return f;
	}
	
	public float[] toFloatNormals() {
		float[] f = new float[normals.size()*3];
		int a = 0;
		Iterator<Vector3f> iter = normals.iterator();
		while (iter.hasNext()){
			Vector3f i = iter.next();
			f[a] = i.x;
			f[a+1] = i.y;
			f[a+2] = i.z;
		}
		a += 3;
		return f;
	}
	
	public void setId(int id){
		this.modelId = id;
	}
}

