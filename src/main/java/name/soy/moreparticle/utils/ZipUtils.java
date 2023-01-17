package name.soy.moreparticle.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ZipUtils {
	public static byte[] compress(byte[] in) {
		var deflater = new Deflater();
		deflater.setInput(in);
		var output = new ByteArrayOutputStream();
		deflater.finish();
		var buffer = new byte[1024];
		while (!deflater.finished()) {
			var count = deflater.deflate(buffer); // returns the generated code... index
			output.write(buffer, 0, count);
		}
		return output.toByteArray();
	}

	public static byte[] decompress(byte[] in) {
		var deflater = new Inflater();
		deflater.setInput(in);
		var output = new ByteArrayOutputStream();
		var buffer = new byte[1024];
		try {
			while (!deflater.finished()) {
				int count = deflater.inflate(buffer);
				output.write(buffer, 0, count);
			}
		} catch (DataFormatException e) {
			return new byte[0];
		}
		return output.toByteArray();
	}
}
