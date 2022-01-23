package com.sumesh.productservice.controller

import com.sumesh.productservice.config.JwtRequestFilter
import com.sumesh.productservice.dao.ISellerRepository
import com.sumesh.productservice.model.Product
import com.sumesh.productservice.service.IProductService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid


@RestController
@RequestMapping("/product")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"],)
class ProductController {

    @Autowired
    private lateinit var iProductService: IProductService

    @Autowired
    private lateinit var iSellerRepositary: ISellerRepository

    @Autowired
    private lateinit var  jwtRequestFilter: JwtRequestFilter

    @PostMapping("/upload")
    fun uploadToS3(@RequestParam("file") file: MultipartFile):ResponseEntity<String>{
        if(file.isEmpty){
            return ResponseEntity<String>("file is empty",HttpStatus.INTERNAL_SERVER_ERROR)
        }
        if(!file.contentType.equals("image/jpeg")) {
            return ResponseEntity<String>("file is not image type", HttpStatus.INTERNAL_SERVER_ERROR)
        }
        return ResponseEntity<String>(iProductService.uploadFile(file),HttpStatus.OK)
    }



    @PostMapping("/addProduct")
    fun addNewProduct(@Valid @RequestBody product: Product):ResponseEntity<String>{
        val user_id = iSellerRepositary.findByEmail(jwtRequestFilter.email)?._id
        return ResponseEntity(user_id?.let { iProductService.addProduct(product, it) },HttpStatus.OK)
    }

    @GetMapping("/getProduct/{id}")
    fun getProductById(@PathVariable id:ObjectId):ResponseEntity<Any>{
        return ResponseEntity(iProductService.getProductById(id),HttpStatus.OK)
    }

    @DeleteMapping("/delete/{id}")
    fun deleteProductById(@PathVariable id: ObjectId):ResponseEntity<String>{
        return ResponseEntity(iProductService.deleteProductById(id,jwtRequestFilter.email),HttpStatus.OK)
    }

    @GetMapping("/getAllProducts")
    fun getAllProducts():ResponseEntity<MutableList<Product>>{
        return ResponseEntity(iProductService.getAllProducts(),HttpStatus.OK)
    }

    @GetMapping("/getAllProductsByMe")
    fun getAllProductsByMe():ResponseEntity<MutableList<Product>>{
        var sellerId = iSellerRepositary.findByEmail(jwtRequestFilter.email)?._id
        return ResponseEntity(sellerId?.let { iProductService.getAllProductsByParticularSeller(it) },HttpStatus.OK)
    }

//    @GetMapping("/getAllProductsBySeller/{sellerId}")
//    fun getAllProductsBySeller(@PathVariable sellerId:ObjectId):ResponseEntity<MutableList<Product>>{
//        return ResponseEntity(iProductService.getAllProductsByParticularSeller(sellerId),HttpStatus.OK)
//    }

    @PatchMapping("/update/{id}")
    fun updateProductsById(@Valid @RequestBody product:Product,@PathVariable id: ObjectId ):ResponseEntity<Any?>{
        return ResponseEntity(iProductService.updateProductById(id, product,jwtRequestFilter.email),HttpStatus.OK)
    }

    @GetMapping("/searchByName/{name}")
    fun searchByName(@PathVariable name:String):ResponseEntity<MutableList<Product>>{
        return ResponseEntity(iProductService.searchByName(name),HttpStatus.OK)
    }

}