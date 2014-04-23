package ac.uk.susx.tag.formatting;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.msgpack.MessagePack;
import org.msgpack.type.ArrayValue;
import org.msgpack.type.MapValue;
import org.msgpack.type.Value;
import org.msgpack.unpacker.Unpacker;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import ac.uk.susx.tag.document.IDocument;
import ac.uk.susx.tag.document.StringDocument;

public class LumiMsgPackDocumentFormatter implements InputDocumentFormatter<String,String>{
	
	private static final String BODY = "body";
	private static final String LANG = "language";
	private static final String LANG_CODE = "en";
	private static final String TITLE = "title";

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
		try {
			Value v = unpacker.readValue();
			MapValue mv = v.asMapValue();
			for(Value key : mv.keySet()) {
				ArrayValue aKey = (ArrayValue) key;
				String keyVal = aKey.get(1).toString().replace("\"", "");
				if(keyVal != null && keyVal.equals(LANG) && !mv.get(key).isNilValue()) {
					if(!mv.get(key).toString().equals(null)){
						if(!mv.get(key).asArrayValue().get(1).toString().replace("\"", "").equals(LANG_CODE) && !mv.get(key).asArrayValue().get(1).toString().replace("\"", "").equals("en-US")) {
							return null;
						}
					}
				}
				else{
					if(!mv.get(key).isNilValue() && (keyVal.equals(TITLE) || keyVal.equals(BODY))) {
						sb.append(parseHTML(mv.get(key).asArrayValue().get(1).toString()));
					}
				}
			}
			if(sb.toString().length() == 0 || sb.toString() == null || sb.toString().replace("\n", "").replace("\t", "").length() == 0) {
				return null;
			}
			else{
				return new StringDocument(sb.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String parseHTML(String html) {
		String doc = null;
		try {
			doc = ArticleExtractor.INSTANCE.getText(html);
		} catch (BoilerpipeProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc.replace("\\n", " ").replace("\\t", " ");
	}

}
