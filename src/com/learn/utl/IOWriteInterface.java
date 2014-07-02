package com.learn.utl;

import java.io.InputStream;

public interface IOWriteInterface {
	String name();

	String qualifiedName();
	
	String savePath();
	
	String extension();
	
	InputStream fileToWrite();
}