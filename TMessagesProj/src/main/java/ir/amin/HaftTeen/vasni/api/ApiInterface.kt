package ir.amin.HaftTeen.vasni.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {

    @GET("user/login")
    fun login(@HeaderMap headers: Map<String, String>): Call<JsonObject>

    @GET("otp/check?")
    fun checkTci(@Query("mobile") phone: String): Call<JsonObject>

    @Multipart
    @POST("user/verify")
    fun verifyUser(@HeaderMap headers: Map<String, String>, @Part("code") code: RequestBody): Call<JsonObject>

    @Multipart
    @POST("user/profile")
    fun registerUser(
            @Part("name") name: RequestBody,
            @Part("phone") phone: RequestBody,
            @Part("grade") grade: RequestBody,
            @Part("province") province: RequestBody,
            @Part("gender") gender: RequestBody,
            @Part("age") age: RequestBody,
            @Part("birthday") birthday: RequestBody,
            @Part file: MultipartBody.Part
    ): Call<JsonObject>

    @Multipart
    @POST("user/profile")
    fun registerUser(
            @Part("name") name: RequestBody,
            @Part("phone") mobile: RequestBody,
            @Part("grade") grade: RequestBody,
            @Part("province") province: RequestBody,
            @Part("gender") gender: RequestBody,
            @Part("age") age: RequestBody,
            @Part("birthday") birthday: RequestBody
    ): Call<JsonObject>

    @GET("app/pages")
    fun getPages(@Query("id") id: Int): Call<JsonObject>

    @GET("news/list")
    fun getNews(@QueryMap query: Map<String, String>): Call<JsonObject>

    @GET("pictures/category")
    fun getPictureCategory(@Query("program") program: String, @Query("source") source: String): Call<JsonObject>

    @GET("pictures/list")
    fun getPicture(@Query("program") program: String, @Query("category") category: String, @Query(
            "source"
    ) source: String, @Query(
            "limit"
    ) limit: String, @Query("offset") offset: String
    ): Call<JsonObject>

    @Multipart
    @POST("pictures/upload")
    fun uploadPic(
            @Part("title") title: RequestBody,
            @Part("category") category: RequestBody,
            @Part file: MultipartBody.Part
    ): Call<JsonObject>

    @GET("videos/category")
    fun getVideoCategory(@Query("program") program: String, @Query("source") source: String): Call<JsonObject>

    @GET("videos/list")
    fun getVideo(@Query("program") program: String, @Query("category") category: String, @Query(
            "source"
    ) source: String, @Query(
            "limit"
    ) limit: String, @Query("offset") offset: String
    ): Call<JsonObject>

    @Multipart
    @POST("ui/file/upload")
    fun uploadVideo(@Part file: MultipartBody.Part, @Part("type") type: RequestBody, @Part("page") page: RequestBody): Call<JsonObject>

    @Multipart
    @POST("videos/upload")
    fun submitUploadVideo(
            @Part("title") title: RequestBody,
            @Part("category") category: RequestBody,
            @Part("video") video: RequestBody
    ): Call<JsonObject>

    @GET("pictures/like")
    fun likeUsersPicture(@Query("source_id") source_id: Int): Call<JsonObject>

    @GET("pictures/dislike")
    fun dislikeUsersPicture(@Query("source_id") source_id: Int): Call<JsonObject>

    @GET("pictures/views")
    fun viewUsersPicture(@Query("source_id") source_id: Int): Call<JsonObject>

    @GET("pictures/delete")
    fun deleteUsersPicture(@Query("source_id") source_id: Int): Call<JsonObject>

    @GET("videos/like")
    fun likeUsersVideo(@Query("source_id") source_id: Int): Call<JsonObject>

    @GET("videos/dislike")
    fun dislikeUsersVideo(@Query("source_id") source_id: Int): Call<JsonObject>

    @GET("videos/views")
    fun viewUsersVideo(@Query("source_id") source_id: Int): Call<JsonObject>

    @GET("videos/delete")
    fun deleteUsersVideo(@Query("source_id") source_id: Int): Call<JsonObject>

    @GET("match/list")
    fun getMatch(@Query("match") match: String, @Query("grade") grade: Int): Call<JsonObject>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("match/answer")
    fun sendAnswer(@Query("match") match: String, @Body jsonArray: JsonArray): Call<JsonObject>

    @GET("polls/list")
    fun getPollList(@QueryMap query: Map<String, String>): Call<JsonObject>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("polls/answer")
    fun sendpollAnswer(@Body jsonArray: JsonArray): Call<JsonObject>

    @GET("competitions/competition")
    fun getData(@Query("limit") limit: Int): Call<JsonObject>

    @GET("competitions/scoreboard-program")
    fun getScoreBoard(@Query("program") program: String, @Query("limit") limit: Int): Call<JsonObject>

    @GET("static-pages/aboutus")
    fun getAbout(@HeaderMap headers: Map<String, String>): Call<JsonObject>

    @GET("static-pages/contactus")
    fun getContactUs(@HeaderMap headers: Map<String, String>): Call<JsonObject>

    @GET("static-pages/page")
    fun getStaticPage(@Query("id") id: String): Call<JsonObject>

    @Multipart
    @POST("complains/complain")
    fun complain(@Part("content") content: RequestBody, @Part("tileId") tileId: RequestBody): Call<JsonObject>
//    fun complain(@Part("content") content: RequestBody, @Part("category") category: RequestBody): Call<JsonObject>

    @GET("app/version")
    fun getVersion(@HeaderMap headers: Map<String, String>): Call<JsonObject>

    @GET("lottery/lottery-program")
    fun convertScore(@Query("program") program: Int): Call<JsonObject>

    @GET("lottery/program-points")
    fun getUserPoint(@Query("program") program: Int): Call<JsonObject>

    @GET("user/getprofile")
    fun getSimpleUserDetail(): Call<JsonObject>

    @GET("user/charge")
    fun checkVoucher(@Query("pin") pin: String, @Query("phone") phone: String): Call<JsonObject>

    @GET("user/active")
    fun loginTci(): Call<JsonObject>

    @GET("user/charge")
    fun chargeTci(@Query("pin") pin: String): Call<JsonObject>

    @GET("api/category/videos")
    fun getVitrinVideo(@Query("id") id: String, @Query("catId") catId: String): Call<JsonObject>

    @GET("api/video/show")
    fun getVitrinVideoDetail(@Query("id") id: String): Call<JsonObject>

    @GET("api/video/sub-category/")
    fun getSubCategoryVitrinVideo(@Query("id") id: String): Call<JsonObject>

    @GET("api/category/games")
    fun getVitrinGame(@Query("id") id: String, @Query("catId") catId: String): Call<JsonObject>

    @GET("api/game/show")
    fun getVitrinGameDetail(@Query("id") id: String): Call<JsonObject>

    @GET("api/category/musics")
    fun getVitrinMusic(@Query("id") id: String, @Query("catId") catId: String): Call<JsonObject>

    @GET("api/music/show")
    fun getVitrinMusicDetail(@Query("id") id: String): Call<JsonObject>

    @GET("api/category/books")
    fun getVitrinBook(@Query("id") id: String, @Query("catId") catId: String): Call<JsonObject>

    @GET("api/book/show")
    fun getVitrinBookDetail(@Query("id") id: String): Call<JsonObject>

    @Multipart
    @POST("api/game/download")
    fun downloadVitrinGame(
            @Part("id") id: RequestBody,
            @Part("value") value: RequestBody
    ): Call<JsonObject>

    @Multipart
    @POST("api/game/rate")
    fun ratingVitrinGame(
            @Part("id") id: RequestBody,
            @Part("value") value: RequestBody
    ): Call<JsonObject>

    @Multipart
    @POST("api/game/comment")
    fun commentVitrinGame(
            @Part("id") id: RequestBody,
            @Part("value") value: RequestBody
    ): Call<JsonObject>

    @Multipart
    @POST("api/book/rate")
    fun ratingVitrinBook(
            @Part("id") id: RequestBody,
            @Part("value") value: RequestBody
    ): Call<JsonObject>

    @Multipart
    @POST("api/book/comment")
    fun commentVitrinBook(
            @Part("id") id: RequestBody,
            @Part("value") value: RequestBody
    ): Call<JsonObject>


    @Multipart
    @POST("api/music/rate")
    fun ratingVitrinMusic(
            @Part("id") id: RequestBody,
            @Part("value") value: RequestBody
    ): Call<JsonObject>

    @Multipart
    @POST("api/music/comment")
    fun commentVitrinMusic(
            @Part("id") id: RequestBody,
            @Part("value") value: RequestBody
    ): Call<JsonObject>

    @Multipart
    @POST("api/video/rate")
    fun ratingVitrinVideo(
            @Part("id") id: RequestBody,
            @Part("value") value: RequestBody
    ): Call<JsonObject>

    @Multipart
    @POST("api/video/comment")
    fun commentVitrinVideo(
            @Part("id") id: RequestBody,
            @Part("value") value: RequestBody
    ): Call<JsonObject>

    @Multipart
    @POST("api/package/rate")
    fun ratingVitrinPackage(
            @Part("id") id: RequestBody,
            @Part("value") value: RequestBody
    ): Call<JsonObject>

    @Multipart
    @POST("api/package/comment")
    fun commentVitrinPackage(
            @Part("id") id: RequestBody,
            @Part("value") value: RequestBody
    ): Call<JsonObject>

    @GET("api/video/filter")
    fun filterVitrinVideo(@Query("id") id: String): Call<JsonObject>

    @GET("api/game/filter")
    fun filterVitrinGame(@Query("id") id: String): Call<JsonObject>

    @GET("api/book/filter")
    fun filterVitrinBook(@Query("id") id: String): Call<JsonObject>

    @GET("api/music/filter")
    fun filterVitrinMusic(@Query("id") id: String): Call<JsonObject>

    @GET("match/leagues")
    fun getMatchLeagues(@Query("category") category: String, @Query("league") league: String): Call<JsonObject>

    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST("match/submit")
    fun leagueSubmit(@Body jsonObject: JsonObject): Call<JsonObject>

    @GET("user/points-program")
    fun getProgramScores(@Query("program") program: String): Call<JsonObject>

    @GET("lottery/all-program-points")
    fun getScores(): Call<JsonObject>

    @GET("media/category")
    fun getMediaCategory(@Query("program") program: String, @Query("source") source: String): Call<JsonObject>

    @GET("media/list")
    fun getMedia(@Query("tile") tile: String, @Query("category") category: String): Call<JsonObject>

    @GET("media/views")
    fun mediaView(@Query("source_id") source_id: String): Call<JsonObject>

    @GET("media/play")
    fun getMediaFile(@Query("id") id: String): Call<JsonObject>

    @GET("media/like")
    fun likeMedia(@Query("source_id") source_id: String): Call<JsonObject>

    @GET("media/dislike")
    fun dislikeMedia(@Query("source_id") source_id: String): Call<JsonObject>

    @Multipart
    @POST("repo/upload")
    fun uploadMedia(
            @Part file: MultipartBody.Part,
            @Part("file_content") file_content: RequestBody,
            @Part("file_name") file_name: RequestBody,
            @Part("file_type") file_type: RequestBody,
            @Part("access_mode") access_mode: RequestBody,
            @Part("provider_code") provider_code: RequestBody,
            @Part("submit") submit: RequestBody
    ): Call<JsonObject>


    @Multipart
    @POST("media/upload")
    fun submitUploadMedia(
            @Part("file_id") file_id: RequestBody,
            @Part("category") category: RequestBody,
            @Part("type") type: RequestBody,
            @Part("title") title: RequestBody,
            @Part file: MultipartBody.Part,
            @Part thumbnail: MultipartBody.Part
    ): Call<JsonObject>

    @GET("media/delete")
    fun deleteUsersMedia(@Query("source_id") source_id: String): Call<JsonObject>

    @GET("api/video/episodes")
    fun getVitrinVideoEpisodes(@Query("id") id: String): Call<JsonObject>

    @GET("match/play")
    fun getMatchMediaFile(@Query("file") file: String): Call<JsonObject>

    @GET("repo/get_media_handle")
    fun getVitrinRahpoLink(@Query("provider_code") provider_code: String, @Query("content_id") content_id: String, @Query("user_name") user_name: String, @Query("password") password: String): Call<JsonObject>

    @GET("wallet/sharj")
    fun checkWalletVoucher(@Query("voucher") voucher: String): Call<JsonObject>

    @GET("wallet/reduce")
    fun getWalletReduce(@Query("product") product: String, @Query("type") type: String): Call<JsonObject>

    @GET("html-game/generate")
    fun HtmlGameGenerate(@Query("id") id: String): Call<JsonObject>

    @GET("menu/menus")
    fun getVitrinMenu(@Query("vitrine") vitrine: String, @Query("category") category: String, @Query("tag") tag: String): Call<JsonObject>

    @GET("menu/products")
    fun searchByName(@Query("vitrine") vitrine: String, @Query("product") product: String): Call<JsonObject>

    @GET("page/pages")
    fun getVitrinPage(@Query("vitrine") vitrine: String, @Query("pageId") pageId: String): Call<JsonObject>

    @GET("page/tags")
    fun getVitrinTag(@Query("vitrine") vitrine: String, @Query("sourceID") sourceID: String, @Query("isBanner") isBanner: String): Call<JsonObject>

    @GET("api/package/show")
    fun getVitrinPackageDetail(@Query("id") id: String): Call<JsonObject>

    @GET("channels/list")
    fun getChannelInfo(@Query("id") id: String): Call<JsonObject>

    @GET("channels/list")
    fun getChannels(@Query("category") category: String): Call<JsonObject>

    @GET("authenticate/setting")
    fun getSetting(@Query("serviceId") serviceId: String): Call<JsonObject>

    @GET("videos/{id}")
    fun getAbrArvanLink(@HeaderMap headers: Map<String, String>, @Path("id") id: String): Call<JsonObject>

    @GET("user/wallet-balance")
    fun getWalletBalance(): Call<JsonObject>

    @GET("user/getprofile")
    fun getProfile(): Call<JsonObject>

    @POST("channels/{channel_id}/videos")
    fun uploadVideoAbrArvan(@HeaderMap headers: Map<String, String>, @Path("channel_id") channel_id: String, @Body jsonObject: JsonObject): Call<JsonObject>

    @Multipart
    @POST("news/comment-save")
    fun newsComment(
            @Part("source_id") source_id: RequestBody,
            @Part("comment") comment: RequestBody
    ): Call<JsonObject>

    @Multipart
    @POST("media/comment-save")
    fun mediaComment(
            @Part("source_id") source_id: RequestBody,
            @Part("comment") comment: RequestBody
    ): Call<JsonObject>

    @Multipart
    @POST("pictures/comment-save")
    fun pictureComment(
            @Part("source_id") source_id: RequestBody,
            @Part("comment") comment: RequestBody
    ): Call<JsonObject>

}