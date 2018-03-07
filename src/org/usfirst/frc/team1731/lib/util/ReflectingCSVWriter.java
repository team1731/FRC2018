package org.usfirst.frc.team1731.lib.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Writes data to a CSV file
 */
public class ReflectingCSVWriter<T> {
    ConcurrentLinkedDeque<String> mLinesToWrite = new ConcurrentLinkedDeque<>();
    PrintWriter mOutput = null;
    Field[] mFields;

    public ReflectingCSVWriter(String fileName, Class<T> typeClass) {
    	//
    	// rename existing file so it's
    	// available after we cycle power
    	//
    	File existingFile = new File(fileName);
    	if(existingFile.exists()) {
    		File previousFile = new File(fileName + ".prev");
    		if(previousFile.exists()) {
    			previousFile.delete();
    		}
    		existingFile.renameTo(previousFile);
    	}
    	
    	
        mFields = typeClass.getFields();
        try {
            mOutput = new PrintWriter(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Write field names.
        StringBuffer line = new StringBuffer();
        for (Field field : mFields) {
            if (line.length() != 0) {
                line.append(", ");
            }
            line.append(field.getName());
        }
        writeLine(line.toString());
    }

    public void add(T value) {
        StringBuffer line = new StringBuffer();
        for (Field field : mFields) {
            if (line.length() != 0) {
                line.append(", ");
            }
            try {
                line.append(field.get(value).toString());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        mLinesToWrite.add(line.toString());
    }

    protected synchronized void writeLine(String line) {
        if (mOutput != null) {
            mOutput.println(line);
        }
    }

    // Call this periodically from any thread to write to disk.
    public void write() {
        while (true) {
            String val = mLinesToWrite.pollFirst();
            if (val == null) {
                break;
            }
            writeLine(val);
        }
    }

    public synchronized void flush() {
        if (mOutput != null) {
            write();
            mOutput.flush();
        }
    }
}
