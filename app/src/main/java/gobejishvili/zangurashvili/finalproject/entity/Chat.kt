package gobejishvili.zangurashvili.finalproject.entity

import java.util.Date

data class Chat(var senderId : Int, var getterId : Int, var text : String, var sendTime : Date)