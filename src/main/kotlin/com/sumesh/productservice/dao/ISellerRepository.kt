package com.sumesh.productservice.dao

import com.sumesh.productservice.model.Seller
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface ISellerRepository: MongoRepository<Seller,ObjectId>{
    @Query("{email:?0}")
    fun findByEmail(email: String?): Seller?
}