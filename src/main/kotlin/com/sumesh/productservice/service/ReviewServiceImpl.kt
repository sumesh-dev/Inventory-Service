package com.sumesh.productservice.service

import com.sumesh.productservice.dao.ProductRepository
import com.sumesh.productservice.model.Product
import com.sumesh.productservice.model.Review
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReviewServiceImpl:IReviewService {

    @Autowired
    private lateinit var productRepository: ProductRepository

    override fun addReview(review: Review, id: ObjectId): Any {
        var product:Product = productRepository.findById(id).get()
            var databaseReview = product.reviews.find { it.email == review.email }
            println(databaseReview)
            if(databaseReview == null){
                product.reviews.add(review)
                productRepository.save(product)
                return "review added"
            }
        return "some error occurred"
    }

    override fun deleteReview(email:String, id: ObjectId): Any {
        var product:Product = productRepository.findById(id).get()
        product.reviews.removeIf { it.email==email }
        productRepository.save(product)
        return "review deleted"
    }

}