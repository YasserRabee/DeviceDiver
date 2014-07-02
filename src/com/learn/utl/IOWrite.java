package com.learn.utl;

import java.io.InputStream;
import java.util.Random;

public class IOWrite implements IOWriteInterface {

	private InputStream iStream;
	private String fName;
	private String sPath;
	private String ext;
	private boolean _isCounted;

	public IOWrite(String name, String extension, InputStream is,
			String savePath, boolean isCounted) {
		fName = name;
		iStream = is;
		sPath = savePath;
		ext = extension;
		_isCounted = isCounted;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return fName;
	}

	@Override
	public String qualifiedName() {
		if (_isCounted) {
			Random rand = new Random();
			return fName + "_" + Math.abs(rand.nextLong()) + ext;
		} else {
			return fName + ext;
		}
	}

	@Override
	public InputStream fileToWrite() {
		// TODO Auto-generated method stub
		return iStream;
	}

	@Override
	public String savePath() {
		// TODO Auto-generated method stub
		return sPath;
	}

	@Override
	public String extension() {
		// TODO Auto-generated method stub
		return ext;
	}

}