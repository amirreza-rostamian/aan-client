package ir.amin.HaftTeen.vasni.model.teentaak

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ir.amin.HaftTeen.vasni.extention.getRandomColor
import java.io.Serializable


class Category : Serializable {
    @SerializedName("id")
    @Expose
    var id: String = ""
    @SerializedName("title")
    @Expose
    var name: String = ""
    @SerializedName("pic")
    @Expose
    var picture: String? = ""
    @SerializedName("children")
    @Expose
    var childrenList: List<SubCategory> = ArrayList()

    constructor(id: String, name: String, picture: String) {
        this.id = id
        this.name = name
        this.picture = picture
    }

    constructor()
}


class SubCategory : Serializable {
    @SerializedName("id")
    @Expose
    var id: String = ""
    @SerializedName("parent")
    @Expose
    var categoryId: String = ""
    @SerializedName("title")
    @Expose
    var name: String = ""
    @SerializedName("isSelected")
    @Expose
    var isSelected = false
    @SerializedName("isExpanded")
    @Expose
    var isExpanded = true
    var color: String = ""

    constructor(id: String, categoryId: String, name: String) {
        this.id = id
        this.categoryId = categoryId
        this.name = name
        this.color = getRandomColor()
    }

    constructor(subCategory: SubCategory) {
        this.id = subCategory.id
        this.categoryId = subCategory.categoryId
        this.name = subCategory.name
        this.isSelected = subCategory.isSelected
        this.color = getRandomColor()
    }

    constructor()

}

class MultiMediasCategoryModel : Serializable {
    var category = Category()
    var subCategoryList: ArrayList<SubCategory> = ArrayList()
    var isSelected = false

    constructor()
    constructor(
            category: Category,
            subCategoryList: ArrayList<SubCategory>,
            isSelected: Boolean
    ) {
        this.category = category
        this.subCategoryList = subCategoryList
        this.isSelected = isSelected
    }


}


class UserVideoCategory : Serializable {
    @SerializedName("id")
    @Expose
    var hash_id: String = ""
    @SerializedName("title")
    @Expose
    var title: String = ""
    @SerializedName("children")
    @Expose
    var children: ArrayList<UserVideoCategory> = ArrayList()
}