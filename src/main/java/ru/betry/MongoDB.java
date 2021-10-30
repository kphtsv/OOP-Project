package ru.betry;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;


public class MongoDB {
    private static final String MongoURI = "mongodb+srv://root:yaneznauyparol@cluster0.rqcxk.mongodb.net/test";
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public MongoDB() {
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(java.util.logging.Level.SEVERE);
        mongoClient = new MongoClient(new MongoClientURI(MongoURI));
        database = mongoClient.getDatabase("data");
        collection = database.getCollection("test");
    }

    public Document getItemForId(String id) {
        Bson field = new Document("_id", id);
        return collection.find(field).first();
    }

    public void insertItem(Document doc) {
        String id = doc.get("_id").toString();
        if (getItemForId(id) == null) {
            collection.insertOne(doc);
        } else {
            Bson filter = new Document("_id", id);
            Bson updateDoc = new Document("$set", doc);
            collection.updateOne(filter, updateDoc);
        }
    }
}
