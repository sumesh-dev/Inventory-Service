package com.sumesh.productservice.service

import com.sumesh.productservice.model.Product
import org.bson.types.ObjectId
import org.springframework.web.multipart.MultipartFile

interface IProductService {
    fun addProduct(product: Product,user_id:ObjectId):String
    fun getProductById(id:ObjectId):Any
    fun deleteProductById(id: ObjectId,email:String):String
    fun getAllProducts():MutableList<Product>
    fun getAllProductsByParticularSeller(sellerId: ObjectId):MutableList<Product>?
    fun updateProductById(id:ObjectId, product: Product,email: String):Any?
    fun searchByName(name:String):MutableList<Product>
    fun uploadFile(file:MultipartFile):String
}