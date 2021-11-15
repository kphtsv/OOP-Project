package ru.betry;

import org.bson.Document;

public interface IRepository {

    public void checkAndPush (Document doc);

    public Document getItem (String id);
}
