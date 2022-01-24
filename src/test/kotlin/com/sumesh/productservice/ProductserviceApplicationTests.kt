package com.sumesh.productservice

import com.sumesh.productservice.dao.ProductRepository
import com.sumesh.productservice.model.Product
import com.sumesh.productservice.model.Review
import com.sumesh.productservice.service.IProductService
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.*

@SpringBootTest
class ProductserviceApplicationTests {

    @Autowired
    private lateinit var iProductService: IProductService

    @MockBean
    private lateinit var productRepository: ProductRepository

	@Test
	fun addProductTest(){
    val product:Product = Product(ObjectId("61ee597e731a3b6965939a02"),"xyz","http://xyz.com",200,"jcnjdvbnnvjvn",ObjectId("61ee597e731a3b6965939a02"),
        mutableListOf<Review>())
        Mockito.`when`(productRepository.save(product)).thenReturn(product)
        Mockito.`when`(productRepository.findById(product._id)).thenReturn(Optional.of(product))
        Assertions.assertEquals("product created successfully",iProductService.addProduct(product,ObjectId("61ee597e731a3b6965939a02")))
    }

    @Test
    fun deleteProductTest(){
        val product:Product = Product(ObjectId("61ee597e731a3b6965939a02"),"xyz","http://xyz.com",200,"jcnjdvbnnvjvn",ObjectId("61ee597e731a3b6965939a02"),
            mutableListOf<Review>())
        Mockito.`when`(productRepository.save(product)).thenReturn(product)
        Mockito.`when`(productRepository.findById(product._id)).thenReturn(Optional.of(product))
        Assertions.assertEquals("product created successfully",iProductService.addProduct(product,ObjectId("61ee597e731a3b6965939a02")))
    }

    @Test
    fun getProductByIdTest(){
        val product:Product = Product(ObjectId("61ee597e731a3b6965939a02"),"xyz","http://xyz.com",200,"jcnjdvbnnvjvn",ObjectId("61ee597e731a3b6965939a02"),
            mutableListOf<Review>())
        Mockito.`when`(productRepository.save(product)).thenReturn(product)
        Mockito.`when`(productRepository.findById(product._id)).thenReturn(Optional.of(product))
        Assertions.assertEquals("product does not exist",iProductService.getProductById(product._id,))
    }

}
