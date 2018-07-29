package kiyosuke.com.csvparser

import kiyosuke.com.water.annotation.Header

class Dummy {
    @Header("名前")
    var name: String? = null
    @Header("ふりがな")
    var kana: String? = null
    @Header("性別")
    var sex: Int? = null
    @Header("年齢")
    var age: Int? = null
    @Header("誕生日")
    var birthDay: String? = null

    override fun toString(): String {
        return buildString {
            append("名前: $name").append(",")
                    .append("ふりがな: $kana").append(",")
                    .append("性別: $sex").append(",")
                    .append("年齢: $age").append(",")
                    .append("誕生日: $birthDay")
        }
    }
}