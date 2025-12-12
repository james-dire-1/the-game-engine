package com.james.renderEngine.shaders;

import java.io.*;
import java.nio.FloatBuffer;
import java.util.Objects;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public abstract class Shader {

    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;

    private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);

    public Shader(String vertexFile, String fragmentFile) {
        vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getUniformLocations();

        // TODO reconsider the ordering of these function calls
    }

    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try{
            InputStream stream = Shader.class.getResourceAsStream(file);
            Objects.requireNonNull(stream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while((line = reader.readLine())!=null){
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
            System.out.println("-----------------------------");
            System.out.println(file);
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;
    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    protected abstract void bindAttributes();
    protected abstract void getUniformLocations();

    protected void bindAttribute(int attributeNumber, String variableName) {
        GL20.glBindAttribLocation(programID, attributeNumber, variableName);
    }

    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    public void loadFloatToUniform(int uniformLocation, float value) {
        GL20.glUniform1f(uniformLocation, value);
    }

    public void loadMatrixToUniform(int uniformLocation, Matrix4f value) {
        value.store(buffer);
        buffer.flip();
        GL20.glUniformMatrix4fv(uniformLocation, false, buffer);
    }

    public void loadVector3fToUniform(int uniformLocation, Vector3f value) {
        GL20.glUniform3f(uniformLocation, value.x, value.y, value.z);
    }

    public void loadVector3fToUniform(int uniformLocation, float x, float y, float z) {
        GL20.glUniform3f(uniformLocation, x, y, z);
    }

    public void loadVector2fToUniform(int uniformLocation, Vector2f value) {
        GL20.glUniform2f(uniformLocation, value.x, value.y);
    }

    public void loadBooleanToUniform(int uniformLocation, boolean value) {
        float toLoad = value ? 1 : 0;
        GL20.glUniform1f(uniformLocation, toLoad);
    }

    public void cleanUp() {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

}
