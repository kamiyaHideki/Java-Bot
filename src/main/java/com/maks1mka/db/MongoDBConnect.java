package com.maks1mka.db;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnect {
    private final String passDb = "password";
    private final ConnectionString connectionString = new ConnectionString("mongodb+srv://dbAdmin:" + passDb + "@database.wrckl.mongodb.net/solution?retryWrites=true&w=majority");
    private final MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();
    private final MongoClient mongoClient = MongoClients.create(settings);
    private final MongoDatabase db = mongoClient.getDatabase("solution");
    public MongoClient getMongoClient() {
        return mongoClient;
    }
    public MongoDatabase getDb() {
        return db;
    }
}
