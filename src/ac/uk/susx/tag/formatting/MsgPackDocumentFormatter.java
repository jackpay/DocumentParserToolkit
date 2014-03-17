package ac.uk.susx.tag.formatting;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Unpacker;

import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.document.StringDocument;

public class MsgPackDocumentFormatter implements InputDocumentFormatter<String,String>{

	public IDocument<String, String> createDocument(String fileLocation) {
		return createDocument(new File(fileLocation));
	}

	public IDocument<String, String> createDocument(File file) {
		MessagePack msgpack = new MessagePack();
		byte[] bytes = null;
		try {
			bytes = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		Unpacker unpacker = msgpack.createUnpacker(in);
		StringBuilder sb = new StringBuilder();
		Iterator<Value> iter = unpacker.iterator();
		while(iter.hasNext()){
			Value next = iter.next();
			next.toString(sb);
			}
		return new StringDocument(sb.toString());
	}

}
