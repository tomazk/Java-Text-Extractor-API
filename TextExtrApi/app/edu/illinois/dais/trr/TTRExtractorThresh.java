package edu.illinois.dais.trr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import weka.clusterers.AbstractClusterer;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Clusterer;
import weka.clusterers.Cobweb;
import weka.clusterers.EM;
import weka.clusterers.FarthestFirst;
import weka.clusterers.MakeDensityBasedClusterer;
import weka.clusterers.SimpleKMeans;
import weka.clusterers.XMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.RemarkNode;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.ScriptTag;
import org.htmlparser.tags.StyleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * Uses the information from the HTML page to extract content.
 * 
 * See http://people.cis.ksu.edu/~weninger/WH_ANNIE08.pdf, and
 * http://people.cis.ksu.edu/~weninger/WH_TIR08.pdf for more information.
 * 
 * @author weninger
 * 
 */
public class TTRExtractorThresh {

	private static Logger logger = Logger.getLogger(TTRExtractorThresh.class);

	private static double[] linkTagList;
	private static double[] derivList;

	public static String[] extractText(String html, int k, float tau) {
		html = html.replaceAll("(?s)<!--.*?-->", "");
		html = html.replaceAll("(?s)<script.*?>.*?</script>", "");
		html = html.replaceAll("(?s)<SCRIPT.*?>.*?</SCRIPT>", "");
		html = html.replaceAll("(?s)<style.*?>.*?</style>", "");
		
		String[] result = new String[2];
		result[0] = "";
		result[1] = "";
		try {
		
			Parser p = new Parser(html);
			NodeList nl = p.parse(null);
			NodeList list = nl.extractAllNodesThatMatch(new TagNameFilter(
					"TITLE"), true);
			if (list.size() >= 1) {
				result[0] = list.elementAt(0).toPlainTextString();
			}

			BufferedReader br = new BufferedReader(
					new StringReader(nl.toHtml()));
			int numLines = 0;
			while (br.readLine() != null) {
				numLines++;
			}
			br.close();

			if (numLines == 0) {
				return result;
			}


			// numLines must be even!
			//if (numLines % 2 != 0) {
			//	numLines++;
			//}
			
			linkTagList = new double[numLines];

			String line;
			br = new BufferedReader(new StringReader(nl.toHtml()));
			for (int i = 0; i < linkTagList.length; i++) {
				line = br.readLine();
				line = line.trim();
				if (line.equals("")) {
					continue;
				}
				linkTagList[i] = getTextToTagRatio(line);
			}
			br.close();

			linkTagList = smooth(linkTagList, 3);
			
			double stddev = Statistics.standardDeviation(linkTagList);
			
			result[1] = clusterHtml(html, tau*stddev);

		} catch (MalformedURLException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} catch (ParserException e) {
			logger.error(e);
			return result;
		}
		return result;
	}

	private static double[] computeDerivs(double[] list) {
		double[] newList = new double[list.length];
		for(int i=0; i<list.length-1; i++){
			if(list.length-i > 3){
				double sum = list[i+1] + list[i+2] + list[i+3];
				double avg = sum/3.0;
				double deriv = list[i] - avg;
				newList[i] = Math.abs(deriv);
			}else if(list.length-i == 3){
				double sum = list[i+1] + list[i+2];
				double avg = sum/2.0;
				double deriv = list[i] - avg;
				newList[i] = Math.abs(deriv);
			}else if(list.length-i == 2){
				double sum = list[i+1];
				double avg = sum;
				double deriv = list[i] - avg;
				newList[i] = Math.abs(deriv);
			}
			//newList[i] = list[i] - list[i+1];
		}
		return newList;
	}

	public static String removeAllTags(String html) throws ParserException{
		try {
			Parser p = new Parser(html);
			NodeList nl = p.parse(null);
			return getText(nl, new StringBuffer());
		} catch (Exception e) {
			return html.replaceAll("<[^>]*>", "");
		}
	}
	
	public static String getText(NodeList nl, StringBuffer sb){
		int i = 0;
		while (i < nl.size()) {
			Node next = nl.elementAt(i);
			if (next instanceof TextNode) {
				sb.append(((TextNode)next).getText()).append(" ");
			}else{
				if (next.getChildren() != null) {
					getText(next.getChildren(), sb);
				}
			}
			i++;
		}
		return sb.toString();
	}

	

	/**
	 * Compute the text to tag ratio of the line. Text is recorded as the number
	 * of characters in the String. Tag is recorded as the number of unique HTML
	 * tags that are found.
	 * 
	 * @param line
	 *            Single line of HTML text.
	 * @return The text to tag ratio of the given line. If no tags are found
	 *         then the text only is reported.
	 */
	private static double getTextToTagRatio(String line) {
		int tag = 0;
		int text = 0;

		for (int i = 0; i >= 0 && i < line.length(); i++) {
			if (line.charAt(i) == '<') {
				tag++;
				i = line.indexOf('>', i);
				if (i == -1) {
					break;
				}
			} else if (tag == 0 && line.charAt(i) == '>') {
				text = 0;
				tag++;
			} else {
				text++;
			}
			
		}
		if (tag == 0) {
			tag = 1;
		}
		return (double) text / (double) tag;
	}

	/**
	 * Smoothes the array by taking the moving average of the array with a
	 * radius of f.
	 * 
	 * @param in
	 *            Array to be smoothed
	 * @param f
	 *            Radius to smooth by
	 * @return The smoothed array
	 */
	private static double[] smooth(double in[], int f) {
		int size = in.length;
		double tmp[] = new double[size];

		for (int i = 0; i < size; i++) {
			int cnt = 0;
			int sum = 0;
			for (int j = (f * -1); j <= f; j++) {
				try {
					sum += in[i + j];
					cnt++;
				} catch (ArrayIndexOutOfBoundsException e) {
					// don't increment count
				}
			}
			tmp[i] = (double) sum / (double) cnt;
		}
		return tmp;
	}

	private static int getBestCluster(String results) {
		String working = results.substring(results.indexOf(" centers"), results
				.indexOf("Distortion:"));
		StringTokenizer st = new StringTokenizer(working);
		boolean start = false;
		double best = Double.MAX_VALUE;
		int bestCluster = 0;
		while(st.hasMoreTokens()){
			String tok = st.nextToken();
			if(tok.equals("Cluster")){
				start = true;
			}
			if(start){
				int clust = Integer.parseInt(st.nextToken());
				double x = Double.parseDouble(st.nextToken());
				double y = Double.parseDouble(st.nextToken());
				if(x*y < best){
					best = x*y;
					bestCluster = clust;
				}
				start = false;
			}
		}
		return bestCluster;
	}

	private static String clusterHtml(String html, double stdev) throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new StringReader(html));

		String line;
		try {
			for (int i = 0; (line = br.readLine()) != null; i++) {
				line = line.trim();
				if (line.equals("")) {
					continue;
				}

				if (linkTagList[i] > stdev) {
					sb.append(line).append('\n');
				}
			}
			br.close();
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}

}

