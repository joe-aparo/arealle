package net.jsa.arealle.service.mongo;

import java.util.List;

import javax.annotation.Resource;

import net.jsa.common.logging.LogUtils;

import org.slf4j.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * Abscract Mongo document storage client.
 * 
 * @author jsaparo
 *
 */
public class DocumentStore {
	public static final String DB_NAME = "arealle";

	private Logger log = LogUtils.getLogger();
	private String collName;

	@Resource(name="mongoClient")
	private MongoClient mongoClient;
	
	public DocumentStore(String collName) {
		this.collName = collName;
	}
	
	protected Logger getLog() {
		return log;
	}
	
	protected DB getDB() {
		return mongoClient.getDB(DB_NAME);
	}
	
	protected DBCollection getCollection() {
		return getDB().getCollection(collName);
	}

	public void addDocuments(List<DBObject> docs) {
		getCollection().insert(docs);
	}
	
	public DBObject getDocumentById(String id) {
		return getCollection().findOne(id);
	}
}
