package com.james.audio.loaders;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;

/**
 * Audio loader class specifically for .wav files. Pretty much all the code in here was written by ThinMatrix,
 * and is meant to be a replacement for the old WaveData class that used to exist in lwjgl 2. If we didn't have
 * this code, we'd probably have to convert all audio files to .ogg format, or come up with some hacky sketchy
 * way to mix lwjgl 2 functionality with lwjgl 3. Yikes! To be honest, I don't know what a huge chunk of this
 * code does. https://www.youtube.com/watch?v=ptjIf_9CKGE
 */
public class WavLoader extends AudioLoader {

	final int totalBytes;
	final int bytesPerFrame;

	private final AudioInputStream audioStream;
	private final byte[] dataArray;

	private WavLoader(AudioInputStream stream) {
		this.audioStream = stream;
		AudioFormat audioFormat = stream.getFormat();
		this.format = getOpenAlFormat(audioFormat.getChannels(), audioFormat.getSampleSizeInBits());
		this.sampleRate = (int) audioFormat.getSampleRate();
		this.bytesPerFrame = audioFormat.getFrameSize();
		this.totalBytes = (int) (stream.getFrameLength() * bytesPerFrame);
		this.rawAudioBuffer = BufferUtils.createByteBuffer(totalBytes);
		this.dataArray = new byte[totalBytes];
		loadData();
	}

	/**
	 * Clean up method to be called after creating the OpenAL buffer.
	 */
	@Override
	public void dispose() {
		try {
			audioStream.close();
			rawAudioBuffer.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadData() {
		try {
			int bytesRead = audioStream.read(dataArray, 0, totalBytes);
			rawAudioBuffer.clear();
			rawAudioBuffer.put(dataArray, 0, bytesRead);
			rawAudioBuffer.flip();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Couldn't read bytes from audio stream!");
		}
	}

	/**
	 * Called from AudioLoader's load() method. Entry point for all the heavy lifting to do with .wav files.
	 */
	public static WavLoader create(String path) {
		InputStream stream = WavLoader.class.getResourceAsStream(path);
		if(stream==null){
			System.err.println("Couldn't find file: "+path);
			return null;
		}
		InputStream bufferedInput = new BufferedInputStream(stream);
		AudioInputStream audioStream = null;
		try {
			audioStream = AudioSystem.getAudioInputStream(bufferedInput);
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		Objects.requireNonNull(audioStream);
		return new WavLoader(audioStream);
	}

	private static int getOpenAlFormat(int channels, int bitsPerSample) {
		if (channels == 1) {
			return bitsPerSample == 8 ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_MONO16;
		} else {
			return bitsPerSample == 8 ? AL10.AL_FORMAT_STEREO8 : AL10.AL_FORMAT_STEREO16;
		}
	}

}
