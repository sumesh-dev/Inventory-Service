package com.sumesh.productservice.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Document(collection = "Products" )
class Product{
    @Id
    lateinit var _id: ObjectId

    @field:NotBlank(message = "Product name is mandatory")
    lateinit var name:String

    @field:NotBlank(message="insert image url")
    lateinit var img:String

    @field:Min(1)
    @field:NotNull(message = "The price shold not be blank")
     var price: Int = 1

    @field:NotNull(message = "give some description")
    lateinit var desc: String
//    @field:NotNull(message="Only authorize user can add the product")
     lateinit var addBy: ObjectId
     var reviews:MutableList<Review> = mutableListOf<Review>()

    constructor()

    constructor(
        _id: ObjectId,
        name: String,
        img: String,
        price: Int,
        desc: String,
        addBy: ObjectId,
        reviews: MutableList<Review>
    ) {
        this._id = _id
        this.name = name
        this.img = img
        this.price = price
        this.desc = desc
        this.addBy = addBy
        this.reviews = reviews
    }

    constructor(name: String, img: String, price: Int, desc: String, addBy: ObjectId, reviews: MutableList<Review>) {
        this.name = name
        this.img = img
        this.price = price
        this.desc = desc
        this.addBy = addBy
        this.reviews = reviews
    }

}
