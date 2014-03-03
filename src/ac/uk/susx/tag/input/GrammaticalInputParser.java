package ac.uk.susx.tag.input;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import ac.uk.susx.tag.annotator.enums.StringAnnotatorEnum;
import ac.uk.susx.tag.configuration.StringConfiguration;

public class GrammaticalInputParser extends AbstractInputParameterParser {
	
	public class GrammaticalInputReader extends AbstractInputReader {
		
		@Parameter 
		(names = {"-t", "--token"}, description="Token annotations")
		private boolean token = true;
		
		@Parameter
		(names = {"-pos", "--posTag"}, description="Pos tag annotations")
		private boolean postag = false;
		
		@Parameter
		(names = {"-ct", "chunkToken"}, description="Chunk token annotations")
		private boolean chunkToken = false;
		
		@Parameter
		(names = {"-s", "--sentence"}, description="Sentence annotations")
		private boolean sentenceTag = false;
		
		@Parameter
		(names = {"-lem", "--lemmatise"}, description="Text lemmatiser")
		private boolean lemmatise =  false;
		
		@Parameter
		(names = {"-stem", "--stemmer"}, description="Stems words")
		private boolean stem = false;
		
		@Parameter
		(names = {"-per", "--person"}, description="Person Annotations")
		private boolean person = false;
		
		@Parameter
		(names = {"-org", "--organisation"}, description="Organisation Annotations")
		private boolean organisation = false;
		
		@Parameter
		(names = {"-loc", "--location"}, description="Location Annotations")
		private boolean location = false;
		
		public boolean chunkToken(){
			return chunkToken;
		}
		
		public boolean token(){
			return token;
		}
		
		public boolean sentence(){
			return sentenceTag;
		}
		
		public boolean posTag(){
			return postag;
		}
		
		public boolean person(){
			return person;
		}
		
		public boolean organisation(){
			return organisation;
		}
		
		public boolean location(){
			return location;
		}
		
		public boolean lemmatise() {
			return lemmatise;
		}
		
		public boolean stem() {
			return stem;
		}
		
		public String buildOutputName(){
			StringBuilder sb = new StringBuilder();
			if(token) { sb.append("tok-"); }
			if(sentenceTag) { sb.append("s-"); }
			if(postag) { sb.append("pos-"); }
			if(chunkToken) { sb.append("ct-"); }
			if(person) { sb.append("per-"); }
			if(location) { sb.append("loc-"); }
			if(organisation) { sb.append("org-"); }
			if(lemmatise) { sb.append("l-"); }
			if(stem){ sb.append("stem-"); }
			return sb.toString().length() > 0 ? sb.toString().substring(0, sb.toString().length()-1) : "output";
		}
	}

	@SuppressWarnings("unused")
	@Override
	public StringConfiguration parseInputParameters(String[] args) {
		GrammaticalInputReader reader = new GrammaticalInputReader();
		JCommander jcomm = new JCommander(reader, args);
		String output = reader.singleFileOutput() ? reader.output() : reader.output() + "/" + reader.buildOutputName();
		StringConfiguration gc = new StringConfiguration(reader.input(), output);

		gc.setInputSuff(reader.suffix());
		gc.setOutSuff(reader.outSuffix());
		gc.setSingleFileOutput(reader.singleFileOutput());
		
		if(reader.lemmatise()) {
			gc.addAnnotator(StringAnnotatorEnum.LEMMATISER.getAnnotator(),true);
		}
		else{
			if(reader.stem()) {
				gc.addAnnotator(StringAnnotatorEnum.STEMMER.getAnnotator(), true);
			}
			else {
				if(reader.token()) {
					gc.addAnnotator(StringAnnotatorEnum.TOKEN.getAnnotator(), true);
				}
			}
		}
		//reader.getAdditionalAnnotators();
		if(reader.sentence()){
			gc.addAnnotator(StringAnnotatorEnum.SENTENCE.getAnnotator(), true);
		}
		if(reader.posTag()){
			gc.addAnnotator(StringAnnotatorEnum.POSTAG.getAnnotator(), true);
		}
		if(reader.chunkToken()){
			gc.addAnnotator(StringAnnotatorEnum.CHUNKTAG.getAnnotator(), true);
		}
		if(reader.person()){
			gc.addAnnotator(StringAnnotatorEnum.PERSON.getAnnotator(), true);
		}
		if(reader.location()){
			gc.addAnnotator(StringAnnotatorEnum.LOCATION.getAnnotator(), true);
		}
		if(reader.organisation()){
			gc.addAnnotator(StringAnnotatorEnum.ORGANISATION.getAnnotator(), true);
		}
		return gc;
	}

}
