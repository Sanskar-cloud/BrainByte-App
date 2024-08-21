package dev.sanskar.featuretesteduco.feature_course.data.remote

// ApiService.kt
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.sanskar.featuretesteduco.core.domain.models.PaymentStatus
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Query
@Serializable
data class PaymentRequest(val teacherId: String, val teacherName: String, val teacherEmail: String)
@Serializable
data class PaymentRequestStudent(
    val StudentId: String,
    val StudentName: String,
    val StudentEmail: String,
    val courseId:String
)
@Serializable
data class PaymentResponse(val paymentLinkUrl: String, val paymentLinkId: String)
data class ApiResponse(val message: String, val success: Boolean)
data class CourseRequest(val teacherId: String, val title: String, val description: String)
//data class Course(val id: String, val teacherId: String, val title: String, val description: String)

interface PaymentApi {
    @POST("/createPayment")
    suspend fun createPayment(@Body paymentRequest: PaymentRequest): PaymentResponse

    @POST("/createPaymentStudent")
    suspend fun createPaymentForStudent(@Body paymentRequest: PaymentRequestStudent): PaymentResponse
    @GET("/api/stdnt/pay")
    suspend fun findStudentByPayment(@Query("courseId")courseId: String, @Query("userId")userId: String,
    @Query("status")status: PaymentStatus):Boolean


    @GET("/api/paymentCallback")
    suspend fun handlePaymentCallback(@Query("payment_id") paymentId: String, @Query("order_id") orderId: String): ApiResponse




    companion object {
        private const val BASE_URL = "http://192.168.0.120:8082"


        fun create(): PaymentApi {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()

            return retrofit.create(PaymentApi::class.java)
        }
    }
}
