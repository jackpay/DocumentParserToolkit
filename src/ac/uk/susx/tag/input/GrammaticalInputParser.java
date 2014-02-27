package ac.uk.susx.tag.input;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import ac.uk.susx.tag.annotator.ChunkTagAnnotatorFactory;
import ac.uk.susx.tag.annotator.LocationAnnotatorFactory;
import ac.uk.susx.tag.annotator.OrganisationAnnotatorFactory;
import ac.uk.susx.tag.annotator.PersonAnnotatorFactory;
import ac.uk.susx.tag.annotator.PoSTagAnnotatorFactory;
import ac.uk.susx.tag.annotator.SentenceAnnotatorFactory;
import ac.uk.susx.tag.annotator.TokenAnnotatorFactory;
import ac.uk.susx.tag.annotator.registry.AnnotatorRegistry;
import ac.uk.susx.tag.configuration.CharSequenceConfiguration;

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
		
		public String buildOutputName(){
			StringBuilder sb = new StringBuilder();
			if(token) { sb.append("tok-"); }
			if(sentenceTag) { sb.append("s-"); }
			if(postag) { sb.append("pos-"); }
			if(chunkToken) { sb.append("ct-"); }
			if(person) { sb.append("per-"); }
			if(location) { sb.append("loc-"); }
			if(organisation) { sb.append("org-"); }
			return sb.toString().substring(0, sb.toString().length()-1);
		}
	}

	@SuppressWarnings("unused")
	@Override
	public CharSequenceConfiguration parseInputParameters(String[] args) {
		GrammaticalInputReader reader = new GrammaticalInputReader();
		JCommander jcomm = new JCommander(reader, args);
		String output = reader.singleFileOutput() ? reader.output() : reader.output() + "/" + reader.buildOutputName();
		CharSequenceConfiguration gc = new CharSequenceConfiguration(reader.input(), output);

		gc.setInputSuff(reader.suffix());
		gc.setOutSuff(reader.outSuffix());
		gc.setSingleFileOutput(reader.singleFileOutput());
		
		if(reader.token()){
			try {
				gc.addAnnotator(AnnotatorRegistry.getAnnotator(TokenAnnotatorFactory.class), true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(reader.help()) {
			for(CommandLineOption s : AnnotatorRegistry.getOptions()) {
				System.err.println(s.getCommand() + "	" + s.getDescription());
			}
		}
		
		if(reader.sentence()){
			try {
				gc.addAnnotator(AnnotatorRegistry.getAnnotator(SentenceAnnotatorFactory.class), true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(reader.posTag()){
			try {
				gc.addAnnotator(AnnotatorRegistry.getAnnotator(PoSTagAnnotatorFactory.class), true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(reader.chunkToken()){
			try {
				gc.addAnnotator(AnnotatorRegistry.getAnnotator(ChunkTagAnnotatorFactory.class), true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(reader.person()){
			try {
				gc.addAnnotator(AnnotatorRegistry.getAnnotator(PersonAnnotatorFactory.class), true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(reader.location()){
			try {
				gc.addAnnotator(AnnotatorRegistry.getAnnotator(LocationAnnotatorFactory.class), true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(reader.organisation()){
			try {
				gc.addAnnotator(AnnotatorRegistry.getAnnotator(OrganisationAnnotatorFactory.class), true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(String s : reader.getAdditionalAnnotators()){
			try {
				gc.addAnnotator(AnnotatorRegistry.getAnnotator(s));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return gc;
	}

}
