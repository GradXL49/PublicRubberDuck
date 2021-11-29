/*
 * Grady Landers
 * Master's Project - Code name Rubber Duck
 * chatbot agent model - modified from https://itsallbinary.com/create-your-own-chat-bot-in-java-using-apache-opennlp-artificial-intelligence-natural-language-processing/
 */

package agent;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import backend.MyUtilities;

import opennlp.tools.doccat.*;
import opennlp.tools.namefind.*;
import opennlp.tools.postag.*;
import opennlp.tools.sentdetect.*;
import opennlp.tools.tokenize.*;
import opennlp.tools.util.*;

public class AgentModel {
	static final String MODEL_FOLDER = "C:\\Users\\Grady\\eclipse-workspace\\RubberDuck\\src\\agent\\models\\";
	
	//variables
	private Map<String, String> questionAnswer = new HashMap<>();
	private DoccatModel model;
	private DocumentCategorizerME catModel;
	private SentenceDetectorME sentModel;
	private TokenizerME tokenModel;
	private POSTaggerME posModel;
	private SimpleLemmatizer lemmaModel;
	private NameFinderME entityModel;
	
	//constructor
	public AgentModel() {
		try {
			//load category model
			catModel = new DocumentCategorizerME(new DoccatModel(new FileInputStream(MODEL_FOLDER+"cat-custom-model.bin")));
			
			//load sentence model
			sentModel = new SentenceDetectorME(new SentenceModel(new FileInputStream(MODEL_FOLDER+"en-sent.bin")));
			
			//load token model
			tokenModel = new TokenizerME(new TokenizerModel(new FileInputStream(MODEL_FOLDER+"en-token.bin")));
			
			//load pos model
			posModel = new POSTaggerME(new POSModel(new FileInputStream(MODEL_FOLDER+"en-pos-maxent.bin")));
			 
			//load lemma model
			lemmaModel = new SimpleLemmatizer(new FileInputStream(MODEL_FOLDER+"en-lemmatizer.dict"));
			
			//load entity model
			entityModel = new NameFinderME(new TokenNameFinderModel(new FileInputStream(MODEL_FOLDER+"ner-custom-model.bin")));
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error initializing agent model.");
		}
		
		//initialize QnA pairings
		questionAnswer.put("greeting", "Nice to meet you :) ");
		questionAnswer.put("conversation-continue", "What else can I help you with? ");
		questionAnswer.put("conversation-complete", "Nice chatting with you. Bbye. ");
		questionAnswer.put("full-inquisition", "Let's do that!");
		questionAnswer.put("operation-inquisition", "Okay, let's perform a ");
		questionAnswer.put("db-inquisition", "Okay, let's make ");
	}
	
	//utilities
	/**
	 * Detect category using given token. Use categorizer feature of Apache OpenNLP.
	 * 
	 * @param model
	 * @param finalTokens
	 * @return
	 * @throws IOException
	 */
	private String detectCategory(DoccatModel model, String[] finalTokens) throws IOException {
		double[] probabilitiesOfOutcomes = catModel.categorize(finalTokens);
		String category = catModel.getBestCategory(probabilitiesOfOutcomes);
		System.out.println("Category: " + category);
		System.out.println("Accuracies:");
		for(int i=0; i<probabilitiesOfOutcomes.length; i++) {
			System.out.println("\t" + catModel.getCategory(i) + ": " + probabilitiesOfOutcomes[i]);
		}
	 
		return category;
	}
	 
	/**
	 * Break data into sentences using sentence detection feature of Apache OpenNLP.
	 * 
	 * @param data
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String[] breakSentences(String data) throws FileNotFoundException, IOException {
		String[] sentences = sentModel.sentDetect(data);
		System.out.println("Sentence Detection: " + Arrays.stream(sentences).collect(Collectors.joining(" | ")));
 
		return sentences;
	}
	 
	/**
	 * Break sentence into words & punctuation marks using tokenizer feature of
	 * Apache OpenNLP.
	 * 
	 * @param sentence
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String[] tokenizeSentence(String sentence) throws FileNotFoundException, IOException {
		// Tokenize sentence.
		String[] tokens = tokenModel.tokenize(sentence);
		System.out.println("Tokenizer : " + Arrays.stream(tokens).collect(Collectors.joining(" | ")));
 
		return tokens;
	}
	 
	/**
	 * Find part-of-speech or POS tags of all tokens using POS tagger feature of
	 * Apache OpenNLP.
	 * 
	 * @param tokens
	 * @return
	 * @throws IOException
	 */
	private String[] detectPOSTags(String[] tokens) throws IOException {
		// Tag sentence.
		String[] posTokens = posModel.tag(tokens);
		System.out.println("POS Tags : " + Arrays.stream(posTokens).collect(Collectors.joining(" | ")));
 
		return posTokens;
	}
	 
	/**
	 * Find lemma of tokens using SimpleLemmatizer from https://github.com/richardwilly98/elasticsearch-opennlp-auto-tagging
	 * 
	 * @param tokens
	 * @param posTags
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	private String[] lemmatizeTokens(String[] tokens, String[] posTags) throws InvalidFormatException, IOException {
		String[] lemmaTokens = new String[tokens.length];
		
		for(int i=0; i<tokens.length; i++) {
			lemmaTokens[i] = lemmaModel.lemmatize(tokens[i], posTags[i]);
		}
		
		return lemmaTokens;
	}
	
	//find entities
	private Span[] detectEntities(String[] tokens, String[] response, int index) {
		Span[] out = entityModel.find(tokens);
		System.out.println("Entities from sentence #" + (index+1) + ":");
		for(int i=0; i<out.length; i++) {
			System.out.println("\t" + out[i]);
		}
		
		return out;
	}
	
	private void extractEntities(Span[] entities, String[] sentence, String[] response) {
		int type = 0;
		String word = null;
		for(int j=0; j<entities.length; j++) {
			switch(entities[j].getType()) {
			case "operation": type=1; break;
			case "db_type": type=2; break;
			case "g_type": type=3;
			}
			word = sentence[entities[j].getStart()];
			
			if(type > 0 && word != null) {
				if(type == 1 && !word.contentEquals("path")) {
					if(word.contentEquals("all") || word.contentEquals("every")) {
						response[1] = "path dijkstra";
					}
					else if(word.contentEquals("specific") || word.contains("one") || word.contains("single")) {
						response[1] = "path aStar";
					}
					else response[type] = word;
				}
				else response[type] = word;
			}
		}
	}
	
	public void getResponse(String message, String[] response, boolean input) {
		try {
			response[0] = "";
			
			// Break users chat input into sentences using sentence detection.
			String[] sentences = breakSentences(message);
			Span[][] entities = new Span[sentences.length][];
			
			for(int i=0; i<sentences.length; i++) {
				// Separate words from each sentence using tokenizer.
				String[] tokens = tokenizeSentence(sentences[i]);
				
				//collect entities
				entities[i] = detectEntities(tokens, response, i);
	 
				// Tag separated words with POS tags to understand their gramatical structure.
				String[] posTags = detectPOSTags(tokens);
	 
				// Lemmatize each word so that its easy to categorize.
				String[] lemmas = lemmatizeTokens(tokens, posTags);
	 
				// Determine BEST category using lemmatized tokens used a mode that we trained
				// at start.
				String category = detectCategory(model, lemmas);
	 
				//get response based on category and check for entitites 
				response[0] += questionAnswer.get(category);
				
				if(input) {
					response[4] = message;
					response[0] = "";
				}
				else if(category.contains("inquisition")) {
					extractEntities(entities[i], tokens, response);
					//System.out.println("Response array:\n" + MyUtilities.printArr(response));
					
					switch(category) {
					case "operation-inquisition":
						response[0] += response[1]+". ";
						break;
					case "db-inquisition":
						if(response[2].contains("array")) response[0] += "an array! ";
						else response[0] += "a graph. ";
						break;
					default:
						//if any options are missing, ask for them
						if(response[1] == null) response[0] = "An operation type is missing, please let me know what you want to do with your data structure.";
						else {
							if(response[2] == null) response[0] = "You did not specify a database type. Please tell me whether you want an array or a graph.";
							else if(response[2].contentEquals("graph")) {
								if(response[3]==null) response[0] = "Did you want a weighted, or unweighted graph?";
								else if(response[1].contentEquals("path")) response[0] = "Do you want all shortest paths or one specific path?";
							}
						}
					}
				}
				
				//for debug
				System.out.println("Response dump:");
				System.out.println(MyUtilities.printArr(response));
				System.out.println();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			response[0] = "An error occurred interpreting the last message.";
		}
	}
}
