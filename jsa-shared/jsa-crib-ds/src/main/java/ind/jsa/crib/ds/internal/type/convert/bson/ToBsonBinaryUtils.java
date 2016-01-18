package ind.jsa.crib.ds.internal.type.convert.bson;

import org.bson.BsonBinary;

public class ToBsonBinaryUtils {
	private ToBsonBinaryUtils() {}
	
	public static BsonBinary strToBsonBinary(String str) {
		return str != null ? new BsonBinary(str.getBytes()) : null;
	}

	public static BsonBinary bytesToBsonBinary(byte[] bytes) {
		return bytes != null ? new BsonBinary(bytes) : null;
	}
}
