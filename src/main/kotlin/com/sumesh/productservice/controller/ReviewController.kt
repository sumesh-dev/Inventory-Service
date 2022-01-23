package com.sumesh.productservice.controller

import com.sumesh.productservice.config.JwtRequestFilter
import com.sumesh.productservice.model.Review
import com.sumesh.productservice.service.IReviewService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/review")
class ReviewController {

    @Autowired
    private lateinit var jwtRequestFilter: JwtRequestFilter

    @Autowired
    private lateinit var iReviewService: IReviewService

    @PostMapping("/{id}")
    fun addReview(@PathVariable id:ObjectId,@Valid @RequestBody review:Review):ResponseEntity<Any>{
        review.email = jwtRequestFilter.email
        return ResponseEntity<Any>(iReviewService.addReview(review,id),HttpStatus.OK)
    }

    @DeleteMapping("/{id}")
    fun deleteReview(@PathVariable id:ObjectId):ResponseEntity<Any>{
        return ResponseEntity<Any>(iReviewService.deleteReview(jwtRequestFilter.email,id),HttpStatus.OK)
    }

}