/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * driver for training and saving my custom agent models
 */

package agent;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import opennlp.tools.doccat.BagOfWordsFeatureGenerator;
import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.doccat.FeatureGenerator;
import opennlp.tools.namefind.*;
import opennlp.tools.util.*;

public class TrainingGround {
	static final String MODEL_FOLDER = "C:\\Users\\Grady\\eclipse-workspace\\RubberDuck\\src\\agent\\models\\";
	/* Name Entity Recognition (NER) model
	 * modified from https://www.tutorialkart.com/opennlp/ner-training-in-opennlp-with-name-finder-training-java-example/
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void trainNER(String fin, String fout) {
		try {
			System.out.println("Reading NER input...");
			InputStreamFactory in = new MarkableFileInputStreamFactory(new File(fin));
			ObjectStream sampleStream = new NameSampleDataStream(new PlainTextByLineStream(in, StandardCharsets.UTF_8));
			
			TrainingParameters params = new TrainingParameters();
			params.put(TrainingParameters.ITERATIONS_PARAM, 100); //number of training iterations
			params.put(TrainingParameters.CUTOFF_PARAM, 1); //word must appear at least this many times to be considered
			System.out.println("Training NER model...");
			TokenNameFinderModel nameFinderModel = NameFinderME.train("en", null, sampleStream, params, TokenNameFinderFactory.create(null, null, Collections.emptyMap(), new BioCodec()));
			
			System.out.println("Writing NER model to: " + fout);
			FileOutputStream outStream = new FileOutputStream(new File(fout));
			nameFinderModel.serialize(outStream);
			
			outStream.close();
			System.out.println("Success!");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/* category recognition
	 * modified from https://itsallbinary.com/create-your-own-chat-bot-in-java-using-apache-opennlp-artificial-intelligence-natural-language-processing/
	 */
	private static void trainCAT(String fin, String fout) {
		try {
			System.out.println("Reading category input...");
			InputStreamFactory in = new MarkableFileInputStreamFactory(new File(fin));
			ObjectStream<String> lineStream = new PlainTextByLineStream(in, StandardCharsets.UTF_8);
			ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);
		 
			TrainingParameters params = new TrainingParameters();
			params.put(TrainingParameters.ITERATIONS_PARAM, 100); //number of training iterations
			params.put(TrainingParameters.CUTOFF_PARAM, 1); //word must appear at least this many times to be considered
			System.out.println("Training category model...");
			DoccatFactory factory = new DoccatFactory(new FeatureGenerator[] { new BagOfWordsFeatureGenerator() });
			DoccatModel catModel = DocumentCategorizerME.train("en", sampleStream, params, factory);
			
			System.out.println("Writing category model to: " + fout);
			FileOutputStream outStream = new FileOutputStream(new File(fout));
			catModel.serialize(outStream);
			
			outStream.close();
			System.out.println("Success!");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//main
	public static void main(String args[]) {
		System.out.println("Creating NER model:");
		trainNER(MODEL_FOLDER+"ner-training-data.txt", MODEL_FOLDER+"ner-custom-model.bin");
		System.out.println("\n\nCreating category model:");
		trainCAT(MODEL_FOLDER+"cat-training-data.txt", MODEL_FOLDER+"cat-custom-model.bin");
	}
}
