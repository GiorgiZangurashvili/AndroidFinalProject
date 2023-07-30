package gobejishvili.zangurashvili.finalproject.entity

data class User(
    var userId: String,
    var username: String,
    var profilePictureUrl: String,
    var profession: String
){
    constructor() : this("", "", "", "")
}

