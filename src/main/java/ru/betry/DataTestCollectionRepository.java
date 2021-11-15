package ru.betry;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.HashMap;

public class DataTestCollectionRepository implements IRepository {
    private HashMap<String, Document> cash;
    private static final String MongoURI = "mongodb+srv://root:yaneznauyparol@cluster0.rqcxk.mongodb.net/test";
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    DataTestCollectionRepository () {
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(java.util.logging.Level.SEVERE);
        mongoClient = new MongoClient(new MongoClientURI(MongoURI));
        database = mongoClient.getDatabase("data");
        collection = database.getCollection("test");
        cash = new HashMap<String, Document>();
    }

    public Document getItem (String id) {
        if (cash.containsKey(id))   {
            return cash.get(id);
        }
        Bson field = new Document("_id", id);
        Document doc =  collection.find(field).first();
        cash.put(id, doc);
        return doc;
    }

    public void checkAndPush(Document doc) {
        String id = doc.get("_id").toString();
        if (!cash.containsKey(id)) {
            putItem(doc);
        } else {
            if (doc != cash.get(id)) {
                putItem(doc);
            }
        }
    }

    private void putItem(Document doc) {
        String id = doc.get("_id").toString();
        if (getItem(id) == null) {
            collection.insertOne(doc);
        } else {
            Bson filter = new Document("_id", id);
            Bson updateDoc = new Document("$set", doc);
            collection.updateOne(filter, updateDoc);
        }
        cash.put(id, doc);
    }
}