package com.maks1mka.db;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class UserCollection {
    MongoDBConnect connection;
    MongoCollection<Document> users;
    public UserCollection() {
        connection = new MongoDBConnect();
        users = connection.getDb().getCollection("users");
    }
    public Document gerUserById(long userId) {
        if(users.find(new Document("_id", userId)).first() == null) {
            addUser(userId);
        }
        return users.find(new Document("_id", userId)).first();
    }
    public void updateUserInfo(long userId, String key, long value) {
        users.updateOne(new Document("_id", userId), new Document("$set", new Document(key, value)));
    }
    public void updateUserAbout(long userId, String aboutValue) {
        users.updateOne(new Document("_id", userId), new Document("$set", new Document("about", aboutValue)));
    }
    public void addUser(long userId) {
        users.insertOne(connection.getMongoClient().startSession(),new Document("_id", userId).append("xp", 1L).append("lvl", 1L).append("msg", 1L).append("currency", 1L).append("marry", 1L));

    }
    public void delUser(long userId) {
        users.deleteOne(new Document("_id", userId));
    }
    public Object getTopUsers(String key) {
        return users.find().sort(new Document(key, -1)).limit(10);
    }

}
