package com.sumesh.productservice.dao

import com.sumesh.productservice.model.Product
import com.sumesh.productservice.model.Review
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository


interface ProductRepository : MongoRepository<Product,ObjectId> {

    @Query("{name: {\$regex : '?0' ,\$options:\"six\"} }")
    fun searchByName(name:String): MutableList<Product>

    @Query("{addBy:?0}")
    fun getProductBySellerId(SellerId:ObjectId): MutableList<Product>

//    @Query("{reviews.email:?0}")
//    fun getReviewByEmail(email:String):Product?
}