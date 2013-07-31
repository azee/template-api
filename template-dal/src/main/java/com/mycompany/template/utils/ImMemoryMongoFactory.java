package com.mycompany.template.utils;

import com.foursquare.fongo.Fongo;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * User: azee
 */
public class ImMemoryMongoFactory {
    private static Fongo fongo = new Fongo("Server name");

    public static MongoOperations provide() {
        return new MongoTemplate(fongo.getMongo(), "database");
    }
}
