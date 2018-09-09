package com.kzonix.community.blog.model.services


import com.mongodb.reactivestreams.client.MongoClient
import io.micronaut.context.annotation.Value
import org.reactivestreams.Publisher

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PublicationManagementService {

    @Value('${mongodb.dbName:databaseName}')
    protected String dbName

    String getDbName() {
        return dbName
    }

    void setDbName(String dbName) {
        this.dbName = dbName
    }
    @Inject
    MongoClient mongoClient

    Publisher<String> get() {
        mongoClient.getDatabase(dbName).createCollection("a" + new Random().nextInt()) }

}
