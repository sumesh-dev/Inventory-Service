package com.sumesh.productservice.service


import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.sumesh.productservice.dao.ISellerRepository
import com.sumesh.productservice.dao.ProductRepository
import com.sumesh.productservice.model.Product
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*

@Service
class ProductServiceImpl : IProductService {
    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var iSellerRepositary: ISellerRepository

    @Autowired
    private lateinit var amazonS3Client: AmazonS3Client

    @Value("\${aws.s3.bucket}")
    private val bucketName: String? = null

    override fun addProduct(product: Product, user_id: ObjectId): String {
            product.addBy = user_id
            productRepository.save(product)
            return  "product created successfully"
    }

    override fun getProductById(id: ObjectId): Any {
        return if(productRepository.existsById(id)){
             productRepository.findById(id).get()
        }
        else{
            "product does not exist"
        }
    }

    override fun deleteProductById(id: ObjectId,email:String): String {
        try {
            var product = productRepository.findById(id).get()
            var userDao = iSellerRepositary.findByEmail(email)
            return if (productRepository.existsById(id)) {
                return if (userDao?.role == "ROLE_admin") {
                    try {
                        val fileName = product.img.substringAfterLast("/")
                        amazonS3Client.deleteObject(DeleteObjectRequest(bucketName,fileName))
                    }
                    catch (e:Exception){
                        return "Cannot deleted Products Now"
                    }
                    productRepository.deleteById(id);
                    "product deleted"
                } else
                    if (product.addBy == userDao?._id) {
                        try {
                            val fileName = product.img.substringAfterLast("/")
                            amazonS3Client.deleteObject(DeleteObjectRequest(bucketName,fileName))
                        }
                        catch (e:Exception){
                            return "Cannot deleted Products Now"
                        }
//                        var fileName = product.img.substringAfterLast("/")
//                        amazonS3Client.deleteObject(DeleteObjectRequest(bucketName,fileName))
                        productRepository.deleteById(id);
                        "product deleted"
                    }
                    else {
                        "you are not authorize to delete this product"
                    }
            } else {
                "product not found"
            }
        }
        catch(e : NoSuchElementException){
            return "No such element exist"
        }
//        catch(e:Exception){
//            return "No element exception"
//        }
    }

    override fun getAllProducts(): MutableList<Product> {
        return productRepository.findAll()
    }

    override fun getAllProductsByParticularSeller(sellerId: ObjectId): MutableList<Product>? {
        return productRepository.getProductBySellerId(sellerId)
    }



    override fun updateProductById(id: ObjectId, product: Product,email: String): Any? {
        var productfromdatabase:Product? = productRepository.findById(id).get()
        var userDao = iSellerRepositary.findByEmail(email)
        return if (productfromdatabase!=null) {
            return if (productfromdatabase.addBy == userDao?._id) {
                if(productfromdatabase.img!=product.img){
                    try {
                        val fileName = productfromdatabase.img.substringAfterLast("/")
                        amazonS3Client.deleteObject(DeleteObjectRequest(bucketName,fileName))
                    }
                    catch (e:Exception){
                        return "Cannot deleted Products Now"
                    }
                }
                product._id = id
                product.addBy = productfromdatabase.addBy
                productRepository.save(product)
                "product update successfully"
            } else {
                "you are not authorize to update this product"
            }
        } else {
            "product not found"
        }
    }
    override fun searchByName(name: String): MutableList<Product> {
        return productRepository.searchByName(name)
    }

    override fun uploadFile(file: MultipartFile): String {

        var extension:String? = StringUtils.getFilenameExtension(file.originalFilename)
        var key:String = UUID.randomUUID().toString()+"."+extension;

        var metaData:ObjectMetadata = ObjectMetadata()
        metaData.contentLength = file.size
        metaData.contentType = file.contentType

        try{
            amazonS3Client.putObject(bucketName,key,file.inputStream,metaData)
        }
        catch (e:IOException){
            return "error occurred while uploading the file"
        }
        amazonS3Client.setObjectAcl("ecommerce-image-bucket1",key,CannedAccessControlList.PublicRead)
        return amazonS3Client.getResourceUrl("ecommerce-image-bucket1",key)
    }
}