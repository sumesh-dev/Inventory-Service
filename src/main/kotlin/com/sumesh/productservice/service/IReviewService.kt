package com.sumesh.productservice.service

import com.sumesh.productservice.model.Review
import org.bson.types.ObjectId

interface IReviewService {
    fun addReview(review: Review,id:ObjectId):Any
    fun deleteReview(email:String,id:ObjectId):Any
}