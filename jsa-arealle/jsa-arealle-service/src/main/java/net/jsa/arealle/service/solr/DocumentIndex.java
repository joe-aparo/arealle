package net.jsa.arealle.service.solr;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.jsa.common.logging.LogUtils;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;

/**
 * Abstract client for indexing and reading Solr documents.
 * 
 * @author jsaparo
 *
 */
public abstract class DocumentIndex {
	private Logger log = LogUtils.getLogger();
	
	/**
	 * Compose a map of values representing a document object read from a SOLR index.
	 * 
	 * @param response A response obtained from a Solr query
	 * @return A map of values representing the document
	 */
	public Map<String, Object> readDocument(QueryResponse response) {
		Map<String, Object> item = null;
		
		SolrDocumentList items = response.getResults();
		
		if (items.getNumFound() > 0) {
			SolrDocument doc = items.get(0);
			Collection<String> fldNames = doc.getFieldNames();
			item = new LinkedHashMap<String, Object>(fldNames.size());

			for (String fld : doc.getFieldNames()) {
				item.put(fld, doc.getFieldValue(fld));
			}
			
			item.put("NUM_FOUND", new Long(items.getNumFound()));
		}
		
		return item;
	}
	
	/**
	 * Index a given collection of Solr documents.
	 * 
	 * @param docs A collection of Solr documents
	 */
	public void addDocuments(List<SolrInputDocument> docs) {
		try {
			getClient().add(docs);
		} catch (SolrServerException | IOException ex) {
			log.error("Error adding documents", ex);
		}
	}
	
	protected Logger getLog() {
		return log;
	}
	
	public abstract HttpSolrServer getClient();

	protected void applyMultiValuedElementsToDocument(MultiValuedMap valueMap, SolrInputDocument doc) {
		// Add multi-valued fields to document
		for (Entry<String, Set<Object>> e : valueMap.getEntries()) {
			if (e.getValue().size() > 0) {
				doc.addField(e.getKey(), e.getValue());
			}
		}
	}
}
